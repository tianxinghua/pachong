package com.shangpin.iog.inviqa.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.inviqa.dto.Product;
import com.shangpin.iog.inviqa.util.MyJsonUtil;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by 赵根春 on 2015/12/25.
 */
@Component("inviqa")
public class FetchProduct {

	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}
	
	
	/**
	 * fetch product and save into db
	 */
	public void fetchProductAndSave() {

		List<Product> list = MyJsonUtil.getProductList();
		messMappingAndSave(list);
	}
	
	
	
	/**
	 * message mapping and save into DB
	 */
	private void messMappingAndSave(List<Product> array) {

		if (array != null) {
			
			
			for (Product item : array) {
				
				if(item.getSkuId()!=null){
					SpuDTO spu = new SpuDTO();
					try {
						spu.setId(UUIDGenerator.getUUID());
						spu.setSupplierId(supplierId);
						spu.setSpuId(item.getSpuId());
						spu.setSpuName(item.getProductName());
						spu.setCategoryName(item.getCategoryName());
						spu.setSubCategoryName(item.getSubCategoryName());
						spu.setBrandName(item.getBrandName());
						spu.setSpuName(item.getProductName());
						if(item.getMaterial()!=null){
							if(item.getMaterial().toLowerCase().indexOf("no data")==-1){
								spu.setMaterial(item.getMaterial());
							}
						}
						if(item.getSeasonName()!=null){
							if(item.getSeasonName().toLowerCase().indexOf("no data")==-1){
								spu.setMaterial(item.getSeasonName());
							}
						}
						
						if(item.getProductOrigin()!=null){
							if(item.getProductOrigin().toLowerCase().indexOf("no data")==-1){
								spu.setProductOrigin(item.getProductOrigin());
							}
						}
						spu.setCategoryGender(item.getCategoryGender());
						productFetchService.saveSPU(spu);
					} catch (Exception e) {
						try {
							productFetchService.updateMaterial(spu);
						} catch (ServiceException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					String image = item.getImages();
					if (StringUtils.isNotBlank(image)) {
						String images[] = image.split("\\|\\|");
						productFetchService.savePicture(supplierId, null,
								item.getSkuId(), Arrays.asList(images));
					}

					SkuDTO sku = new SkuDTO();
					try {
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSpuId(item.getSpuId());
						String proSize = item.getSize();
						if ("no".equals(proSize.toLowerCase())) {
							sku.setProductSize("A");
						} else {
							sku.setProductSize(proSize);
						}
						sku.setSkuId(item.getSkuId());
						sku.setStock(item.getStock());
						sku.setSalePrice(item.getSalePrice());
						sku.setMarketPrice(item.getMarketPrice());
						if(item.getSupplierPrice()!=null){
							if(item.getSupplierPrice().toLowerCase().indexOf("no data")==-1){
								sku.setSupplierPrice(item.getSupplierPrice());
							}
						}
						if(item.getBarcode()!=null){
							if(item.getBarcode().toLowerCase().indexOf("no data")==-1){
								sku.setBarcode(item.getBarcode());
							}
						}
						sku.setColor(item.getColor());
						sku.setProductName(item.getProductName());
						sku.setProductDescription(item.getProductDescription());
						sku.setProductCode(item.getProductCode());
						sku.setSaleCurrency(item.getSaleCurrency());
						productFetchService.saveSKU(sku);
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
