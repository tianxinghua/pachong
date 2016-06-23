package com.shangpin.iog.gibilogic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.gibilogic.dto.Category;
import com.shangpin.iog.gibilogic.dto.Color;
import com.shangpin.iog.gibilogic.dto.Product;
import com.shangpin.iog.gibilogic.util.Excel2DTO;
import com.shangpin.product.AbsSaveProduct;
@Component("gibilogicService")
public class GibiLogicServiceIml extends AbsSaveProduct{
	
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	final Logger logger = Logger.getLogger("info");

	private static ResourceBundle bdl = null;

	public static String supplierId;
	public static String picpath;
	public static String excelPath;
	public static int day;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		picpath = bdl.getString("picpath");
		excelPath = bdl.getString("excelPath");
	}
	public static void main(String[] args){
//		new RevolveFrameFetchProduct().fetchProductAndSave();
//	  	加载spring
        loadSpringContext();
        GibiLogicServiceIml stockImp =(GibiLogicServiceIml)factory.getBean("gibilogicService");
		stockImp.handleData("sku", supplierId, day, picpath);
	}
	
	//sku:List(skuDTO) spu:List(spuDTO) image: Map(id;picName,List) 
	@Override
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Product> productMap = getProductJson();
		Map<String, String> categoryMap = getCategoryMap();
		Map<String, String> colorMap = getColorMap();
		
		
		//组织数据
		ArrayList<SkuDTO> skuList = new ArrayList<SkuDTO>();
		ArrayList<SpuDTO> spuList = new ArrayList<SpuDTO>();
		ArrayList<String> imgList = new ArrayList<String>();
		Map<String,List<String>> imgMap = new HashMap<String, List<String>>();
		Map<String,Object> returnMap = new HashMap<String, Object>();
		
		for (Entry<String, Product> entry : productMap.entrySet()) {
			Product p = entry.getValue();
			SkuDTO sku = new SkuDTO();
			SpuDTO spu = new SpuDTO();
			
			sku.setId(UUIDGenerator.getUUID());
			sku.setSkuId(p.getSkuId());
			sku.setSupplierId(supplierId);
			sku.setSpuId(p.getSkuId());
			sku.setColor(colorMap.get(p.getColore()));
			sku.setStock(p.getStock());
			sku.setMarketPrice(p.getProduct_price());
			sku.setSupplierPrice("");
			sku.setSalePrice("");
			sku.setSaleCurrency("dollar");
			sku.setProductSize(p.getSize());
			sku.setProductName(p.getProduct_name());
			sku.setProductDescription(p.getProduct_desc());

			skuList.add(sku);
			
			spu.setId(UUIDGenerator.getUUID());
			spu.setSupplierId(supplierId);
			spu.setSpuId(p.getSkuId());
			spu.setBrandName(p.getVendor_name());
			spu.setCategoryName(getCategoryName(categoryMap, p.getCategory()));
			spu.setMaterial(p.getMaterial());
			spu.setProductOrigin(p.getMadein());
			spu.setSeasonName(p.getSeason());
			spu.setCategoryGender(p.getGender());
			
			spuList.add(spu);
			
			imgList.add(p.getImage());
			imgMap.put(p.getSkuId()+";"+p.getSkuId(), imgList);
		}
		returnMap.put("sku", skuList);		
		returnMap.put("spu", spuList);		
		returnMap.put("image", imgMap);		
		
		return returnMap;
	}
	private Map<String,Product> getProductJson(){
		
		String productJson = "";
		int pageNum = 1;
		Gson gson = new GsonBuilder().create();
		HashMap<String, Product> jsonMap = new HashMap<String, Product>();
		Map<String,Product> json = null;
		while(true){
			System.out.println("页码"+pageNum);
			productJson = HttpUtil45.get("http://shop.areadocks.it/en/api/product?pagesize=100&page="+pageNum++, new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10),null);
			json = gson.fromJson(productJson, new TypeToken<Map<String, Product>>(){}.getType());
			if (json.size()==0) {
				break;
			}
			for (Entry<String, Product> entry : json.entrySet()) {
				
				if (jsonMap.containsKey(entry.getValue().getSkuId())) {
					Product product = jsonMap.get(entry.getValue().getSkuId());
					product.setCategory(product.getCategory()+","+entry.getValue().getCategory());
				}else{
					jsonMap.put(entry.getValue().getSkuId(), entry.getValue());
				}
			}
		}
		
		return jsonMap;
	}
	
	private Map<String,String> getCategoryMap(){
		
		HashMap<String, String> categoryMap = new HashMap<String, String>();

		
		String categoryJson = "";
		int pageNum = 1;
		Gson gson = new GsonBuilder().create();
		Category[] categorys = new Category[]{};
		
		while(true){
			categoryJson = HttpUtil45.get("http://shop.areadocks.it/en/api/category?pagesize=100&page="+pageNum++, new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10),null);
			categorys = gson.fromJson(categoryJson, categorys.getClass());
			if (categorys.length==0) {
				break;
			}
			for (Category category : categorys) {
				categoryMap.put(category.getCategoryId(), category.getCategoryName());
			}
		}
		
		return categoryMap;
	}
	
	private String getCategoryName(Map<String,String> categoryMap,String categoryIds){
		String[] catIds = categoryIds.split(",");
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < catIds.length; i++) {
			if (StringUtils.isNotBlank(categoryMap.get(catIds[i]))) {
				sb.append(categoryMap.get(catIds[i])).append(",");
			}
		}
		return sb.toString();
	}
	
	private Map<String,String> getColorMap(){
		HashMap<String, String> colorMap = new HashMap<String,String>();
		Short[] needColsNo = new Short[]{0,2};
		List<Color> colorList = Excel2DTO.toDTO(excelPath, 0, needColsNo, Color.class);
		for (Color color : colorList) {
			colorMap.put(color.getColorId(), color.getColorName());
		}
		return colorMap;
	}
}
