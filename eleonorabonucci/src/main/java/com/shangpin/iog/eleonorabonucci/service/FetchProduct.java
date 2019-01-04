package com.shangpin.iog.eleonorabonucci.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.eleonorabonucci.dto.Item;
import com.shangpin.iog.eleonorabonucci.dto.Items;
import com.shangpin.iog.eleonorabonucci.dto.Product;
import com.shangpin.iog.eleonorabonucci.dto.Products;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by loyalty on 15/6/8.
 */
@Component("eleonorabonucci")
public class FetchProduct extends AbsSaveProduct {
	final Logger logger = Logger.getLogger("info");
	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;

	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static int day;
	private static String url = null;
	private static Gson gson = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
		gson = new Gson();
	}

	public void sendAndSaveProduct() {
		handleData("spu", supplierId, day, null);
	}

	@Override
	public Map<String, Object> fetchProductAndSave() {
		String result = HttpUtil45.get(url, new OutTimeConfig(1000 * 60 * 30,
				1000 * 60 * 30, 1000 * 60 * 30), null);
		HttpUtil45.closePool();
        logger.info("the resul calling api :"+result);
		if (result != null && !"".equals(result)) {

			result = result.replace("\uFEFF", "");
		}
		Products products = null;
		List<Product> productList = null;
		try {
			products = ObjectXMLUtil.xml2Obj(Products.class, result);
			if (products != null && products.getProducts() != null) {
				productList = products.getProducts();
				for (Product pro : productList) {
					supp.setData(gson.toJson(pro));
					pushMessage(null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag) {
			return fetchProductAndSave(productList);
		}
		return null;
	}

	public Map<String, Object> fetchProductAndSave(List<Product> productList) {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();
		String skuId = "";
		String size = "";
		for (Product product : productList) {
			SpuDTO spu = new SpuDTO();
			Items items = product.getItems();
			if (null == items) {// 无SKU
				continue;
			}
			List<Item> itemList = items.getItems();
			if (null == itemList)
				continue;

			for (Item item : itemList) {

				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSpuId(product.getProductId());
				skuId = item.getItem_id();
				if (skuId.indexOf("½") > 0) {
					skuId = skuId.replace("½", "+");
				}
				sku.setSkuId(skuId);
				size = item.getItem_size();
				if (StringUtils.isNotBlank(size)) {
					if (size.indexOf("½") > 0) {
						size = size.replace("½", ".5");
					}
				}
				sku.setProductSize(size);
				if (item.getMarket_price() != null) {
					sku.setMarketPrice(item.getMarket_price().replaceAll(",",
							"."));
				}

				if (item.getSell_price() != null) {
					sku.setSalePrice(item.getSell_price().replaceAll(",", "."));
				}

				if (item.getSupply_price() != null) {
					sku.setSupplierPrice(item.getSupply_price().replaceAll(",",
							"."));
				}
				sku.setColor(item.getColor());
				sku.setProductDescription(item.getDescription());
				sku.setStock(item.getStock());
				sku.setProductCode(product.getProducer_id());
				sku.setProductDescription(product.getDescription()
						.replaceAll(",", " ").replaceAll("\\r", "")
						.replaceAll("\\n", "").trim());
				skuList.add(sku);

				if (StringUtils.isNotBlank(item.getPicture())) {
					String[] picArray = item.getPicture().split("\\|");

					List<String> picUrlList = Arrays.asList(picArray);
					productFetchService.savePicture(supplierId, null, skuId,
							picUrlList);
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
					spu.setProductOrigin(product.getProduct_MADEin());

					spu.setCategoryGender(product.getGender());
					productFetchService.saveSPU(spu);
				} catch (ServiceException e) {
					try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
				}
			}

			returnMap.put("sku", skuList);
			returnMap.put("spu", spuList);
			returnMap.put("image", imageMap);
		}
		return returnMap;
	}
}
