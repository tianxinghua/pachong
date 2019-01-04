package com.shangpin.iog.catalogue.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.catalogue.excel.Item;
import com.shangpin.iog.catalogue.excel.ReadExcel;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("FechProduct")
public class FechProduct {

	private static Logger logInfo  = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String path;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));	
		path = bdl.getString("path");
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	
	public void fechAndSave(){
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
			
			List<Item> list = new ReadExcel().readExcel(path);
			if(list.size()>0){
				for(Item item:list){
					try{
						
						if(!item.getQuatity().equals("0") && !item.getQuatity().equals("0.0")){
							String si = item.getSize();
							String size = si.substring(si.indexOf(":")+1, si.length());
							SkuDTO sku = new SkuDTO();
							sku.setSkuId(item.getId()+"-"+size);
							sku.setSpuId(item.getId());
							sku.setId(UUIDGenerator.getUUID());
							sku.setSupplierId(supplierId);
							sku.setMarketPrice(item.getPrix_TTC());
							sku.setSupplierPrice(item.getPrix_HT());
							sku.setProductCode(item.getId());
							//颜色
							sku.setProductDescription(item.getDescription2());
							sku.setProductSize(size);
							sku.setStock(item.getQuatity());
							
							String pics = item.getImageURL();
							List<String> picList = Arrays.asList(pics.split(","));
							productFetchService.savePicture(supplierId, null, sku.getSkuId(), picList);
							
							
							SpuDTO spu = new SpuDTO();
							spu.setId(UUIDGenerator.getUUID());
				            spu.setSpuId(item.getId());
				            spu.setSupplierId(supplierId);
				            String categoryGender = item.getCategory();
				            if(categoryGender.contains("Men,Women")){
				            	spu.setCategoryGender("Men,Women");
				            }else if(categoryGender.contains("Men")){
				            	spu.setCategoryGender("Men");
				            }else if(categoryGender.contains("Women")){
				            	spu.setCategoryGender("Women");
				            }
				            spu.setCategoryName(categoryGender.substring(0, categoryGender.indexOf(",")));
				            String brand = item.getTitre();
				            brand = brand.substring(0,brand.indexOf("-"));
				            spu.setBrandName(brand);
				            //材质
				            //产地
				          
				          //sku入库
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
				            //spu 入库
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
						
					}catch(Exception ex){
						logError.error(ex);
						ex.printStackTrace();
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
			
		}catch(Exception ex){
			logError.error(ex);
			ex.printStackTrace();
		}
		
	}
}
