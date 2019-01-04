package com.shangpin.iog.stefaniamode.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.stefaniamode.dto.Item;
import com.shangpin.iog.stefaniamode.dto.Items;
import com.shangpin.iog.stefaniamode.dto.Product;
import com.shangpin.iog.stefaniamode.dto.Products;
import com.shangpin.iog.stefaniamode.util.FtpUtil;
import com.shangpin.product.AbsSaveProduct;

@Component("framestefaniamode")
public class StefanFrameFetchProduct extends AbsSaveProduct {
	private static ApplicationContext factory;

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	final Logger logger = Logger.getLogger("info");

	private static ResourceBundle bdl = null;

	public static String supplierId;

	public static String url;
	public static String picpath;
	public static int day;
	public static Gson gson = null;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
		picpath = bdl.getString("picpath");
		gson = new Gson();
	}

	public void sendAndSaveProduct() {
		handleData("spu", supplierId, day, null);
	}

	public Map<String, Object> fetchProductAndSave() {

		Map<String, Object> returnMap = null;
		
		Products products = null;
		String xml = null;
//		xml = HttpUtil45.get(url, new OutTimeConfig(1000 * 60 * 60,
//				1000 * 60 * 60, 1000 * 60 * 60), null);
		List<Product> productList = null;
		ByteArrayInputStream is = null;
		System.out.println(url);

		try {
//			is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
//			products = ObjectXMLUtil.xml2Obj(Products.class, is);
			FtpUtil ftpUtil=new FtpUtil();
			products = ftpUtil.downloadFTP();

			if (null!=products&&products.getProducts() != null
					&& products.getProducts().size() > 0) {

				productList = products.getProducts();
				for (Product product : productList) {
					product.setCategory(product.getDescription());
					supp.setData(gson.toJson(product));
					pushMessage(gson.toJson(supp));
				}
				// true 保存数据
				if (flag) {
					returnMap = saveProduct(productList);
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return returnMap;

	}

	private Map<String, Object> saveProduct(List<Product> productList) {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();

		String origin = "";
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
				sku.setSupplierPrice("");
				sku.setColor(item.getColor());
				sku.setProductDescription(item.getDescription());
				sku.setStock(item.getStock());
				sku.setProductCode(product.getProductId());
				sku.setSaleCurrency("EUR");
				sku.setBarcode(item.getBarcode());
				skuList.add(sku);
			}
			spu.setId(UUIDGenerator.getUUID());
			spu.setSupplierId(supplierId);
			spu.setSpuId(product.getProductId());
			spu.setBrandName(product.getProduct_brand());
			spu.setCategoryName(product.getCategory());
			spu.setSpuName(product.getProduct_name());
			spu.setSeasonId(product.getSeason_code());
			spu.setMaterial(product.getProduct_material());
			if (StringUtils.isNotBlank(product.getMade_in())) {
				origin = product.getMade_in();
			}
			spu.setProductOrigin(origin);
			// 商品所属性别字段；
			spu.setCategoryGender(product.getMain_category());
			spuList.add(spu);
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}

	public static void main(String[] args) throws Exception {
		// 加载spring
		loadSpringContext();
		StefanFrameFetchProduct stockImp = (StefanFrameFetchProduct) factory
				.getBean("framestefaniamode");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		stockImp.fetchProductAndSave();
	}
}
