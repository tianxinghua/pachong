package com.shangpin.iog.articoli;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
import com.shangpin.iog.articoli.dto.Product;
import com.shangpin.iog.articoli.dto.Products;
import com.shangpin.iog.articoli.dto.Size;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("articoli")
public class FechProduct {

	private static Logger logInfo = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 60,
			1000 * 60 * 5, 1000 * 60 * 5);

	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String uri;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		uri = bdl.getString("uri");
	}

	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;

	public void fechAndSave() {
		try {

			Date startDate, endDate = new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate, day
					* -1, "D");
			// 获取原有的SKU 仅仅包含价格和库存
			Map<String, SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService
						.findStockAndPriceOfSkuObjectMap(supplierId, startDate,
								endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}

			String result = HttpUtil45.get(uri, outTimeConf, null);
			result = result.replaceAll("<Season>", "<season>").replaceAll("</Season>", "</season>");
			result = result.replaceAll("<Color>", "<color>").replaceAll("</Color>", "</color>");
			result = result.replaceAll("<Material>", "<material>").replaceAll("</Material>", "</material>");
//			Products products = XMLUtil.gsonXml2Obj(Products.class, result);
//			String result = UnicodeReader.file2Striing(new File("E:\\lubaijiang\\B.xml"));
//			result = result.replaceAll("<other_images/>", "<other_images><image1></image1><image2></image2></other_images>");
//			result = result.replaceAll("<sizes/>","");//<sizes><size><size_desc></size_desc><size_stock></size_stock></size></sizes>
//			result = result.replaceAll("<sizes />","<size><size_desc></size_desc><size_stock></size_stock></size>");
			logInfo.info(result); 			
			Products products = ObjectXMLUtil.xml2Obj(Products.class, result);
			if (products.getProduct().size() > 0) {
				for (Product product : products.getProduct()) {
					if(null != product.getSizes() && null != product.getSizes().getSize()){
						for(Size size :product.getSizes().getSize()){
							//sku
							try{
								String productsize = size.getSize_desc().replaceAll("½", ".5");
								SkuDTO sku = new SkuDTO(); //38½
								sku.setId(UUIDGenerator.getUUID());
				                sku.setSupplierId(supplierId);
				                sku.setSkuId(product.getCode()+"-"+productsize);
				                sku.setSpuId(product.getCode());
				                sku.setProductName(product.getProduct_name());
				                sku.setMarketPrice(product.getMarket_price());
				                sku.setSalePrice(product.getSell_price());
				                sku.setProductCode(product.getCode());
				                sku.setColor(product.getColor());
				                sku.setProductDescription(product.getDescription());
				                sku.setProductSize(productsize);
				                sku.setStock(size.getSize_stock()); 
								// sku入库
								try {
									if (skuDTOMap.containsKey(sku.getSkuId())) {
										skuDTOMap.remove(sku.getSkuId());
									}
									productFetchService.saveSKU(sku);

								} catch (ServiceException e) {
									try {
										if (e.getMessage().equals("数据插入失败键重复")) {
											// 更新价格和库存
											productFetchService.updatePriceAndStock(sku);
										} else {
											e.printStackTrace();
										}
									} catch (ServiceException e1) {
										logError.error(e1.getMessage());
										e1.printStackTrace();
									}
								}
								
							}catch(Exception ex){
								logError.error(ex);
								ex.printStackTrace();
							}
							
						}
					}				
					//spu
					try{						
						
						List<String> pics = new ArrayList<>();
						pics.add(product.getImage());
						pics.add(product.getOther_images().getImage1());
						pics.add(product.getOther_images().getImage2());
						pics.add(product.getOther_images().getImage3());
						pics.add(product.getOther_images().getImage4());
						pics.add(product.getOther_images().getImage5());
						pics.add(product.getOther_images().getImage6());
						pics.add(product.getOther_images().getImage7());
						pics.add(product.getOther_images().getImage8());
						pics.add(product.getOther_images().getImage9());
						pics.add(product.getOther_images().getImage10());					
						productFetchService.savePicture(supplierId,
								product.getCode(), null, pics);

						// spu 入库
						SpuDTO spu = new SpuDTO();	
						spu.setId(UUIDGenerator.getUUID());
						spu.setSpuId(product.getCode());
						spu.setSupplierId(supplierId);
						spu.setCategoryGender(product.getGender());
						spu.setCategoryName(product.getCategory());
						spu.setSeasonId(product.getSeason());
						spu.setBrandName(product.getBrand());
						spu.setMaterial(product.getMaterial()); 
						try {
							productFetchService.saveSPU(spu);
						} catch (ServiceException e) {
							logError.error(e.getMessage());
							try {
								productFetchService.updateMaterial(spu);
							} catch (ServiceException ex) {
								logError.error(ex.getMessage());
								ex.printStackTrace();
							}

							e.printStackTrace();
						}
						
					}catch(Exception ex){
						ex.printStackTrace();
						logError.error(ex);
					}
					
				}

			}

			// 更新网站不再给信息的老数据
			for (Iterator<Map.Entry<String, SkuDTO>> itor = skuDTOMap
					.entrySet().iterator(); itor.hasNext();) {
				Map.Entry<String, SkuDTO> entry = itor.next();
				if (!"0".equals(entry.getValue().getStock())) {// 更新不为0的数据
																// 使其库存为0
					entry.getValue().setStock("0");
					try {
						productFetchService.updatePriceAndStock(entry
								.getValue());
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

//	public static void main(String[] args){
//		String result = HttpUtil45.get(uri, outTimeConf, null);
//		Products products = XMLUtil.gsonXml2Obj(Products.class, result);
//	}
	
}
