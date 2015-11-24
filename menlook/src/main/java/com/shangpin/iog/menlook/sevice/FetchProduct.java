package com.shangpin.iog.menlook.sevice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.menlook.dto.Item;
import com.shangpin.iog.menlook.util.DownloadAndReadCSV;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.SkuPriceService;

@Component("menlooks")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static Logger logMongo = Logger.getLogger("mongodb");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private SkuPriceService skuPriceService;
	public void fetchProductAndSave() {
//		Map<String, String> mongMap = new HashMap<>();
//		OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);
//		mongMap.put("supplierId", supplierId);
//		mongMap.put("supplierName", "menlook");
//		logMongo.info(mongMap);
		logger.info("开始抓取");
		Map<String,Item> spuMap = new HashMap<String, Item>();
		List<Item> list = null;
		try {
			list = DownloadAndReadCSV.readLocalCSV();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Item item : list) {
			if (!item.getStock().equals("^")) {
				
				if (!spuMap.containsKey(item.getSpuId())) {
					spuMap.put(item.getSpuId(), item);
				}
				SkuDTO skuDTO = new SkuDTO();
				skuDTO.setId(UUIDGenerator.getUUID());
				skuDTO.setSkuId(item.getSkuId().replace("^", ""));
				skuDTO.setSupplierId(supplierId);
				skuDTO.setSpuId(item.getSpuId().replace("^", ""));
				skuDTO.setProductCode(item.getProductCode().replace("^", ""));
				skuDTO.setColor(item.getColor().replace("^", ""));
				skuDTO.setSaleCurrency(item.getCurrency());
				skuDTO.setProductSize(item.getSize().replace("^", ""));
				skuDTO.setStock(item.getStock().replace("^", ""));
				skuDTO.setMarketPrice(item.getMarketPrice().replace("^", ""));
				skuDTO.setSupplierPrice(item.getSupplierPrice().replace("^", ""));
				try {
					productFetchService.saveSKU(skuDTO);
				} catch (ServiceException e) {
					if (e.getMessage().equals("数据插入失败键重复")) {
						try {
							productFetchService.updatePriceAndStock(skuDTO);
						} catch (ServiceException e1) {
							loggerError.info("库存更新失败");
							e1.printStackTrace();
						}
					}
				}
				ProductPictureDTO picture = new ProductPictureDTO();
				picture.setSupplierId(supplierId);
				picture.setId(UUIDGenerator.getUUID());
				picture.setSkuId(item.getSkuId().replace("^", ""));
				picture.setPicUrl(item.getImg().replace("^", ""));
				try {
					productFetchService.savePictureForMongo(picture);
				} catch (ServiceException e) {
					loggerError.info("图片存储失败");
					e.printStackTrace();
				}
			}
		}
		for(Entry<String, Item> entry:spuMap.entrySet()){
			SpuDTO spuDTO = new SpuDTO();
			spuDTO.setId(UUIDGenerator.getUUID());
			spuDTO.setSpuId(entry.getKey().replace("^", ""));
			spuDTO.setSupplierId(supplierId);
			spuDTO.setBrandName(entry.getValue().getBrand().replace("^", ""));
			spuDTO.setCategoryName(entry.getValue().getCategory().replace("^", ""));
			spuDTO.setCategoryGender("men");
			String martrial = entry.getValue().getMaterial().replace("^", "");
			if (StringUtils.isNotBlank(martrial)) {
				spuDTO.setMaterial(martrial);
			}else {
				spuDTO.setMaterial(entry.getValue().getDescription().replace("^", ""));
			}
			try {
				productFetchService.saveSPU(spuDTO);
			} catch (ServiceException e) {
				loggerError.info(entry.getKey()+"spu保存失败");
				e.printStackTrace();
			}
		}
	}
}
