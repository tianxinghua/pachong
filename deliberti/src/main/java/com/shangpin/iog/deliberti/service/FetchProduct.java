package com.shangpin.iog.deliberti.service;

import java.util.ArrayList;
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
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.deliberti.util.delibertiUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("deliberti")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	public void fetchProductAndSave() {
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		delibertiUtil kixUtil = new delibertiUtil();
		List<Product> allProducts = delibertiUtil.getAllProducts();
		Map<String, String> metaField = null;
		
		for (Product product : allProducts) {
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSupplierId(supplierId);
			spu.setSpuId("");
			spu.setBrandName("");
			spu.setCategoryGender("");
			spu.setCategoryName("");
			spu.setMaterial("");
			spu.setSeasonName("");
			spu.setProductOrigin("");
			
			//============================保存spu===================================
			 try {
				productFetchService.saveSPU(spu);
			} catch (ServiceException e) {
			   try {
					productFetchService.updateMaterial(spu);
				} catch (ServiceException e1) {
					e1.printStackTrace();
				}
			}
		}
		
			//===============================保存sku=================================
			/*
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSpuId(spu.getSpuId());
				sku.setSkuId("");
				sku.setProductCode("");
				sku.setColor("");
				sku.setSalePrice("");
				sku.setProductName("");
				sku.setProductSize("");
				sku.setStock("");
				sku.setSaleCurrency("");
				
				if(skuDTOMap.containsKey(sku.getSkuId())){
					skuDTOMap.remove(sku.getSkuId());
				}
				try {
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
			}
			*/
			//============================保存图片=====================================
			//productFetchService.savePicture(supplierId, spu.getSpuId(), null, imageList);
		
		
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
		
		logger.info("抓取结束");
	}
}

