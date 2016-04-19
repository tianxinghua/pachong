package com.shangpin.iog.dressOn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dressOn.dto.Product;
import com.shangpin.iog.dressOn.utils.CsvUtil;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("dressOn")
public class FetchProduct {
	
	private static Logger logError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String filePath = "";
	private static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		filePath = bdl.getString("url");
		day = Integer.valueOf(bdl.getString("day"));
	}
	@Autowired
	public ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	
	public void fechProduct(){
		
		try {
			
			Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
			Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
//			String result = HttpUtil45.get(filePath, timeConfig, null);
			List<Product> items = CsvUtil.readCSV(filePath, Product.class, ',');
			for(Product product:items){
				
				String skuId = product.getSku_No().replaceAll("\"", "");
				if(StringUtils.isNotBlank(skuId)){
					
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
					sku.setSupplierId(supplierId);
					sku.setSkuId(skuId+ "_" + product.getSize());
					sku.setSpuId(skuId);
					sku.setProductName(product.getName().replaceAll("\"", "").replaceAll(",", " "));
					sku.setMarketPrice(product.getMarket_price().replaceAll("\"", "").replaceAll(",", " "));
					sku.setProductCode(product.getProduct_No().replaceAll("\"", "").replaceAll(",", " "));
					sku.setColor(product.getColor().replaceAll("\"", "").replaceAll(",", " "));
					sku.setProductDescription(product.getDescription().replaceAll("\"", "").replaceAll("<br>", "").replaceAll("•", "").replaceAll(",", " ").trim());
					sku.setProductSize(product.getSize().replaceAll("\"", "").replaceAll(",", " "));
					sku.setStock(product.getQty().replaceAll("\"", "").replaceAll(",", " "));
					sku.setBarcode(product.getBarcode() + "_" + product.getSize());
					try {
						
						if(skuDTOMap.containsKey(sku.getSkuId())){
							skuDTOMap.remove(sku.getSkuId());
						}
						productFetchService.saveSKU(sku);					
						
					} catch (ServiceException e) {
						if (e.getMessage().equals("数据插入失败键重复")) {
							// 更新价格和库存
							try {
								productFetchService.updatePriceAndStock(sku);
							} catch (ServiceException e1) {
								e1.printStackTrace();
							}
							e.printStackTrace();
						}
					}
					
					//保存图片
					List<String> list = new ArrayList<String>();
					list.add(product.getImage1());
					list.add(product.getImage2());
					list.add(product.getImage3());
					list.add(product.getImage4());
					
					productFetchService.savePicture(supplierId, skuId, null, list);		
				
					SpuDTO spu = new SpuDTO();
		            spu.setId(UUIDGenerator.getUUID());
		            spu.setSpuId(sku.getSpuId());
		            spu.setSupplierId(supplierId);
		            spu.setCategoryGender(product.getCategory());
		            spu.setCategoryName(product.getCategory().replaceAll("\"", "").replaceAll(",", " "));
		            spu.setBrandName(product.getBrand().replaceAll("\"", "").replaceAll(",", " "));	            
		            spu.setMaterial(product.getMaterials().replaceAll("\"", "").replaceAll(",", " "));
		            spu.setProductOrigin(product.getMade_In());
		            
		            String str = product.getDescription();
		            int beginIndex=str.indexOf("%");
		    		if(beginIndex!=-1){
			        	str=str.substring(beginIndex-3);
			        	str=str.replace(" ", ",");
			        	String array []=str.split(",");
			        	List li = new ArrayList<String>();
			        	li = Arrays.asList(array);
			        	spu.setMaterial(ListToString(li));
		    		}
		            
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
			
		} catch (Exception e) {
			logError.error(e);
			e.printStackTrace();
		}
	}
	
	private static final String SEP1 = " ";  
    public static String ListToString(List<?> list) { 
        StringBuffer sb = new StringBuffer(); 
        if (list != null && list.size() > 0) { 
            for (int i = 0; i < list.size(); i++) { 
                if (list.get(i) == null || list.get(i) == "") { 
                    continue; 
                } 
                // 如果值是list类型则调用自己 
                if (list.get(i) instanceof List) { 
                    sb.append(ListToString((List<?>) list.get(i))); 
                    sb.append(SEP1); 
                } else if (list.get(i) instanceof Map) { 
                    //sb.append(MapToString((Map<?, ?>) list.get(i))); 
                    sb.append(SEP1); 
                } else { 
                    sb.append(list.get(i)); 
                    sb.append(SEP1); 
                } 
            } 
        } 
        return sb.toString(); 
    }

}
