package com.shangpin.iog.rossana;

import java.util.ArrayList;
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
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.rossana.dto.Item;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("PullDown")
public class PullDown {
	
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
			String result = HttpUtil45.get(filePath, timeConfig, null);
			List<Item> items = CsvUtil.readLocalCSV(result, Item.class, ";");
			for(Item item:items){
				String material = "";
				String productOrigin = "";
				String description = item.getDescription();
				if(description.contains("%")){
					material = description.substring(description.indexOf("%")-3).trim();
					if(material.contains("<BR/>")){
						material = material.substring(0, material.indexOf("<BR/>"));
					}
					material = material.replaceAll(">", "").replaceAll("\"", "");
				}
				if(description.contains("Made in")){
					productOrigin = description.substring(description.indexOf("Made in"));
					if(productOrigin.contains("<BR/>")){
						productOrigin = productOrigin.substring(0, productOrigin.indexOf("<BR/>"));
					}
					productOrigin = productOrigin.replaceAll("\"", "");
				}else if(description.contains("made in")){
					productOrigin = description.substring(description.indexOf("made in"));
					if(productOrigin.contains("<BR/>")){
						productOrigin = productOrigin.substring(0, productOrigin.indexOf("<BR/>"));
					}
					productOrigin = productOrigin.replaceAll("\"", "");
				}
				
				String salePrice = item.getPrice().replaceAll("\"", "");
				String marketPrice = String.valueOf(new java.text.DecimalFormat("#.00").format(Double.parseDouble(salePrice)*1.2));
				String skuId = item.getSku().replaceAll("\"", "");
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSkuId(skuId);
				sku.setSpuId(skuId);
				sku.setProductName(item.getProducer().replaceAll("\"", "")+" "+item.getSubcategory().replaceAll("\"", ""));
				sku.setSalePrice(salePrice);
				sku.setMarketPrice(marketPrice);
				sku.setProductCode(skuId);
				sku.setColor(item.getColor().replaceAll("\"", ""));
				sku.setProductDescription(item.getDescription().replaceAll("\"", ""));
				sku.setProductSize(item.getSize_detail().replaceAll("\"", ""));
				sku.setStock(item.getQty_in_stock().replaceAll("\"", ""));
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
				if(StringUtils.isNotBlank(item.getBigimage0())){
					list.add(item.getBigimage0());
				}
				if(StringUtils.isNotBlank(item.getBigimage1())){
					list.add(item.getBigimage1());
				}
				if(StringUtils.isNotBlank(item.getBigimage2())){
					list.add(item.getBigimage2());
				}
				if(StringUtils.isNotBlank(item.getBigimage3())){
					list.add(item.getBigimage3());
				}
				
				productFetchService.savePicture(supplierId, null, skuId, list);		
			
				SpuDTO spu = new SpuDTO();
	            spu.setId(UUIDGenerator.getUUID());
	            spu.setSpuId(sku.getSpuId());
	            spu.setSupplierId(supplierId);
	            spu.setCategoryGender(item.getCategory().replaceAll("\"", ""));
	            spu.setCategoryName(item.getSubcategory().replaceAll("\"", ""));
	            spu.setBrandName(item.getProducer().replaceAll("\"", ""));
	            spu.setSeasonName(item.getSeason().replaceAll("\"", ""));
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

}
