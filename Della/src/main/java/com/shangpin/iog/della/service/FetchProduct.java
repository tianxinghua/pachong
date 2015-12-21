package com.shangpin.iog.della.service;

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
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("della")
public class FetchProduct {
	
	private static Logger logError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String filePath = "";
	private static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		filePath = bdl.getString("filepath");
		day = Integer.valueOf(bdl.getString("day"));
	}

	@Autowired
	public ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	
	public void fetchSave(){
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
			
			List<Item> items = CSVUtil.readLocalCSV(filePath, Item.class, ";");
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
				try {
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
				if(StringUtils.isNotBlank(item.getPhoto_links())){
					String[] photos =  item.getPhoto_links().replaceAll("\"", "").split("\\|");
					productFetchService.savePicture(supplierId, null, sku.getSkuId(), Arrays.asList(photos));
					
				}
				
				
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
	            spu.setMaterial(item.getItem_detailed_info().replaceAll("\"", ""));//材质
	            spu.setProductOrigin(item.getMade_in().replaceAll("\"", ""));
	            spu.setSeasonId(item.getSeason().replaceAll("\"", ""));
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
			logError.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
