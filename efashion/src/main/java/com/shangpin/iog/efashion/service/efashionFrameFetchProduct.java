package com.shangpin.iog.efashion.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.efashion.dao.Item;
import com.shangpin.iog.efashion.dao.Item_images;
import com.shangpin.iog.efashion.dao.Result;
import com.shangpin.iog.efashion.dao.ReturnObject;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("efashion")
public class efashionFrameFetchProduct extends AbsSaveProduct {

	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String picpath;
	public static int day;
	public static int max;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		max = Integer.valueOf(bdl.getString("max"));
		url = bdl.getString("url");
		picpath = bdl.getString("picpath");
	}
	static List<Item> retList = new ArrayList<Item>();
	static int i = 0;

	public static void getProductList(int index) {
		String json = HttpUtil45.get(
				url + "&limit=" + max + "&offset=" + index, new OutTimeConfig(
						1000 * 60, 1000 * 60, 1000 * 60), null);
		System.out.println(json);
		ReturnObject obj = new Gson().fromJson(json, ReturnObject.class);
		// 第一步：获取活动信息
		if (obj != null) {
			Result result = obj.getResults();
			List<Item> item = result.getItems();
			if (!item.isEmpty()) {
				retList.addAll(item);
				i++;
				System.out.println("------------------------第" + i
						+ "页---------------------------");
				System.out.println("商品数量：" + item.size());
				System.out.println("总的商品数量：" + retList.size());
				getProductList(max * i + 1);
			}
		}
	}

	/**
	 * message mapping and save into DB
	 */
	public Map<String, Object> fetchProductAndSave() {

		getProductList(1);
		System.out.println("总的商品数量：" + retList.size());
		logger.info("总的商品数量：" + retList.size());
		System.out.println("--拉取数据end--");
		logger.info("--拉取数据end--");
		// messMappingAndSave();
		System.out.println("--正在保存中.....--");
		logger.info("--正在保存中.....--");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();

		for (Item item : retList) {
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSupplierId(supplierId);
			spu.setSpuId(item.getProduct_id());
			spu.setCategoryName(item.getCategory());
			spu.setBrandName(item.getBrand());
			spu.setSpuName(item.getItem_intro());
			spu.setMaterial(item.getTechnical_info());
			spu.setProductOrigin(item.getMade_in());
			spu.setSeasonName(item.getSeason_year());
			spu.setCategoryGender(item.getGender());
			spuList.add(spu);

			String skuId = null;
			SkuDTO sku = new SkuDTO();
			sku.setId(UUIDGenerator.getUUID());
			sku.setSupplierId(supplierId);
			sku.setSpuId(item.getProduct_id());
			String size = null;
			size = item.getSize();
			if (size == null) {
				size = "A";
			}
			skuId = item.getSku_id() + "|" + item.getProduct_reference() + "|"
					+ item.getColor_reference() + "|" + size;
			sku.setSkuId(skuId);
			sku.setProductSize(size);
			sku.setStock(item.getQuantity());
			sku.setProductCode(item.getProduct_reference());
			sku.setMarketPrice(item.getPrice_IT());
			sku.setColor(item.getColor());
			sku.setProductName(item.getItem_intro());
			sku.setProductDescription(item.getItem_description() + ","
					+ item.getSuitable());
			sku.setSaleCurrency(item.getCurrency());
			sku.setSalePrice("");
			sku.setSupplierPrice("");
			skuList.add(sku);

			Item_images images = item.getItem_images();
			List<String> picArray = new ArrayList<String>();
			if(StringUtils.isNotBlank(images.getUrl1())){
				picArray.add(images.getUrl1());
			}
			if(StringUtils.isNotBlank(images.getUrl2())){
				picArray.add(images.getUrl2());
			}
			if(StringUtils.isNotBlank(images.getUrl3())){
				picArray.add(images.getUrl3());
			}
			if(StringUtils.isNotBlank(images.getUrl4())){
				picArray.add(images.getUrl4());
			}
			imageMap.put(sku.getSkuId() + ";" + sku.getSkuId().split("\\|")[1]+URLEncoder.encode("|")+sku.getSkuId().split("\\|")[2], picArray);
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	public static void main(String[] args) throws Exception {
	  	//加载spring
        loadSpringContext();
        efashionFrameFetchProduct stockImp =(efashionFrameFetchProduct)factory.getBean("efashion");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		stockImp.handleData("sku", supplierId, day, picpath);
	}
}
