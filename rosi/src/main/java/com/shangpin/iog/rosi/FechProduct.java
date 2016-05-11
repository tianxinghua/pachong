package com.shangpin.iog.rosi;

import java.io.File;
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
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.rosi.dto.CSVTemp;
import com.shangpin.iog.rosi.dto.Channel;
import com.shangpin.iog.rosi.dto.Item;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("rosi")
public class FechProduct {

	private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static int day;
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 5*60,
			1000 * 60 * 5, 1000 * 60 * 5);
	
	private static String uri = "";
	private static String path = "";
	
	static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        uri = bdl.getString("uri");
        day = Integer.valueOf(bdl.getString("day"));
        path = bdl.getString("path");
    }
	
	@Autowired
    ProductFetchService productFetchService;
    @Autowired
	ProductSearchService productSearchService;
	
	public void fechAndSave(){
		
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		try{
			
			if(StringUtils.isNotBlank(path)){
				File file = new File(path);
				List<CSVTemp> list = CVSUtil.readCSV(file, CSVTemp.class, ',');
				for(CSVTemp temp:list){
					try{
						
						SkuDTO sku = new SkuDTO();
						sku.setId(UUIDGenerator.getUUID());
		                sku.setSupplierId(supplierId);
		                sku.setSkuId(temp.getSupplierSkuNo());
		                sku.setSpuId(temp.getProductModel());
		                sku.setProductName(temp.getSopProductName());
		                sku.setMarketPrice(temp.getMarkerPrice());  
		                sku.setProductCode(temp.getProductModel());
		                sku.setColor(temp.getProductColor());
		                sku.setProductDescription(temp.getPcDesc());
		                sku.setSaleCurrency("EUR");
		                sku.setProductSize(temp.getProductSize());
		                sku.setStock(temp.getStock());
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
		                        e1.printStackTrace();
		                    }
		                }
		                
		              //保存SPU
		                SpuDTO spu = new SpuDTO();
		                //SPU 必填
		                spu.setId(UUIDGenerator.getUUID());
		                spu.setSpuId(temp.getProductModel());
		                spu.setSupplierId(supplierId);
		                spu.setCategoryGender(temp.getGender());
		                spu.setCategoryName(temp.getCategoryName());
		                spu.setBrandName(temp.getBrandName());		                
		                spu.setMaterial(temp.getMaterial());
		                //spu.setProductOrigin(item.get);
		                try {
		                    productFetchService.saveSPU(spu);
		                } catch (ServiceException e) {
		                	try{
			            		productFetchService.updateMaterial(spu);
			            	}catch(ServiceException ex){
			            		ex.printStackTrace();
			            	}
		                    e.printStackTrace();
		                }
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
				return;
			}	
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		try{
			
			String result = HttpUtil45.get(uri, outTimeConf, null);
			result = result.replaceAll("Discounted-price", "Discounted_price");
			result = result.replaceAll("product-code", "product_code");
			logger.info(result); 
			Channel channel = XMLUtil.gsonXml2Obj(Channel.class, result);
			if(channel != null && channel.getItem().size()>0){
				System.out.println("------------一共"+channel.getItem().size()+"条数据---------------"); 
				logger.info("------------一共"+channel.getItem().size()+"条数据---------------"); 
				for(Item item : channel.getItem()){
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
	                sku.setSupplierId(supplierId);
	                sku.setSkuId(item.getSku());
	                sku.setSpuId(item.getSupplier_SKU());
	                sku.setProductName(item.getSubtitle());
	                sku.setMarketPrice(item.getPrice().replaceAll("EUR", "").replaceAll(",", ""));  
	                sku.setProductCode(item.getProduct_code());
	                sku.setColor(item.getColor());
	                sku.setProductDescription(item.getDescription().replaceAll(",", " "));
	                sku.setSaleCurrency("EUR");
	                sku.setProductSize(item.getSize());
	                sku.setStock(item.getStock());
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
	                        e1.printStackTrace();
	                    }
	                }
	                
	              //保存图片
	                List<String> list = new ArrayList<String>();
	                if (item.getImage_link_1() != null) {	  
	                	list.add(item.getImage_link_1());	                    
	                }
	                if(StringUtils.isNotBlank(item.getImage_link_2())){
	                	list.add(item.getImage_link_2());
	                }
	                if(StringUtils.isNotBlank(item.getImage_link_3())){
	                	list.add(item.getImage_link_3());
	                }
	                if(StringUtils.isNotBlank(item.getImage_link_4())){
	                	list.add(item.getImage_link_4());
	                }
	                if(StringUtils.isNotBlank(item.getImage_link_5())){
	                	list.add(item.getImage_link_5());
	                }
	                productFetchService.savePicture(supplierId, null, sku.getSkuId(), list);
	               //
	              //保存SPU
	                SpuDTO spu = new SpuDTO();
	                //SPU 必填
	                spu.setId(UUIDGenerator.getUUID());
	                spu.setSpuId(item.getSupplier_SKU());
	                spu.setSupplierId(supplierId);
	                spu.setCategoryGender(item.getGender());
	                spu.setCategoryName(item.getCategory_name());
	                spu.setBrandName(item.getBrand_name());
	                String material = item.getComposition();
	                if(StringUtils.isBlank(material)){
	                	material = item.getMaterial();
	                }
	                spu.setMaterial(material);
	                spu.setProductOrigin(item.getOrigin());
	                spu.setSeasonName(item.getSeason()); 
	                try {
	                    productFetchService.saveSPU(spu);
	                } catch (ServiceException e) {
	                	try{
		            		productFetchService.updateMaterial(spu);
		            	}catch(ServiceException ex){
		            		ex.printStackTrace();
		            	}
	                    e.printStackTrace();
	                }
				}
			}
			
		}catch(Exception ex){
			loggerError.error(ex);
			ex.printStackTrace();
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
		
	}
	
}
