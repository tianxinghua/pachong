package com.shangpin.iog.eds.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.eds.dao.Item;
import com.shangpin.iog.eds.dao.Material;
import com.shangpin.iog.eds.dao.Result;
import com.shangpin.iog.eds.dao.ReturnObject;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("efashion")
public class FetchProduct extends AbsSaveProduct{

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
	
	public static int day;
	public static int max;
	public static Gson gson = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		max = Integer.valueOf(bdl.getString("max"));
		url = bdl.getString("url");
		if(bdl.getString("flag")!=null){
			flag = Boolean.parseBoolean(bdl.getString("flag"));
		}
		gson = new Gson();
	}
	static int i=0;
	private List<Item> allItemList = new ArrayList<Item>();
	
	public void sendAndSaveProduct(){
		handleData("spu", supplierId, day, null);
	}
	
	public  void fetchProduct(int index){
		
		String json = HttpUtil45
				.get(url+"&limit="+max+"&offset="+index,
						new OutTimeConfig(1000 * 60 *2, 1000 * 60*2, 1000 * 60*2),
						null);
		System.out.println(json);
		try{
			ReturnObject obj = gson.fromJson(json, ReturnObject.class);
			if(obj!=null){
				Result result = obj.getResults();
				List<Item> itemList = result.getItems();
				if(itemList!=null&&!itemList.isEmpty()){
					allItemList.addAll(itemList);
					//发送到队列
					for(Item item:itemList){
						supp.setData(gson.toJson(item));
						pushMessage(null);
					}
					i++;
					fetchProduct(max*i+1);
				}
			}
		}catch(Exception e){
			i++;
			fetchProduct(max*i+1);
			System.out.println(e.getMessage());
		}
		
	}
	
	public Map<String, Object> fetchProductAndSave() {
		fetchProduct(1);
		Map<String, Object> returnMap = null;
		if(flag){
			returnMap = saveProduct();
		}
		return returnMap;
	}

	private Map<String, Object> saveProduct() {
		logger.info("总的商品数量：" + allItemList.size());
		System.out.println("总的商品数量：" + allItemList.size());
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();

		for (Item item : allItemList) {
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSupplierId(supplierId);
			spu.setSpuId(item.getProduct_id());
			spu.setBrandName(item.getBrand());
			spu.setSpuName(item.getItem_intro());
			spu.setCategoryName(item.getFirst_category());
			List<Material> list = item.getTechnical_info();
			if(list!=null&&!list.isEmpty()){
				StringBuffer str = new StringBuffer();
				for(Material m:list){
					str.append(",").append(m.getPercentage()).append(m.getName());
				}
				spu.setMaterial(str.substring(1));
			}
			spu.setProductOrigin(item.getMade_in());
			spu.setSeasonName(item.getSeason_year()+item.getSeason_reference());
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
			skuId = item.getProduct_id() + "-" + size;
			sku.setSkuId(skuId);
			sku.setProductSize(size);
			sku.setStock(item.getQuantity());
			sku.setProductCode(item.getProduct_reference());
			sku.setMarketPrice(item.getRetail_price());
			sku.setColor(item.getColor());
			sku.setProductName(item.getItem_intro());
			sku.setProductDescription(item.getItem_description());
			sku.setSaleCurrency(item.getCurrency());
			sku.setSalePrice("");
			sku.setSupplierPrice(item.getPrice());
			skuList.add(sku);

			String [] images = item.getItem_images().getFull();
			imageMap.put(sku.getSkuId() + ";" + item.getProduct_reference()+URLEncoder.encode("|")+item.getColor_reference(), Arrays.asList(images));
		}
		allItemList.clear();
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
}
