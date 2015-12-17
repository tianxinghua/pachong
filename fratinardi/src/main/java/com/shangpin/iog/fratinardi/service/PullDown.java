package com.shangpin.iog.fratinardi.service;

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
import com.shangpin.iog.fratinardi.dto.Item;
import com.shangpin.iog.fratinardi.util.CsvUtil;
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
			List<Item> items = CsvUtil.readLocalCSV(result, Item.class, ",");
			for(Item item:items){
				
				String skuId = item.getSku_No().replaceAll("\"", "");
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSkuId(skuId);
				sku.setSpuId(skuId);
				sku.setProductName(item.getName().replaceAll("\"", ""));
				sku.setMarketPrice(item.getMarket_price().replaceAll("\"", ""));
				sku.setProductCode(item.getProduct_No().replaceAll("\"", ""));
				sku.setColor(item.getColor().replaceAll("\"", ""));
				sku.setProductDescription(item.getDescription().replaceAll("\"", "").replaceAll("<br>", "").replaceAll("•", "").trim());
				sku.setProductSize(item.getSize().replaceAll("\"", ""));
				sku.setStock(item.getQty().replaceAll("\"", ""));
				sku.setBarcode(item.getBarcode().replaceAll("\"", ""));
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
				if(StringUtils.isNotBlank(item.getImage1())){
					list.add(item.getImage1());
				}
				if(StringUtils.isNotBlank(item.getImage2())){
					list.add(item.getImage2());
				}
				if(StringUtils.isNotBlank(item.getImage3())){
					list.add(item.getImage3());
				}
				if(StringUtils.isNotBlank(item.getImage4())){
					list.add(item.getImage4());
				}
				
				productFetchService.savePicture(supplierId, null, skuId, list);		
			
				SpuDTO spu = new SpuDTO();
	            spu.setId(UUIDGenerator.getUUID());
	            spu.setSpuId(sku.getSpuId());
	            spu.setSupplierId(supplierId);
	            spu.setCategoryGender(item.getGender().replaceAll("\"", ""));
	            spu.setCategoryName(item.getCategory().replaceAll("\"", ""));
	            spu.setBrandName(item.getBrand().replaceAll("\"", ""));	            
	            spu.setMaterial(item.getMaterials().replaceAll("\"", ""));
//	            spu.setProductOrigin();
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
