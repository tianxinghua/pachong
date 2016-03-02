package com.shangpin.iog.papini.service;

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
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.papini.dto.Item;
import com.shangpin.iog.papini.dto.Items;
import com.shangpin.iog.papini.dto.Product;
import com.shangpin.iog.papini.util.ZipUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("papini")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
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
		ZipUtil zipUtil = new ZipUtil();
		List<Product> allProducts = zipUtil.getAllProduct();
		
		String origin = "";
		for (Product product : allProducts) {
			Items items = product.getItems();
			if (null == items) {// 无SKU
				continue;
			}
			List<Item> itemList = items.getItems();
			if (null == itemList){
				continue;
			}
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSupplierId(supplierId);
			spu.setSpuId(product.getProductId());
			spu.setBrandName(product.getProduct_brand());
			spu.setCategoryGender(product.getMain_category());
			spu.setCategoryName(product.getCategory());
			spu.setMaterial(product.getProduct_material());
			spu.setSeasonName(product.getSeason_code());
			if (StringUtils.isNotBlank(product.getMade_in())) {
				origin = product.getMade_in();
			}
			spu.setProductOrigin(origin);
			
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
			String skuId = "";
			String size = "";
			for (Item item : itemList) {
				SkuDTO sku = new SkuDTO();
				Integer stock = Integer.parseInt(item.getStock());
				if (stock == 0) {
					continue;
				}
				
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				
				sku.setSpuId(spu.getSpuId());
				skuId = item.getItem_id();
				if (skuId.indexOf("½") > 0) {
					skuId = skuId.replace("½", "+");
				}
				sku.setSkuId(skuId);
				sku.setProductCode(product.getProductId());
				sku.setColor(item.getColor());
				sku.setProductDescription(item.getDescription());
				
				sku.setMarketPrice(item.getMarket_price());
				sku.setSalePrice(item.getSell_price());
				
				size = item.getItem_size();
				if (size.indexOf("½") > 0) {
					size = size.replace("½", "+");
				}
				sku.setProductSize(size);
				
				sku.setStock(item.getStock());
				sku.setSaleCurrency("EURO");
				
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
				
				//============================保存图片=====================================
				if (StringUtils.isNotBlank(item.getPicture())) {
					String[] picArray = item.getPicture().split("\\|");
					productFetchService.savePicture(supplierId, null, item.getItem_id(), Arrays.asList(picArray));
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
		
		logger.info("抓取结束");
	}
}

