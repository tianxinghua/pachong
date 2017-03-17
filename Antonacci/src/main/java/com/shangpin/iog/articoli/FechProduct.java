package com.shangpin.iog.articoli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.articoli.dto.Product;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

@Component("antonacci")
public class FechProduct extends AbsSaveProduct {

	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String uri;
	private static Gson gson = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		uri = bdl.getString("uri");
		gson = new Gson();
	}

	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;

	public static void main(String[] args) throws Exception {
	}

	public void sendAndSaveProduct() throws ServiceException {
		handleData("spu", supplierId, day, null);
	}

	@Override
	public Map<String, Object> fetchProductAndSave() {

		Map<String, Object> returnMap = null;
		String json = HttpUtil45.get(uri, new OutTimeConfig(1000 * 60 * 20,
				1000 * 60 * 20, 1000 * 60 * 20), null);
		List<Product> productList = gson.fromJson(json,
				new TypeToken<List<Product>>() {
				}.getType());
		if (productList != null && productList.size() > 0) {
			returnMap = fechAndSave(productList);
		}
		
		return returnMap;
	}

	public Map<String, Object> fechAndSave(List<Product> products) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();
		if (products != null && products.size() > 0) {
			for (Product product : products) {
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSkuId(product.getSkuID());
				sku.setSpuId(product.getSpuID());
				sku.setBarcode(product.getSkuID());
				sku.setProductName(product.getProductName());
				sku.setMarketPrice(product.getMarketPrice());
				sku.setSalePrice(product.getSalePrice());
				sku.setProductCode(product.getProductCode());
				sku.setColor(product.getColor());
				sku.setProductDescription(product.getProductDescription());
				sku.setProductSize(product.getSize().replace("½", "."));
				sku.setStock(product.getStock());
				sku.setSaleCurrency(product.getSaleCurrency());
				skuList.add(sku);

				// spu 入库
				SpuDTO spu = new SpuDTO();
				spu.setId(UUIDGenerator.getUUID());
				spu.setSpuId(product.getSpuID());
				spu.setSupplierId(supplierId);
				spu.setCategoryGender(product.getGender());
				spu.setCategoryName(product.getCategoryName());
				spu.setSeasonId(product.getSeasonName());
				spu.setBrandName(product.getBrandName());
				spu.setMaterial(product.getMaterial());
				String origin = "";
				if (product.getProductDescription() != null) {
					String[] descArray = product.getProductDescription().split(
							"-", -1);
					for (String desc : descArray) {
						if (desc.contains("made in")) {
							origin = desc.replace("made in", "").trim();
							break;
						}
					}
				}
				spu.setProductOrigin(origin);
				spuList.add(spu);

				List<String> list = new ArrayList<String>();
				if (product.getImages() != null) {
					String[] picUrl = product.getImages().split("\\|", -1);
					for (String pic : picUrl) {
						list.add(pic);
					}
					imageMap.put(product.getSpuID(), list);
				}
			}
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
}
