package com.shangpin.iog.russoCapri.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

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
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.russoCapri.dto.Item;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

/**
 * Created by houkun on 2015/11/26.
 */
@Component("russoCapri")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static String supplierId;
    private static String url;
	public static int day;
	private static String username,password;
    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
        day = Integer.valueOf(bdl.getString("day"));
        username = bdl.getString("username");
		password = bdl.getString("password");
    }
    @Autowired
    private ProductFetchService productFetchService;
    @Autowired
	ProductSearchService productSearchService;

    /**
     * 得到产品信息并储存
     */
    public void fetchProductAndSave(){
    	Map<String,Item> spuMap= new HashMap<String,Item>();
    	Map<String,String> imgMap= new HashMap<String,String>();
    	Map<String,String> priceMap= new HashMap<String,String>();
        //获取产品信息
        logger.info("get product starting....");
        System.out.println("开始获取产品信息");
        Map<String,String> map = new HashMap<>();
//    	String spuData = HttpUtil45.post(url+"GetAllItemsMarketplace", map,new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600),username,password);
        
    	String skuData = HttpUtil45.postAuth(url+"GetAllAvailabilityMarketplace", map,new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600),username,password);
    	
    	String imageData = HttpUtil45.postAuth(url+"GetAllImageMarketplace", map,new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600),username,password);
    	
    	String priceData = HttpUtil45.postAuth(url+"GetAllPricelistMarketplace", map,new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600),username,password);
    
    	String spuData = HttpUtil45.postAuth(url+"GetAllItemsMarketplace", map, new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600), username, password);
    	System.out.println("获取产品信息结束");
    	Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
//		Map<String,SkuDTO> skuDTOMap = new HashedMap();
//		try {
//			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
    	
        logger.info("get product over");
        //映射数据并保存
        logger.info("save product into DB begin");
        String data = "";
        //价格信息
        String[] priceStrings = priceData.split("\\r\\n");
        String[] priceArr = null;
        for (int i = 1; i < priceStrings.length; i++) {
        	if (StringUtils.isNotBlank(priceStrings[i])) {
				if (i==1) {
				  data =  priceStrings[i].split("\\n")[1];
				}else {
				  data = priceStrings[i];
				}
        	}
			priceArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
			priceMap.put(priceArr[0], priceArr[3]);
			
        }
        
        
        
        System.out.println("保存spu");
        //得到所有的spu信息
        String[] spuStrings = spuData.split("\\r\\n");
        String[] spuArr = null;
		for (int i = 1; i < spuStrings.length; i++) {
			if (StringUtils.isNotBlank(spuStrings[i])) {
				if (i==1) {
				  data =  spuStrings[i].split("\\n")[1];
				}else {
				  data = spuStrings[i];
			}
				spuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
				SpuDTO spu = new SpuDTO();
				Item item = new Item();
				  try {
					   item.setColor(StringUtils.isBlank(spuArr[10])?spuArr[4]:spuArr[10]);
//					   item.setColor(spuArr[10]);
					   item.setSupplierPrice(spuArr[16]);
					   item.setDescription(spuArr[15]);
					   item.setSpuId(spuArr[0]);
					   
					   item.setStyleCode(spuArr[3]);
					   item.setColorCode(spuArr[4]);
					   
					   spuMap.put(spuArr[0], item);

					   spu.setId(UUIDGenerator.getUUID());
		               spu.setSupplierId(supplierId);
		               spu.setSpuId(spuArr[0]);
		               spu.setBrandName(spuArr[2]);
		               spu.setCategoryName(spuArr[8]);
		               //spu.setSpuName(fields[0]);
		               spu.setSeasonId(spuArr[1]);
		               
		               StringBuffer material = new StringBuffer() ;
		               if (StringUtils.isNotBlank(spuArr[11])) {
		            	   material.append(spuArr[11]).append(";");
		               }else if(StringUtils.isNotBlank(spuArr[15])){
		            	   material.append(spuArr[15]).append(";");
		               }else if (StringUtils.isNotBlank(spuArr[42])) {
		            	   material.append(spuArr[42]);
		               }
		               spu.setMaterial(material.toString());
		               
		               spu.setCategoryGender(spuArr[5]);
		               spu.setProductOrigin(spuArr[40]);
		               productFetchService.saveSPU(spu);
		           } catch (ServiceException e) {
		        	   try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
		           }
			}
		}
		System.out.println("save sku data");
		//处理sku信息
		//处理图片信息
		String[] imageStrings = imageData.split("\\r\\n");
		String[] imageArr = null;
		for (int j = 2; j < imageStrings.length; j++) {
			if (StringUtils.isNotBlank(imageStrings[j])) {
				data = imageStrings[j];
				imageArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
				if (!imgMap.containsKey(imageArr[0])) {
					imgMap.put(imageArr[0], imageArr[1]);
				}else {
					imgMap.put(imageArr[0],imgMap.get(imageArr[0])+","+imageArr[1]);
				}
			}
		}
		String size="";
		String[] skuStrings = skuData.split("\\r\\n");
		String[] skuArr = null;
		for (int i = 1; i < skuStrings.length; i++) {
			if (StringUtils.isNotBlank(skuStrings[i])) {
				if (i==1) {
				  data =  skuStrings[i].split("\\n")[1];
				}else {
				  data = skuStrings[i];
				}
				skuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
				if (spuMap.containsKey(skuArr[0])) {
					Item item = spuMap.get(skuArr[0]);
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
        			sku.setSupplierId(supplierId);
        			sku.setSpuId(skuArr[0]);
        			//sku.setSkuId(skuId);
        			size = skuArr[1];
        			if(size.indexOf("½")>0){
        				size=size.replace("½","+");
        			}
        			sku.setProductSize(size);
        			if (StringUtils.isBlank(priceMap.get(item.getSpuId()))) {
        				logger.info(item.getSpuId()+"++++++++++++++++++++++++++++++++++"+"没有价格");
						System.err.println(item.getSpuId()+"++++++++++++++++++++++++++++++++++"+"没有价格");
						continue;
					}
        			sku.setMarketPrice(priceMap.get(item.getSpuId()).replace(",", ""));
        			sku.setColor(item.getColor());
        			sku.setProductDescription(item.getDescription());
        			sku.setSaleCurrency("EURO");
        			String stock = skuArr[2];
        			String barCode = skuArr[5];
        			sku.setStock(stock);
        			//skuid+barcode
        			sku.setSkuId(skuArr[0]+"-"+barCode);
        			sku.setBarcode(barCode);
        			sku.setProductCode(item.getStyleCode()+"-"+item.getColorCode());
        			try {
        				
//        				if(skuDTOMap.containsKey(sku.getSkuId())){
//    						skuDTOMap.remove(sku.getSkuId());
//    					}
        				
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
	        				e1.printStackTrace();
	        			}
					}
        			//处理图片
					String imgUrls = imgMap.get(skuArr[0]);
					if (StringUtils.isNotBlank(imgUrls)) {
						String[] imgUrlArr = imgUrls.split(",");
						productFetchService.savePicture(supplierId, null, sku.getSkuId(), Arrays.asList(imgUrlArr));
					}
				}
			}
		}
		System.out.println("update old data");
		//更新网站不再给信息的老数据
//		for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
//			 Map.Entry<String,SkuDTO> entry =  itor.next();
//			if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
//				entry.getValue().setStock("0");
//				try {
//					productFetchService.updatePriceAndStock(entry.getValue());
//				} catch (ServiceException e) {
//					e.printStackTrace();
//				}
//			}
//		}
        logger.info("save product into DB success");
    }

    public static void main(String[] args){
        new FetchProduct().fetchProductAndSave();
    }
}
