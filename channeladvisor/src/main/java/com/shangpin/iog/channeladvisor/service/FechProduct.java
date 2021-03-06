package com.shangpin.iog.channeladvisor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("channeladvisor")
public class FechProduct {

	private static Logger logInfo  = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String access_token = "";
	private static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		access_token = bdl.getString("conf.access.token");
		day = Integer.valueOf(bdl.getString("day"));
	}

	@Autowired
	public ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;

	public void fetchProductAndSave() {

		String url = "https://api.channeladvisor.com/v1/Products?access_token="+access_token+"&";
		OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
		try{
			
			Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
			Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			while(StringUtils.isNotBlank(url) && !url.equals("null")){
				
				//方式1
				String content = HttpUtil45.get(url, timeConfig, null);
				JSONObject jsonObj = JSONObject.fromObject(content);
				if(jsonObj.containsKey("error")){
					access_token = getNewToken(timeConfig);
					url = url.replaceFirst(url.substring(url.indexOf("=")+1, url.indexOf("&")),access_token);
					System.out.println(url);
					content = HttpUtil45.get(url, timeConfig, null);
					//System.out.println(content);
					jsonObj = JSONObject.fromObject(content);
				}
				JSONArray array = jsonObj.getJSONArray("value");
				for(int i=0;i<array.size();i++){
					JSONObject obj = array.getJSONObject(i);
					String marketPrice = obj.getString("BuyItNowPrice");
					String stock = obj.getString("TotalAvailableQuantity");
					String skuId = obj.getString("Sku");
					
					//只有BuyItNowPrice不为空以及TotalAvailableQuantity不为空时才能入库
					if(!marketPrice.equals("null") && StringUtils.isNotBlank(marketPrice)&& !stock.equals("null") && StringUtils.isNotBlank(stock) && !stock.equals("0") && (skuId.startsWith("NY") || skuId.startsWith("ny"))){
						String categoryName = obj.getString("Classification");
						String brandName = obj.getString("Brand");
						String categoryGender = "";
						String material = "";
						String productOrigin = "";
						String id = obj.getString("ID");
						
						String color = "";
						String productName = "";
						String productCode = obj.getString("UPC");
						String productSize = "";
						
						String attributeUrl = "https://api.channeladvisor.com/v1/Products("+id+")/Attributes?access_token="+access_token+"&";
						String attributes = HttpUtil45.get(attributeUrl, timeConfig, null);
						JSONObject attrObj = JSONObject.fromObject(attributes);
						if(attrObj.containsKey("error")){
							access_token = getNewToken(timeConfig);
							attributeUrl = attributeUrl.replaceFirst(attributeUrl.substring(attributeUrl.indexOf("=")+1, attributeUrl.indexOf("&")),access_token);
							System.out.println(attributeUrl);
							attributes = HttpUtil45.get(attributeUrl, timeConfig, null);
							//System.out.println(content);
							attrObj = JSONObject.fromObject(attributes);
						}
						JSONArray attrArr = attrObj.getJSONArray("value");
						
						for(int j=0;j<attrArr.size();j++){
							JSONObject attr= attrArr.getJSONObject(j);
							switch (attr.getString("Name")) {
							case "Color":
								color = attr.getString("Value");
								break;
							case "ebay title":
								productName = attr.getString("Value");
								break;
							case "Size":
								productSize = attr.getString("Value");
								break;				
							case "Gender":
								categoryGender = attr.getString("Value");
								break;
							case "Material":
								material = attr.getString("Value");
								break;
							case "UPC":
								if(StringUtils.isBlank(productCode)){
									productCode = attr.getString("Value");
								}
								break;
							case "country of origin":
								productOrigin = attr.getString("Value");
							
							default:
								break;
							}
						}
						//图片
						String picUrl = "https://api.channeladvisor.com/v1/Products("+id+")/Images?access_token="+access_token+"&";
						String pics = HttpUtil45.get(picUrl, timeConfig, null);
						JSONObject picObj = JSONObject.fromObject(pics);
						if(picObj.containsKey("error")){
							access_token = getNewToken(timeConfig);
							picUrl = picUrl.replaceFirst(picUrl.substring(picUrl.indexOf("=")+1, picUrl.indexOf("&")),access_token);
							System.out.println(picUrl);
							pics = HttpUtil45.get(picUrl, timeConfig, null);
							picObj = JSONObject.fromObject(pics);
						}
						JSONArray picArr = picObj.getJSONArray("value");
						List<String> list = new ArrayList<String>();
						for(int k=0;k<picArr.size();k++){
							list.add(picArr.getJSONObject(k).getString("Url"));
						}
						//保存图片
						productFetchService.savePicture(supplierId, null, skuId, list);
						
						//入库sku
						SkuDTO sku = new SkuDTO();
			            sku.setId(UUIDGenerator.getUUID());
			            sku.setSupplierId(supplierId);
			            sku.setSkuId(skuId);
			            sku.setSpuId(id);
			            sku.setColor(color);
			            sku.setProductCode(productCode);
			            sku.setProductName(productName);
			            sku.setProductSize(productSize);
			            sku.setStock(stock);
			            sku.setSupplierId(supplierId);
			            sku.setMarketPrice(marketPrice);
			            sku.setSaleCurrency("USD");
			            try {
			            	if(skuDTOMap.containsKey(sku.getSkuId())){
								skuDTOMap.remove(sku.getSkuId());
							}
			                productFetchService.saveSKU(sku);
			                
			            } catch (ServiceException e) {
			                try {
			                    if (e.getMessage().equals("数据插入失败键重复")) {
			                        //更新价格和库存
			                        productFetchService.updatePriceAndStock(sku);
			                    } else {
			                        e.printStackTrace();
			                    }
			                } catch (ServiceException e1) {
			                	logError.error(e1.getMessage());
			                    e1.printStackTrace();
			                }
			            }
			            
			            //入库spu
			            SpuDTO spu = new SpuDTO();
			            spu.setId(UUIDGenerator.getUUID());
			            spu.setSpuId(id);
			            spu.setSupplierId(supplierId);
			            spu.setBrandName(brandName);
			            spu.setCategoryGender(categoryGender);
			            spu.setCategoryName(categoryName);
			            spu.setMaterial(material);
			            spu.setProductOrigin(productOrigin);
			            try {
			                productFetchService.saveSPU(spu);
			            } catch (ServiceException e) {
			            	logError.error(e.getMessage());
			            	try{
			            		productFetchService.updateMaterial(spu);
			            	}catch(ServiceException ex){
			            		logError.error(ex.getMessage());
			            		ex.printStackTrace();
			            	}
			            	
			                e.printStackTrace();
			            }
					}
		            
				}
				try{
					url = jsonObj.getString("@odata.nextLink");
					System.out.println(url);
				}catch(Exception ex){
					logError.error(ex.getMessage());
					System.out.println("-------------------最后一页啦-----------------");
					break;
				}
				
			}
			
			//更新网站不再给信息的老数据
			for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
				 Map.Entry<String,SkuDTO> entry =  itor.next();
				if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
					entry.getValue().setStock("0");
					try {
						productFetchService.updatePriceAndStock(entry.getValue());
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}
			
		}catch(Exception ex){
			logError.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public String getNewToken(OutTimeConfig timeConfig){
		String application_id = "qwmmx12wu7ug39a97uter3dz29jbij3j";
        String shared_secret = "TqMSdN6-LkCFA0n7g7DWuQ";
        Map<String,String> map = new HashMap<>();
        map.put("grant_type","refresh_token");
        map.put("refresh_token", "6Rz4sozjjOFbdazaU_gjnnFwWvfG2VgG9L14kL9tB3w");
        map.put("redirect_uri","https://49.213.13.167:8443/iog/download/code");
        String kk = HttpUtil45.postAuth("https://api.channeladvisor.com/oauth2/token", map, timeConfig,application_id,shared_secret);
        System.out.println("kk = "  + kk);
        return JSONObject.fromObject(kk).getString("access_token");
	}
}
