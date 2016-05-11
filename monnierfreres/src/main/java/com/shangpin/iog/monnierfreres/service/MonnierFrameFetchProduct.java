package com.shangpin.iog.monnierfreres.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.monnierfreres.dto.Item;
import com.shangpin.iog.monnierfreres.dto.Product;
import com.shangpin.iog.monnierfreres.utils.DownloadAndReadCSV;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

@Component("monnierFrameFetchProduct")
public class MonnierFrameFetchProduct extends AbsSaveProduct {

	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String picpath;
	private static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		day = Integer.valueOf(bdl.getString("day"));
		picpath = bdl.getString("picpath");
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;

	public Map<String, Object> fetchProductAndSave() {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();

		List<Product> list = null;
		try {
			list = DownloadAndReadCSV.readLocalCSV();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Product product : list) {
			SpuDTO spu = new SpuDTO();
			List<Item> items = product.getItems();
			if (items.size() == 0) {
				continue;
			}
			for (Item item : items) {
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSkuId(item.getItemCode());
				sku.setSupplierId(supplierId);
				sku.setSpuId(product.getProductCode());
				sku.setProductCode(product.getProductCode());
				sku.setColor(product.getColor());
				sku.setSaleCurrency("USD");
				sku.setMarketPrice(item.getPrice());
				sku.setStock(item.getStock());
				sku.setBarcode(item.getBarCode());
				sku.setProductSize(item.getSize());
				sku.setProductName(product.getProductName());
				sku.setProductDescription(product.getDescription());
				skuList.add(sku);

				String[] images = product.getImage_url();
				imageMap.put(sku.getSkuId() + ";" + sku.getProductCode(), Arrays.asList(images));
				 
				spu.setId(UUIDGenerator.getUUID());
				spu.setSpuId(product.getProductCode());
				spu.setSupplierId(supplierId);
				spu.setBrandName(product.getBrand());
				spu.setCategoryName(product.getCategory());
				spu.setSubCategoryName(product.getSubCategory());
				spu.setCategoryGender("female");
				spu.setMaterial(product.getMaterial());
				spu.setProductOrigin(product.getMade());
				spuList.add(spu);
			}
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}

	private static ApplicationContext factory;

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) throws Exception {
		// 加载spring
		loadSpringContext();
		MonnierFrameFetchProduct stockImp = (MonnierFrameFetchProduct) factory
				.getBean("monnierFrameFetchProduct");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		stockImp.handleData("sku", supplierId, day, picpath);
	}

}
