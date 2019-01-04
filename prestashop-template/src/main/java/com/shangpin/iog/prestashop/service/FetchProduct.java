package com.shangpin.iog.prestashop.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.prestashop.dto.Associations;
import com.shangpin.iog.prestashop.dto.Categories;
import com.shangpin.iog.prestashop.dto.Category;
import com.shangpin.iog.prestashop.dto.Combination;
import com.shangpin.iog.prestashop.dto.Combinations;
import com.shangpin.iog.prestashop.dto.Description;
import com.shangpin.iog.prestashop.dto.DescriptionShort;
import com.shangpin.iog.prestashop.dto.IdAttributeGroup;
import com.shangpin.iog.prestashop.dto.Image;
import com.shangpin.iog.prestashop.dto.Images;
import com.shangpin.iog.prestashop.dto.Language;
import com.shangpin.iog.prestashop.dto.Name;
import com.shangpin.iog.prestashop.dto.Prestashop;
import com.shangpin.iog.prestashop.dto.Product;
import com.shangpin.iog.prestashop.dto.ProductOptionValue;
import com.shangpin.iog.prestashop.dto.ProductOptionValues;
import com.shangpin.iog.prestashop.dto.Products;
import com.shangpin.iog.prestashop.dto.StockAvailable;
import com.shangpin.iog.prestashop.dto.StockAvailables;
import com.shangpin.iog.prestashop.order.Cart;
import com.shangpin.iog.prestashop.order.CartRow;
import com.shangpin.iog.prestashop.order.CartRows;
import com.shangpin.iog.prestashop.order.Order;
import com.shangpin.iog.prestashop.order.OrderRow;
import com.shangpin.iog.prestashop.order.OrderRows;
import com.shangpin.iog.prestashop.pojo.Sku;
import com.shangpin.iog.prestashop.pojo.Spu;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("prestashop-template")
public class FetchProduct extends AbsSaveProduct {

	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	@Autowired
	EventProductService eventProductService;
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String token;

	public static int day;
	public static Gson gson = null;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
		token = bdl.getString("token");
		if (bdl.getString("flag") != null) {
			flag = Boolean.parseBoolean(bdl.getString("flag"));
		}
		gson = new Gson();
	}
	static int i = 0;

	public void sendAndSaveProduct() {
		handleData("spu", supplierId, day, null);
	}

	private static String getResultByUrl(String href) {
		return HttpUtil45.get(href, new OutTimeConfig(1000 * 60 * 3,
				1000 * 60 * 30, 1000 * 60 * 30), null, null, token, null);
	}

	@Override
	public Map<String, Object> fetchProductAndSave() {

		Map<String,Object> returnMap = null;
		List<Spu> allSpu = new ArrayList<Spu>();
		String result = getResultByUrl(url);
		try {
			Prestashop pre = ObjectXMLUtil.xml2Obj(Prestashop.class, result);
			if (pre != null) {
				Products products = pre.getProducts();
				if (products != null) {
					List<Product> list = products.getProduct();
					if (list != null && list.size() > 0) {
						for (Product product : list) {
							Spu spu = new Spu();
							String productUrl = product.getHref();
							result =getResultByUrl(productUrl);
							pre = ObjectXMLUtil.xml2Obj(Prestashop.class,
									result);
							convertProductDTO(pre,spu);
							pushMessage(gson.toJson(spu));
							allSpu.add(spu);
						}
					}
				}
			}
			
			if(flag){
				if(allSpu!=null&&allSpu.size()>0){
				returnMap = getProductMap(allSpu);	
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	private Map<String, Object> getProductMap(List<Spu> allItemList) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();

		for (Spu item : allItemList) {
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSupplierId(supplierId);
			spu.setSpuId(item.getSpuId());
			spu.setBrandName(item.getBrandName());
			spu.setSpuName(item.getSpuName());
			spu.setCategoryName(item.getCategoryName());
			spu.setMaterial(item.getMaterial());
			spu.setProductOrigin(item.getProductOrigin());
			spu.setSeasonName(item.getSeasonName());
			spu.setCategoryGender(item.getGender());
			spuList.add(spu);
			List<Sku> listSku = item.getSkus();
			for(Sku sku:listSku){
				String skuId = null;
				SkuDTO skuDTO = new SkuDTO();
				skuDTO.setId(UUIDGenerator.getUUID());
				skuDTO.setSupplierId(supplierId);
				skuDTO.setSpuId(item.getSpuId());
				String size = null;
				size = sku.getSize();
				skuId = sku.getSkuId();
				skuDTO.setSkuId(skuId);
				skuDTO.setProductSize(size);
				skuDTO.setStock(sku.getQty());
				skuDTO.setProductCode(item.getProductCode());
				skuDTO.setMarketPrice(sku.getMarketPrice());
				skuDTO.setColor(item.getColor());
				skuDTO.setProductName(item.getSpuName());
				skuDTO.setProductDescription(item.getProductDescription());
				skuDTO.setSalePrice("");
				skuList.add(skuDTO);
			}
			imageMap.put(item.getSpuId() + ";" + item.getProductCode(), item.getImages());
		}
		allItemList.clear();
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}

	private void convertProductDTO(Prestashop pre,Spu spu) throws Exception {
		
		if (pre != null) {
			Product product = pre.getProduct();
			if (product != null) {
				String spuName = getSpuName(product);
				String productCode = getProductCode(product);
				String productDescription = getDescription(product);
				if(productDescription!=null){
					String [] arr = productDescription.split("<br>");
					if(arr.length>0){
						for(String de:arr){
							if(de!=null&&de.contains("Color:")){
								spu.setColor(de.replace("Color:","").trim());
							}
							if(de!=null&&de.contains("Details:")){
								spu.setMaterial(de.replace("Details:","").trim());
							}
							if(de!=null&&de.contains("Description:")){
								spu.setProductDescription(de.replace("Description:","").trim());
							}
						}
					}
				}
				Associations associations = product.getAssociations();
				String category = getCategory(associations);
				List<String> images = getImages(associations);
				List<Sku> skus = new ArrayList<Sku>();
				getSkuHref(associations,skus);
				spu.setSpuName(spuName);
				spu.setBrandName(product.getManufacturer_name());
				spu.setCategoryName(category);
				spu.setGender("kid");
				spu.setProductCode(productCode);
				spu.setProductOrigin("");
				spu.setSeasonName("carryover");
				spu.setSpuId(product.getId());
				spu.setImages(images);
				spu.setSkus(skus);
			}
		}
	}

	private String getProductCode(Product product) {
		DescriptionShort descriptionShort = product.getDescription_short();
		if(descriptionShort!=null){
			List<Language> lists = descriptionShort.getLanguage();
			if(lists!=null&&lists.size()>0){
				for(Language language:lists){
					if("3".equals(language.getId())){
						return language.getText();
					}
				}
			}
		}
		return null;
	}

	private String getSpuName(Product product) {
		Name name = product.getName();
		if(name!=null){
			return getLanguageText(name.getLanguage());
		}
		return null;
	}

	private List<String> getImages(Associations associations) {
		List<String> imageList = new ArrayList<String>();
		Images images = associations.getImages();
		if (images != null) {
			List<Image> listImages = images.getList();
			if (listImages != null && listImages.size() > 0) {
				for (Image image : listImages) {
					if (image.getHref() != null) {
						imageList.add(image.getHref());
					}
				}
			}
		}
		return imageList;
	}


	private String getSkuHref(Associations associations,List<Sku> skus) throws Exception {
		Combinations combinations = associations.getCombinations();
		StockAvailables stockAvailables = associations.getStockAvailables();
		if (combinations != null&&stockAvailables!=null) {
			
			List<StockAvailable> listStock = stockAvailables.getStock_available();
			List<Combination> list = combinations.getCombination();
			if (list != null &&listStock!=null&& list.size() > 0&&listStock.size()>0) {
				
				Map<String,String> skuStock = new HashMap<String,String>();
				for(StockAvailable stockAvailable:listStock){
					skuStock.put(stockAvailable.getId_product_attribute(),stockAvailable.getId());
				}
				for (Combination com : list) {
					Sku sku = new Sku();
					String skuUrl = com.getHref();
					String result = getResultByUrl(skuUrl);
					Prestashop pre = ObjectXMLUtil.xml2Obj(Prestashop.class,
							result);
					if (pre != null) {
						Combination combination = pre.getCombination();
						if (combination != null) {
							String barCode = combination.getEan13();
							String price = combination.getPrice();
							String qty = combination.getQuantity();
							String skuId = null;
							if(skuStock.containsKey(combination.getId())){
								//spuId-skuId-stockId ==>spuId、skuId推送订单，stockId更新库存
								skuId =combination.getId_product()+"-"+combination.getId()+"-"+skuStock.get(combination.getId());
							}
							
							sku.setBarCode(barCode);
							sku.setMarketPrice(price);
							sku.setQty(qty);
							sku.setSkuId(skuId);
							
							associations = combination.getAssociations();
							if (associations != null) {
								ProductOptionValues productOptionValues = associations
										.getProductOptionValues();
								if (productOptionValues != null) {
									List<ProductOptionValue> productOptionValueList = productOptionValues
											.getProductOptionValue();
									String size = null;
									if (productOptionValueList != null
											&& productOptionValueList.size() > 0) {
										for (ProductOptionValue productOptionValue : productOptionValueList) {
											String href = productOptionValue.getHref();
											result = getResultByUrl(href);
											pre = ObjectXMLUtil.xml2Obj(Prestashop.class,
													result);
											if(pre!=null){
												productOptionValue = pre.getProductOptionValue();
												if(productOptionValue!=null){
													IdAttributeGroup idAttributeGroup = productOptionValue
															.getIdAttributeGroup();
													if(idAttributeGroup!=null){
														String groupId = idAttributeGroup.getText();
														String text = null;
														Name name = productOptionValue
																.getName();
														if (name != null) {
															text = getLanguageText(name
																	.getLanguage());
														}
														// 2代表颜色
														if ("2".equals(groupId)) {
														}
														// 1代表尺码
														if ("1".equals(groupId)) {
															size = text;
															sku.setSize(size);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					skus.add(sku);
				}
			}
		}
		return null;
	}

	private String getCategory(Associations product) throws JAXBException {
		Categories categories = product.getCategories();
		if (categories != null) {
			List<Category> list = categories.getCategory();
			if (list != null && list.size() > 0) {
				String categoryUrl = list.get(list.size() - 1).getHref();
				String result = getResultByUrl(categoryUrl);
				Prestashop pre = ObjectXMLUtil
						.xml2Obj(Prestashop.class, result);
				if (pre != null) {
					Category category = pre.getCategory();
					if (category != null) {
						Name name = category.getName();
						if (name != null) {
							return getLanguageText(name.getLanguage());
						}
					}
				}
			}
		}
		return null;
	}

	private String getDescription(Product product) {
		Description description = product.getDescription();
		if (description != null) {
			return getLanguageText(description.getLanguage());
		}
		return null;
	}

	private String getLanguageText(List<Language> list) {
		if (list != null && list.size() > 0) {
			for (Language language : list) {
				// 1代表英文
				if ("1".equals(language.getId())) {
					return language.getText();
				}
			}
		}
		return null;
	}
	
}
