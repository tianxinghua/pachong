package com.shangpin.iog.spinnaker.stock.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

/**
 * Created by loyalty on 15/6/8.
 */
@Component("spinnaker")
public class FetchProduct extends AbsSaveProduct {
	final Logger logger = Logger.getLogger(this.getClass());
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String database = "";
	private static String key = "";
	private static String seasonUrl = "";
	private static String productUrl = "";
	private static String priceUrl = "";
	private static List<Spu> allList = null;
	private Gson gson = new Gson();
	private static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		key = bdl.getString("key");
		database = bdl.getString("database");
		seasonUrl = bdl.getString("seasonUrl");
		productUrl = bdl.getString("productUrl");
		priceUrl = bdl.getString("priceUrl");
		if(bdl.getString("flag")!=null){
			flag = Boolean.parseBoolean(bdl.getString("flag"));
		}
		allList = new ArrayList<Spu>();
	}

	public void sendAndSaveProduct() {
		handleData("spu", supplierId, day, null);
	}

	public Map<String, Object> fetchProductAndSave() {

		Map<String, Object> returnMap = null;
		try {
			String[] databaseArray = null; //
			if (database != null) {
				databaseArray = database.split(",");
			}
			OutTimeConfig outTimeConfig = new OutTimeConfig(1000 * 60 * 2,
					1000 * 60 * 2, 1000 * 60 * 2);
			for (String database : databaseArray) {
				String season_json = HttpUtil45.get(seasonUrl + database
						+ "&key=" + key, outTimeConfig, null);
				SeasoncodeList season_list = gson.fromJson(season_json,
						new TypeToken<SeasoncodeList>() {
						}.getType());
				for (Seasoncode obj : season_list.getSeasonCode()) {
					int i = 1;
					while (true) {
						String producturl = productUrl + database
								+ "&CategoryId=&BrandId=&SeasonCode="
								+ URLEncoder.encode(obj.getSeasonCode())
								+ "&StartIndex=" + i + "&EndIndex=" + (i + 100)
								+ "&key=" + key;
						System.out.println(producturl);
						String json = null;
						try {
							json = HttpUtil45.get(producturl, outTimeConfig,
									null);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (json != null && !json.isEmpty()) {
							Products list = null;
							try {
								list = gson.fromJson(json,
										new TypeToken<Products>() {
										}.getType());
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (list != null && list.getProduct() != null) {
								for (Spu spu : list.getProduct()) {
									supp.setData(gson.toJson(spu));
									pushMessage(null);
									allList.add(spu);
								}
							}
							if (list != null && list.getProduct() != null
									&& list.getProduct().length < 100) {
								break;
							}
						}
						i += 100;
					}
				}
			}

			if (flag) {
				returnMap = productSave();
			}

		} catch (Exception s) {
		}
		return returnMap;
	}

	private Map<String, Object> productSave() {
		logger.info("总的商品数量：" + allList.size());
		System.out.println("总的商品数量：" + allList.size());
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();
		if (allList != null && allList.size() > 0) {
			for (Spu spu : allList) {
				if (null != spu.getItems() && null != spu.getItems().getItem()
						&& spu.getItems().getItem().length > 0) {
					SpuDTO spuDTO = new SpuDTO();
					spuDTO.setSupplierId(supplierId);
					for (Sku sku : spu.getItems().getItem()) {
						SkuDTO skuDTO = new SkuDTO();
						String colorCode = "";
						String colorName = "";
						String color = sku.getColor();
						if (color.contains(" ")) {
							String firstStr = color.substring(0,
									color.indexOf(" "));
							if (hasDigit(firstStr)) {
								colorCode = firstStr;
								colorName = color.substring(color.indexOf(" "))
										.trim();
							} else {
								colorCode = color;
								colorName = color;
							}
						} else {
							colorCode = color;
							colorName = color;
						}
						spuDTO.setId(UUIDGenerator.getUUID());
						spuDTO.setSpuId(spu.getProduct_id() + "-" + colorCode);
						spuDTO.setSpuName(spu.getDescription());
						spuDTO.setCategoryGender(spu.getType());
						spuDTO.setCategoryName(spu.getCategory());
						spuDTO.setBrandName(spu.getProducer_id());
						spuDTO.setSeasonName(spu.getSeason());
						spuDTO.setMaterial(spu.getProduct_detail());
						spuDTO.setProductOrigin(spu.getProduct_MadeIn());
						
						skuDTO.setId(UUIDGenerator.getUUID());
						skuDTO.setColor(colorName);
						skuDTO.setProductCode(spu.getProduct_name() + " "
								+ colorCode);
						skuDTO.setProductDescription(spu.getProduct_detail());
						skuDTO.setSpuId(spuDTO.getSpuId());
						skuDTO.setSupplierId(supplierId);
						skuDTO.setSkuId(sku.getBarcode());
						skuDTO.setBarcode(sku.getBarcode());
						if (sku.getItem_size().length() > 4) {
							skuDTO.setProductSize(sku.getItem_size().substring(
									0, sku.getItem_size().length() - 4));
						} else {
							skuDTO.setProductSize(sku.getItem_size());
						}
						String stock = sku.getStock();
						if (StringUtils.isEmpty(stock)) {
							stock = "0";
						} else if (Integer.valueOf(stock) <= 0) {
							stock = "0";
						}
						skuDTO.setStock(stock);
						
						
						List<String> pics = sku.getPictures();
						imageMap.put(skuDTO.getSkuId() + ";" + skuDTO.getProductCode(),pics);
						
						skuList.add(skuDTO);
						spuList.add(spuDTO);
					}
				}
			}
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}

	private Price getPrice(String itemId, String barCode, String dataBase) {
		Price price = null;
		String json = null;
		try {
			// sanremo特殊处理
			if ("2015082801463".equals(supplierId)) {
				json = HttpUtil45.get(priceUrl + dataBase + "&ItemID=" + itemId
						+ "&key=" + key, new OutTimeConfig(1000 * 60 * 2,
						1000 * 60 * 2, 1000 * 60 * 2), null);
			} else {
				json = HttpUtil45.get(priceUrl + dataBase + "&barcode="
						+ barCode + "&key=" + key, new OutTimeConfig(
						1000 * 60 * 2, 1000 * 60 * 2, 1000 * 60 * 2), null);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		if (json != null && !json.isEmpty()) {
			price = gson.fromJson(json, new TypeToken<Price>() {
			}.getType());
		}
		return price;
	}

	/**
	 * 判断字符串中是否含有数字
	 * 
	 * @param content
	 * @return
	 */
	private boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;
	}

	/**
	 * 校验价格
	 * 
	 * @param price
	 * @return
	 */
	public static String verifyPrice(String price) {
		if (StringUtils.isBlank(price)) {
			return "0.00";
		} else {
			return price.replaceAll(",", ".");
		}
	}
}
