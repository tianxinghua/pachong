package com.shangpin.iog.della.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import com.shangpin.iog.della.dto.Item;
import com.shangpin.iog.della.utils.CSVUtil;
import com.shangpin.iog.della.utils.FTPUtils;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

@Component("della")
public class FetchProduct extends AbsSaveProduct{
	
	private static Logger logError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String filePath = "";
	private static int day;
	private static String remoteFileName = null;
	private static String local = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		filePath = bdl.getString("filepath");
		day = Integer.valueOf(bdl.getString("day"));
		remoteFileName = bdl.getString("remoteFileName");
		local = bdl.getString("local");
	}

	@Autowired
	public ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		try {
			
//			Date startDate,endDate= new Date();
//			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
//			//获取原有的SKU 仅仅包含价格和库存
//			Map<String,SkuDTO> skuDTOMap = new HashedMap();
//			try {
//				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
//			} catch (ServiceException e) {
//				e.printStackTrace();
//			}
			
			List<Item> items = null;
			
			try{
				
				FTPUtils ftp =new FTPUtils("mosuftp", "inter2015£", "92.223.134.2", 21);
				ftp.downFile("MOSU", remoteFileName, local);
				File file = new File(local+File.separator+remoteFileName);
				items = CSVUtil.readCSV(file, Item.class, ';');
				ftp.logout();
			}catch(Exception ex){
				logError.error("第一次下载==="+ex.getMessage());
				try{
					System.out.println("========第一次下载失败，再试一次==========="); 
					FTPUtils ftp =new FTPUtils("mosuftp", "inter2015£", "92.223.134.2", 21);
					ftp.downFile("MOSU", remoteFileName, local);
					File file = new File(local+File.separator+remoteFileName);
					items = CSVUtil.readCSV(file, Item.class, ';');
					ftp.logout();
				}catch(Exception e){
					logError.error("第2次下载==="+e.getMessage());
					
				}
			}
				
			
			
			Map<String,Item> spuItems = new HashMap<String,Item>();
			for(Item item:items){
				
				//保存sku
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSkuId(item.getItem_code());
				sku.setSpuId(item.getSupplier_item_code().replaceAll("\"", ""));
				sku.setProductName((item.getBrand_name()+" "+item.getBrand_line()).replaceAll("\"", ""));//item.getBrand_name()+"品类"
				sku.setMarketPrice(item.getRetail_price().replaceAll(",", "."));
				sku.setSupplierPrice(item.getYour_price().replaceAll(",", "."));
				sku.setSalePrice(item.getSold_price().replaceAll(",", "."));
				sku.setProductCode(item.getSupplier_item_code().replaceAll("\"", ""));
				sku.setColor(item.getColor_description().replaceAll("\"", ""));
				sku.setProductDescription(item.getItem_detailed_info().replaceAll(",", ".").replaceAll("\"", ""));
				String size = item.getSize();
				if(size.contains("1/2")){
					size = size.replace("1/2", ".5");
				}
				size = size.replaceAll("\"", "").replaceAll(",", ".");
				sku.setProductSize(size);
				sku.setStock(item.getQuantity());
				
				skuList.add(sku);
				
//				try {
//					if(skuDTOMap.containsKey(sku.getSkuId())){
//						skuDTOMap.remove(sku.getSkuId());
//					}
//					productFetchService.saveSKU(sku);					
//					
//				} catch (ServiceException e) {
//					if (e.getMessage().equals("数据插入失败键重复")) {
//						// 更新价格和库存
//						try {
//							productFetchService.updatePriceAndStock(sku);
//						} catch (ServiceException e1) {
//							e1.printStackTrace();
//						}
//						e.printStackTrace();
//					}
//				}
				
				//保存图片
//				if(StringUtils.isNotBlank(item.getPhoto_links())){
//					String[] photos =  item.getPhoto_links().replaceAll("\"", "").split("\\|");
//					productFetchService.savePicture(supplierId, null, sku.getSkuId(), Arrays.asList(photos));
//					
//				}
				List<String> listPics = new ArrayList<String>();				
				if(StringUtils.isNotBlank(item.getLINK_PHOTO_1())){
					listPics.add(item.getLINK_PHOTO_1());
				}
				if(StringUtils.isNotBlank(item.getLINK_PHOTO_2())){
					listPics.add(item.getLINK_PHOTO_2());
				}
				if(StringUtils.isNotBlank(item.getLINK_PHOTO_3())){
					listPics.add(item.getLINK_PHOTO_3());
				}
				if(StringUtils.isNotBlank(item.getLINK_PHOTO_4())){
					listPics.add(item.getLINK_PHOTO_4());
				}
				if(StringUtils.isNotBlank(item.getLINK_PHOTO_5())){
					listPics.add(item.getLINK_PHOTO_5());
				}
				if(StringUtils.isNotBlank(item.getLINK_PHOTO_6())){
					listPics.add(item.getLINK_PHOTO_6());
				}
				if(StringUtils.isNotBlank(item.getLINK_PHOTO_7())){
					listPics.add(item.getLINK_PHOTO_7());
				}
				if(StringUtils.isNotBlank(item.getLINK_PHOTO_8())){
					listPics.add(item.getLINK_PHOTO_8());
				}
				if(StringUtils.isNotBlank(item.getLINK_PHOTO_9())){
					listPics.add(item.getLINK_PHOTO_9());
				}
				if(StringUtils.isNotBlank(item.getLINK_PHOTO_10())){
					listPics.add(item.getLINK_PHOTO_10());
				}
				
				imageMap.put(sku.getSkuId()+";"+sku.getProductCode(), listPics);
//				productFetchService.savePicture(supplierId, null, sku.getSkuId(), listPics);
				
				
				if(!spuItems.containsKey(item.getSupplier_item_code())){
					spuItems.put(item.getSupplier_item_code(),item);
				}
			}
			
			//保存spu
			Iterator iterator = spuItems.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry entry = (Map.Entry)iterator.next();
				Item item = (Item)entry.getValue();
				SpuDTO spu = new SpuDTO();
	            spu.setId(UUIDGenerator.getUUID());
	            spu.setSpuId(item.getSupplier_item_code().replaceAll("\"", ""));
	            spu.setSupplierId(supplierId);
	            spu.setBrandName(item.getBrand_name().replaceAll("\"", ""));
	            spu.setCategoryGender(item.getGender().replaceAll("\"", ""));
	            spu.setCategoryName(item.getBrand_line().replaceAll("\"", ""));//品类
	            String material = item.getItem_detailed_info();
	            try{
	            	material = material.substring(material.lastIndexOf(":")+1);
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
	            spu.setMaterial(material);//材质
	            spu.setProductOrigin(item.getMade_in().replaceAll("\"", ""));
	            spu.setSeasonId(item.getSeason().replaceAll("\"", ""));
	            
	            spuList.add(spu);
	            
//	            try {
//	                productFetchService.saveSPU(spu);
//	            } catch (ServiceException e) {
//	            	logError.error(e.getMessage());
//	            	try{
//	            		productFetchService.updateMaterial(spu);
//	            	}catch(ServiceException ex){
//	            		logError.error(ex.getMessage());
//	            		ex.printStackTrace();
//	            	}
//	                e.printStackTrace();
//	            }
			}
			
//			//更新网站不再给信息的老数据
//			for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
//				 Map.Entry<String,SkuDTO> entry =  itor.next();
//				if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
//					entry.getValue().setStock("0");
//					try {
//						productFetchService.updatePriceAndStock(entry.getValue());
//					} catch (ServiceException e) {
//						e.printStackTrace();
//					}
//				}
//			}		
			
		} catch (Exception e) {
			logError.error(e.getMessage());
			e.printStackTrace();
		}
		
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
	
	
	
}
