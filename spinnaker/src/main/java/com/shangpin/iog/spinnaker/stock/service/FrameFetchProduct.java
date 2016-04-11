package com.shangpin.iog.spinnaker.stock.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.spinnaker.stock.dto.Price;
import com.shangpin.iog.spinnaker.stock.dto.Products;
import com.shangpin.iog.spinnaker.stock.dto.Seasoncode;
import com.shangpin.iog.spinnaker.stock.dto.SeasoncodeList;
import com.shangpin.iog.spinnaker.stock.dto.Sku;
import com.shangpin.iog.spinnaker.stock.dto.Spu;
import com.shangpin.product.AbsSaveProduct;

@Component("frameSpinnaker")
public class FrameFetchProduct extends AbsSaveProduct {
	private static ApplicationContext factory;

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	final Logger logger = Logger.getLogger("info");

	private static ResourceBundle bdl = null;

	public static String supplierId;
	// public static String url;
	// public static String picpath;
	public static int day;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		// url = bdl.getString("url");
		// picpath = bdl.getString("picpath");
	}

	@Override
	public Map<String, Object> fetchProductAndSave() {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();

		// 首先获取季节码
		// http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetAllSeasonCode?DBContext=Default&key=8IZk2x5tVN

		Gson gson = new Gson();

		String[] databaseArray = new String[] { "Default" }; //
		OutTimeConfig outTimeConfig = new OutTimeConfig(1000 * 60 * 2, 1000 * 60 * 2, 1000 * 60 * 2);

		for (String database : databaseArray) {
			String season_json = HttpUtil45
					.get("http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetAllSeasonCode?DBContext=" + database
							+ "&key=8IZk2x5tVN", outTimeConfig, null);

			SeasoncodeList season_list = gson.fromJson(season_json, new TypeToken<SeasoncodeList>() {
			}.getType());

			String producturl = "";

			for (Seasoncode obj : season_list.getSeasonCode()) {
				int i = 1;
				while (true) {
					// 然后根据季节码抓取sku
					// http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetProducts?DBContext=Default&CategoryId=&BrandId=&SeasonCode=[[seasoncode]]&StartIndex=[[startindex]]&EndIndex=[[endindex]]&key=8IZk2x5tVN
					producturl = "http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetProducts?DBContext="
							+ database
							+ "&CategoryId=&BrandId=&SeasonCode=[[seasoncode]]&StartIndex=[[startindex]]&EndIndex=[[endindex]]&key=8IZk2x5tVN";
					String url = null;
					try {
						url = producturl
								.replaceAll("\\[\\[seasoncode\\]\\]", URLEncoder.encode(obj.getSeasonCode(), "UTF-8"))
								.replaceAll("\\[\\[startindex\\]\\]", "" + i)
								.replaceAll("\\[\\[endindex\\]\\]", "" + (i + 100));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

					String json = null;
					try {

						json = HttpUtil45.get(url, outTimeConfig, null);
						logger.info("json=" + json);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (json != null && !json.isEmpty()) {
						Products list = null;
						try {
							list = gson.fromJson(json, new TypeToken<Products>() {
							}.getType());
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (list != null && list.getProduct() != null) {
							String priceUrl;
							String itemID;
							String stock;
							for (Spu spu : list.getProduct()) {
								// spu入库
								SpuDTO spudto = new SpuDTO();
								spudto.setBrandName(spu.getProducer_id());
								spudto.setCategoryGender(spu.getType());
								spudto.setCategoryName(spu.getCategory());
								spudto.setCreateTime(new Date());
								spudto.setSeasonId(obj.getSeasonCode());
								spudto.setSupplierId(supplierId);
								spudto.setSpuId(spu.getProduct_id());
								spudto.setId(UUIDGenerator.getUUID());
								spudto.setMaterial(spu.getProduct_detail());
								spudto.setPicUrl(spu.getUrl());
								spudto.setSpuName(spu.getDescription());

								spuList.add(spudto);

								for (Sku sku : spu.getItems().getItem()) {
									// sku入库操作
									stock = sku.getStock();
									if (StringUtils.isBlank(stock)) {
										stock = "0";
									} else {
										if (Integer.valueOf(stock) <= 0) {
											stock = "0";
										}
									}
									SkuDTO skudto = new SkuDTO();
									skudto.setCreateTime(new Date());
									skudto.setBarcode(sku.getBarcode());
									skudto.setColor(sku.getColor());
									skudto.setId(UUIDGenerator.getUUID());
									skudto.setProductCode(spu.getProduct_name());
									skudto.setProductDescription(spu.getProduct_detail());
									skudto.setProductName(spu.getDescription());

									if (sku.getItem_size().length() > 4) {
										skudto.setProductSize(
												sku.getItem_size().substring(0, sku.getItem_size().length() - 4));
									} else {
										skudto.setProductSize(sku.getItem_size());
									}
									skudto.setSkuId(sku.getBarcode()); // sku.getItem_id()
									itemID = sku.getItem_id();
									priceUrl = "http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetPriceByItemID?DBContext="
											+ database + "&ItemID=" + itemID + "&key=8IZk2x5tVN";
									try {
										json = HttpUtil45.get(priceUrl, outTimeConfig, null);
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
									}
									if (json != null && !json.isEmpty()) {
										Price price = null;
										price = gson.fromJson(json, new TypeToken<Price>() {
										}.getType());
										if (price != null && price.getMarket_price() != null
												|| price.getSuply_price() != null) {
											skudto.setMarketPrice(price.getMarket_price());
											skudto.setSupplierPrice(price.getSuply_price().replace(",", "."));
										}
									}

									skudto.setSpuId(spu.getProduct_id());
									skudto.setStock(sku.getStock());
									skudto.setSupplierId(supplierId);

									skuList.add(skudto);

									// 保存图片
									List<String> imgList = new ArrayList<String>();
									if (sku.getPictures() != null) {
										for (String imageUrl : sku.getPictures()) {
											if (imageUrl != null) {
												imgList.add(imageUrl);
											}
										}
										imageMap.put(skudto.getSkuId() + ";" + skudto.getProductCode() + " "
												+ skudto.getColor(), imgList);
										// pfs.savePicture(supplierId, null,
										// skudto.getSkuId(), imgList);
									}

								}
							}
						} else {
							break;
						}

						if (list != null && list.getProduct() != null && list.getProduct().length < 100) {
							break;
						}
					}
					i += 100;
				}
			}
		}

		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}

	public static void main(String[] args) throws Exception {
		// 加载spring
		loadSpringContext();
		FrameFetchProduct stockImp = (FrameFetchProduct) factory.getBean("frameSpinnaker");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		stockImp.handleData("sku", supplierId, day, "");
	}

}
