package com.shangpin.iog.stefaniamode.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.stefaniamode.dto.Item;
import com.shangpin.iog.stefaniamode.dto.Items;
import com.shangpin.iog.stefaniamode.dto.Product;
import com.shangpin.iog.stefaniamode.dto.Products;

/**
 * Created by Jerry Wang on 2015/09/23.
 */
@Component("stefaniamode")
public class FetchProduct {
	final Logger logger = Logger.getLogger(this.getClass());
	private static Logger infoLogger = Logger.getLogger("info");
	@Autowired
	ProductFetchService productFetchService;

	public void fetchProductAndSave(String xmlContent, String supplierId) {

		try {
			Map<String, String> mongMap = new HashMap<>();
			mongMap.put("supplierId", supplierId);
			mongMap.put("supplierName", "stefaniamode");
			mongMap.put("result", xmlContent);
			infoLogger.info(mongMap);
			Products products = ObjectXMLUtil.xml2Obj(Products.class,
					xmlContent);
			List<Product> productList = products.getProducts();
			for (Product product : productList) {
				SpuDTO spu = new SpuDTO();

				Items items = product.getItems();
				if (null == items) {// 无SKU
					continue;
				}

				List<Item> itemList = items.getItems();
				if (null == itemList)
					continue;
				String skuId = "";
				for (Item item : itemList) {
					SkuDTO sku = new SkuDTO();
					try {
						Integer stock = Integer.parseInt(item.getStock());
						if (stock == 0) {
							continue;
						}
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);

						sku.setSpuId(product.getProductId());
						skuId = item.getItem_id();
						if (skuId.indexOf("½") > 0) {
							skuId = skuId.replace("½", "+");
						}
						sku.setSkuId(skuId);
						sku.setProductSize(item.getItem_size());
						sku.setMarketPrice(item.getMarket_price());
						sku.setSalePrice(item.getSell_price());
						// sku.setSupplierPrice(item.getSupply_price());
						sku.setColor(item.getColor());
						sku.setProductDescription(item.getDescription());
						sku.setStock(item.getStock());
						sku.setProductCode(product.getProducer_id());
						sku.setSaleCurrency("EUR");
						productFetchService.saveSKU(sku);

						if (StringUtils.isNotBlank(item.getPicture())) {
							String[] picArray = item.getPicture().split("\\|");

							for (String picUrl : picArray) {
								ProductPictureDTO dto = new ProductPictureDTO();
								dto.setPicUrl(picUrl);
								dto.setSupplierId(supplierId);
								dto.setId(UUIDGenerator.getUUID());
								dto.setSkuId(item.getItem_id());
								try {
									productFetchService
											.savePictureForMongo(dto);
								} catch (ServiceException e) {
									e.printStackTrace();
								}
							}
						}

					} catch (ServiceException e) {
						try {
							if (e.getMessage().equals("数据插入失败键重复")) {
								// 更新价格和库存
								productFetchService.updatePriceAndStock(sku);
							} else {
								e.printStackTrace();
							}

						} catch (ServiceException e1) {
							e1.printStackTrace();
						}
					}
				}

				try {
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(product.getProductId());
					spu.setBrandName(product.getProduct_brand());
					spu.setCategoryName(product.getCategory());
					spu.setSpuName(product.getProduct_name());
					spu.setSeasonId(product.getSeason_code());
					spu.setMaterial(product.getProduct_material());
					productFetchService.saveSPU(spu);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
