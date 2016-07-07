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
import org.apache.commons.lang.StringUtils;
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
import com.shangpin.iog.monnierfreres.utils.CVSUtil;
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

		try {
			OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*5,1000*60*60,1000*60*60);
			String data = HttpUtil45.get(url, outTimeConf, null, "mnfrootadmin", "LfHaP2016!");
//			System.out.println(data); 
			List<Item> items = CVSUtil.readCSV(data, Item.class, '|');
			
			for (Item item : items) {
				try {
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
					sku.setSkuId(item.getSku());
					sku.setSupplierId(supplierId);
					sku.setSpuId(item.getProduct_id());
					sku.setProductCode(item.getProduct_id());
					sku.setColor(item.getColor());
//					sku.setSaleCurrency();
					sku.setMarketPrice(item.getPrice());
					sku.setStock(item.getQty());
//					sku.setBarcode(item.getBarCode());
					sku.setProductSize(item.getSize());
					sku.setProductName(item.getCategory_breadcrumb()+" "+item.getBrand());
					sku.setProductDescription(item.getDescription());
					skuList.add(sku);

					List<String> images = new ArrayList<String>();
					if(StringUtils.isNotBlank(item.getImage_url_1())){
						images.add(item.getImage_url_1());
					}
					if(StringUtils.isNotBlank(item.getImage_url_2())){
						images.add(item.getImage_url_2());
					}
					if(StringUtils.isNotBlank(item.getImage_url_3())){
						images.add(item.getImage_url_3());
					}
					if(StringUtils.isNotBlank(item.getImage_url_4())){
						images.add(item.getImage_url_4());
					}
					if(StringUtils.isNotBlank(item.getImage_url_5())){
						images.add(item.getImage_url_5());
					}				
					imageMap.put(sku.getSkuId() + ";" + sku.getProductCode(), images);
					
					SpuDTO spu = new SpuDTO(); 
					spu.setId(UUIDGenerator.getUUID());
					spu.setSpuId(sku.getSpuId());
					spu.setSupplierId(supplierId);
					spu.setBrandName(item.getBrand());
					spu.setCategoryName(item.getCategory_breadcrumb());
//					spu.setSubCategoryName(product.getSubCategory());
					spu.setCategoryGender("female");
					spu.setMaterial(item.getMaterial()+" "+item.getCharacteristics1()+";"+item.getCharacteristics2()+";"+item.getCharacteristics3()+";"+item.getCharacteristics4());
//					spu.setProductOrigin(item.get);
					spuList.add(spu);
				} catch (Exception e) {
					e.printStackTrace(); 
				}
				
			}	
		} catch (Exception e) {
			e.printStackTrace(); 
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
//		loadSpringContext();
//		MonnierFrameFetchProduct stockImp = (MonnierFrameFetchProduct) factory
//				.getBean("monnierFrameFetchProduct");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		stockImp.handleData("sku", supplierId, day, picpath);
		MonnierFrameFetchProduct stockImp = new MonnierFrameFetchProduct();
		stockImp.fetchProductAndSave();
	}

}
