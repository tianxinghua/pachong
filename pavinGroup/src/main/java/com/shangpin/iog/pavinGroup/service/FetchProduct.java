package com.shangpin.iog.pavinGroup.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TimeZone;

import javax.xml.bind.JAXBException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.pavinGroup.dto.Channel;
import com.shangpin.iog.pavinGroup.dto.Item;
import com.shangpin.iog.pavinGroup.dto.Rss;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("pavinGroup")
public class FetchProduct {

	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String Designers;
	private static String Donna;
	private static String FlashSale;
	private static String Highlights ;
	private static String NuoviArrivi	;
	private static String SpecialPrice;
	private static String Spring;
	private static String Uomo ;
	private static String [] array;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		Designers = bdl.getString("Designers");
		Donna = bdl.getString("Donna");
		FlashSale = bdl.getString("FlashSale");
		Highlights = bdl.getString("Highlights");
		NuoviArrivi = bdl.getString("NuoviArrivi");
		SpecialPrice = bdl.getString("SpecialPrice");
		Spring = bdl.getString("Spring");
		Uomo = bdl.getString("Uomo");
		array = new String[]{Designers,Donna,FlashSale,Highlights,NuoviArrivi,SpecialPrice,Spring,Uomo};
	}

	/**
	 * fetch product and save into db
	 */
	public void fetchProductAndSave() {

		String xml = null;
		for(int i=0;i<array.length;i++){
			xml = HttpUtil45
					.get(array[i],
							new OutTimeConfig(1000 * 60*5, 1000 * 60*5, 1000 * 60*5),
							null);
			readLine(xml,i+"");
			System.out.println(array[i]);
			try {
				ByteArrayInputStream is = new ByteArrayInputStream(
						xml.getBytes("UTF-8"));
				Rss rss = null;
				rss = ObjectXMLUtil.xml2Obj(Rss.class, is);
				messMappingAndSave(rss);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * message mapping and save into DB
	 */
	private void messMappingAndSave(Rss rss) {
		if (rss != null) {
			Channel channel = rss.getChannel();
			if (channel != null) {
				List<Item> array = channel.getListItem();
				if(array!=null){
					for (Item item : array) {
						// 把新活动保存入库到EVENT_PRODUCT表中
						SpuDTO spu = new SpuDTO();
						try {
							spu.setId(UUIDGenerator.getUUID());
							spu.setSupplierId(supplierId);
							spu.setSpuId(item.getSupplierSkuNo());
							spu.setCategoryName(item.getGroup_description());
							// spu.setSubCategoryName(item.getProduct_category_name());
							spu.setBrandName(item.getBrand());
							spu.setSpuName(item.getTitle());
							spu.setMaterial(item.getMaterial());
							spu.setCategoryGender(item.getGender());
							productFetchService.saveSPU(spu);
						} catch (Exception e) {
							e.printStackTrace();
						}

						SkuDTO sku = new SkuDTO();
						try {
							sku.setId(UUIDGenerator.getUUID());
							sku.setSupplierId(supplierId);
							sku.setSpuId(item.getSupplierSkuNo());
							sku.setSkuId(item.getSupplierSkuNo());
							sku.setProductSize(item.getProductSize());
							sku.setStock(item.getStock());
							sku.setMarketPrice(item.getPrice());
							sku.setColor(item.getProductColor());
							sku.setProductName(item.getTitle());
							sku.setProductDescription(item.getDescription());
							 sku.setSaleCurrency("EUR");
							productFetchService.saveSKU(sku);
							
							if (StringUtils.isNotBlank(item.getImages())) {
								String[] picArray = item.getImages().split("\\|");
								for (String picUrl : picArray) {
									ProductPictureDTO dto = new ProductPictureDTO();
									dto.setPicUrl(picUrl);
									dto.setSupplierId(supplierId);
									dto.setId(UUIDGenerator.getUUID());
									dto.setSkuId(item.getSupplierSkuNo());
									try {
										productFetchService
												.savePictureForMongo(dto);
										// System.out.println("图片保存success");
									} catch (ServiceException e) {
										e.printStackTrace();
									}
								}
							}
						} catch (ServiceException e) {
							if (e.getMessage().equals("数据插入失败键重复")) {
								try {
									productFetchService.updatePriceAndStock(sku);
								} catch (ServiceException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							} else {
								e.printStackTrace();
							}

						}

					}
				}
			}
		}

	}
	private static String getJson(String fileName) {

		String fullFileName = "D:/product.xml";

		File file = new File(fullFileName);
		Scanner scanner = null;
		StringBuilder buffer = new StringBuilder();
		try {
			scanner = new Scanner(file, "utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
			}
		} catch (Exception e) {

		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		System.out.println(buffer.toString());
		return buffer.toString();
	}
	  private static void readLine(String content,String name){
	    	File file = new File("C://"+name+".json");
	    	FileWriter fwriter = null;
	    	   try {
	    	    fwriter = new FileWriter(file);
	    	    fwriter.write(content);
	    	   } catch (Exception ex) {
	    	    ex.printStackTrace();
	    	   } finally {
	    	    try {
	    	     fwriter.flush();
	    	     fwriter.close();
	    	    } catch (Exception ex) {
	    	     ex.printStackTrace();
	    	    }
	    	   }
	    }
}



