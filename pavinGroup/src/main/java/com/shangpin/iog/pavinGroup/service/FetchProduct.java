package com.shangpin.iog.pavinGroup.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
		
		List<Rss> list = null;
		try {
				for(int i=0;i<array.length;i++){
					list = new ArrayList<Rss>();
					System.out.println("-------------------------第"+(i+1)+"个开始--------------------------------");
					fetchProduct(array[i]);
					System.out.println("-------------------------第"+(i+1)+"个结束--------------------------------");
				} 
			}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	private void fetchProduct(String url){
		
		try {
			String xml = null;
			
			xml = HttpUtil45
					.get(url,
							new OutTimeConfig(1000 * 60*5, 1000 * 60*5, 1000 * 60*5),
							null);
			System.out.println(url);
				ByteArrayInputStream is = null;
				
				is = new ByteArrayInputStream(
						xml.getBytes("UTF-8"));
				Rss rss = null;
				rss = ObjectXMLUtil.xml2Obj(Rss.class, is);
				if(rss!=null){
					Channel channel = rss.getChannel();
					if(channel!=null){
						List<Item> item = channel.getListItem();
						messMappingAndSave(item);
						if(channel.getNextPage()!=null){
							fetchProduct(channel.getNextPage());
						}
					}
				}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * message mapping and save into DB
	 */
	private void messMappingAndSave(List<Item> array) {
				if(array!=null){
					for (Item item : array) {
						SpuDTO spu = new SpuDTO();
						try {
							spu.setId(UUIDGenerator.getUUID());
							spu.setSupplierId(supplierId);
							spu.setSpuId(item.getSupplierSkuNo());
							spu.setCategoryName(item.getGroup_description());
							spu.setBrandName(item.getBrand());
							spu.setSpuName(item.getTitle());
							spu.setMaterial(item.getMaterial());
							spu.setCategoryGender(item.getGender());
							productFetchService.saveSPU(spu);
						} catch (Exception e) {
							try {
								productFetchService.updateMaterial(spu);
							} catch (ServiceException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
						
						if (StringUtils.isNotBlank(item.getImages())) {
							String[] picArray = item.getImages().split("\\|");
							productFetchService.savePicture(supplierId, null, item.getSupplierSkuNo(), Arrays.asList(picArray));
						}
						
					}
				}
	}

}



