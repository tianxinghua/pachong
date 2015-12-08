package com.shangpin.iog.rossanaNiccolai.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.rossanaNiccolai.dao.Image;
import com.shangpin.iog.rossanaNiccolai.dao.Item;
import com.shangpin.iog.rossanaNiccolai.dao.Material;
import com.shangpin.iog.rossanaNiccolai.dao.Result;
import com.shangpin.iog.rossanaNiccolai.dao.ReturnObject;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("rossanaNiccolai")
public class FetchProduct {

	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
	}

	/**
	 * fetch product and save into db
	 */
	public void fetchProductAndSave() {
		
		String json = HttpUtil45
				.get(url,
						new OutTimeConfig(1000 * 60, 1000 * 60, 1000 * 60),
						null);
		
		ReturnObject obj = new Gson().fromJson(json, ReturnObject.class);
		// 第一步：获取活动信息
		if(obj!=null){
			Result result = obj.getResults();
			if(result!=null){
				messMappingAndSave(result);
			}
		}
	}
	/**
	 * message mapping and save into DB
	 */
	private void messMappingAndSave(Result result) {
		
		String code = result.getReqCode();
		if("200".equals(code)){
			List<Item> array = result.getItems();
				if(array!=null){
					for (Item item : array) {
						SpuDTO spu = new SpuDTO();
						try {
							spu.setId(UUIDGenerator.getUUID());
							spu.setSupplierId(supplierId);
							spu.setSpuId(item.getProduct_id());
							spu.setCategoryName(item.getFirst_category());
							spu.setSubCategoryName(item.getSecond_category());
							spu.setBrandName(item.getBrand());
							spu.setSpuName(item.getItem_intro());
							Material [] list = item.getTechnical_info();
							StringBuffer str = new StringBuffer();
							if(list!=null){
								for(Material m:list){
									str.append(","+m.getPercentage()+"%"+m.getName());
								}
								spu.setMaterial(str.substring(1));
							}	
							spu.setCategoryGender(item.getGender());
							productFetchService.saveSPU(spu);
						} catch (Exception e) {
							e.printStackTrace();
						}

						SkuDTO sku = new SkuDTO();
						try {
							sku.setId(UUIDGenerator.getUUID());
							sku.setSupplierId(supplierId);
							sku.setSpuId(item.getProduct_id());
							String size = null;
							if("".equals(item.getSize())||item.getSize()==null){
								size = "A";
							}else{
								size = item.getSize();
							}
							sku.setSkuId(item.getProduct_id()+"|"+item.getProduct_reference()+"|"+item.getColor_reference()+"|"+size);
							sku.setProductSize(item.getSize());
							sku.setStock(item.getQuantity());
							sku.setMarketPrice(item.getPrice());
							sku.setColor(item.getColor());
							sku.setProductName(item.getItem_intro());
							sku.setProductDescription(item.getItem_description());
							sku.setSaleCurrency(item.getCurrency());
							productFetchService.saveSKU(sku);
							
							Image picArray = item.getItem_images();
							if(picArray!=null){
								for (String picUrl : picArray.getFull()) {
									ProductPictureDTO dto = new ProductPictureDTO();
									dto.setPicUrl(picUrl);
									dto.setSupplierId(supplierId);
									dto.setId(UUIDGenerator.getUUID());
									dto.setSkuId(item.getProduct_id());
									try {
										productFetchService
												.savePictureForMongo(dto);
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
