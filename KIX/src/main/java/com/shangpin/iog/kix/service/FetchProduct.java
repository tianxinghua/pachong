package com.shangpin.iog.kix.service;

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
import com.shangpin.iog.kix.dto.Image;
import com.shangpin.iog.kix.dto.Product;
import com.shangpin.iog.kix.dto.Variant;
import com.shangpin.iog.kix.util.KIXUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("kix")
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
		KIXUtil kixUtil = new KIXUtil();
		List<Product> allProducts = kixUtil.getAllProducts();
		Map<String, String> metaField = null;
		
		for (Product product : allProducts) {
			//sava spu 
			metaField = kixUtil.getMetaField(product.getId());
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSupplierId(supplierId);
//			spu.setSpuId(metaField.get("item_number"));
			spu.setSpuId(product.getId());
			spu.setBrandName(product.getVendor());
			
			spu.setCategoryGender(metaField.get("sex"));
			
			spu.setCategoryName(product.getProduct_type());
			spu.setMaterial(metaField.get("material"));
			spu.setSeasonName(metaField.get("sex"));
			spu.setProductOrigin(metaField.get("made_in"));
			
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
			//===============================保存sku=================================
			Variant[] variants = product.getVariants();
			for (Variant variant : variants) {
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSpuId(spu.getSpuId());
				sku.setSkuId(variant.getId());
				sku.setProductCode(variant.getSku());
				sku.setColor(metaField.get("color_desc"));
				sku.setSalePrice(variant.getPrice());
				sku.setProductName(product.getTitle());
				sku.setProductSize(variant.getTitle());
				sku.setStock(variant.getInventory_quantity());
				sku.setSaleCurrency("HDK");
				
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
			//============================保存图片=====================================
			Image[] images = product.getImages();
			List<String> imageList = new ArrayList<String>();
			for (Image image : images) {
				imageList.add(image.getSrc());
			}
			productFetchService.savePicture(supplierId, spu.getSpuId(), null, imageList);
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
		
		logger.info("抓取结束");
	}
}

