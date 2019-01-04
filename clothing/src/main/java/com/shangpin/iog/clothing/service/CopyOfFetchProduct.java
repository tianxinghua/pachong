package com.shangpin.iog.clothing.service;
//package com.shangpin.iog.forzieri.service;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.ResourceBundle;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.shangpin.iog.common.utils.UUIDGenerator;
//import com.shangpin.iog.dto.SkuDTO;
//import com.shangpin.iog.dto.SpuDTO;
//import com.shangpin.iog.forzieri.utils.DownloadAndReadCSV;
//import com.shangpin.iog.service.ProductFetchService;
//import com.shangpin.iog.service.ProductSearchService;
//import com.shangpin.iog.service.SkuPriceService;
//import com.shangpin.product.AbsSaveProduct;
//
//@Component("forzieri")
//public class CopyOfFetchProduct extends AbsSaveProduct{
//
//	private static Logger logger = Logger.getLogger("info");
//	private static Logger logMongo = Logger.getLogger("mongodb");
//	private static ResourceBundle bdl = null;
//	private static String supplierId;
//	private static String url;
//	public static int day;
//	static {
//		if (null == bdl)
//			bdl = ResourceBundle.getBundle("conf");
//		supplierId = bdl.getString("supplierId");
//		url = bdl.getString("url");
//		day = Integer.valueOf(bdl.getString("day"));
//		if(bdl.getString("flag")!=null){
//			flag = Boolean.parseBoolean(bdl.getString("flag"));
//		}
//	}
//	@Autowired
//	private ProductFetchService productFetchService;
//	@Autowired
//	private SkuPriceService skuPriceService;
//	@Autowired
//	private ProductSearchService productSearchService;
//	
//	public void sendAndSaveProduct(){
//		handleData("spu", supplierId, day, null);
//	}
//	
//	@Override
//	public Map<String, Object>  fetchProductAndSave(){
//		logger.info("开始抓取");
//		Map<String, Object> returnMap = null;
//		try {
//			JSONArray array = DownloadAndReadCSV.readLocalCSV();
//			Iterator<JSONObject> it = array.iterator();
//            while (it.hasNext()) {
//                JSONObject ob = (JSONObject) it.next();
//                supp.setData(ob.toString());
//    			pushMessage(null);
//            }
//        	if(flag){
//				returnMap = fetchProductAndSave(array);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		logger.info("抓取结束");
//		return returnMap;
//	}
//	public Map<String,Object> fetchProductAndSave(JSONArray array) {
//
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
//		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
//		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();
//		Iterator<JSONObject> it = array.iterator();
//        while (it.hasNext()) {
//            JSONObject ob = (JSONObject) it.next();
//            SkuDTO sku = new SkuDTO();
//			sku.setId(UUIDGenerator.getUUID());
//			sku.setSkuId(ob.getString("gtin"));
//			sku.setSupplierId(supplierId);
//			sku.setSpuId(ob.getString("id"));
//			sku.setProductCode(ob.getString("gtin"));
//			sku.setColor(ob.getString("color"));
//			sku.setSaleCurrency("USD");
//			sku.setMarketPrice(ob.getString("price"));
//			String stock = ob.getString("availability");
//			Pattern pattern = Pattern.compile("[0-9]*"); 
//		    Matcher isNum = pattern.matcher(stock);
//		    if(isNum.matches() ){
//			   sku.setStock(stock);
//		    } 
//			
//			sku.setBarcode(ob.getString("gtin"));
//			sku.setProductSize(ob.getString("size"));
//			sku.setProductName(ob.getString("title"));
//			sku.setProductDescription(ob.getString("description"));
//			skuList.add(sku);
//			String images = ob.getString("additional_image_link");
//			imageMap.put(sku.getSkuId() + ";" + sku.getProductCode(), Arrays.asList(images));
//			
//			SpuDTO spu = new SpuDTO();
//			spu.setId(UUIDGenerator.getUUID());
//			spu.setSpuId(ob.getString("id"));
//			spu.setSupplierId(supplierId);
//			spu.setBrandName(ob.getString("brand"));
//			spu.setCategoryName(ob.getString("google_product_category"));
//			spu.setCategoryGender(ob.getString("gender"));
//			spu.setMaterial(ob.getString("material"));
//			String desc = ob.getString("description");
//			if(desc.contains("Made in")){
//				String [] arr = desc.split("\\.");
//				for(String origin:arr){
//					if(origin.contains("Made in")){
//						System.out.println(origin);
//						spu.setProductOrigin(origin);
//						break;
//					}
//				}
//			}
//			spuList.add(spu);
//        }
//		returnMap.put("sku", skuList);
//		returnMap.put("spu", spuList);
//		returnMap.put("image", imageMap);
//		return returnMap;
//	}
//
//}
//
