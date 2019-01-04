package com.shangpin.iog.ostore.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.ostore.dto.AtelierDTO;
import com.shangpin.iog.ostore.dto.SupplierProduct;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.sun.org.apache.bcel.internal.generic.PUSH;

/**
 * Created by monkey on 2015/12/25.
 */
@Component("atelierostore")
public class FetchProduct {
    final Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String api_url = "";
	private static String spu_interface = "";
	private static String sku_interface = "";
	private static String image_interface = "";
	private static String price_interface = "";
	private static String userName = "";
	private static String password = "";
	private static String savePath = "";
	private static String supplierId = "";
	private static String supplierName = "";
	public static Gson gson = null;
	public static SupplierProduct supp = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");	
		
		api_url = bdl.getString("api_url");
		spu_interface = bdl.getString("spu_interface");
		sku_interface = bdl.getString("sku_interface");
		image_interface = bdl.getString("image_interface");
		price_interface = bdl.getString("price_interface");
		userName = bdl.getString("userName");
		password = bdl.getString("password");
		savePath = bdl.getString("savePath");
		supplierName = bdl.getString("supplierName");
		
		gson = new Gson();
		supp = new SupplierProduct();
		supp.setMessageType("json");
		supp.setSupplierName(supplierName);
		supp.setSupplierId(supplierId);
	}
    @Autowired
    private ProductFetchService productFetchService;
    @Autowired
	ProductSearchService productSearchService;

	/**
	 * 抓取主程序
	 */
	public void fetchProductAndSave() {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			//具体的业务
			Map<String,List<String>> imgMap = handleImagData();
			Map<String,List<String>> skuMap =  handleSkuData();
			Map<String,String> spuMap =  handleSpuData();
			Map<String,String> priceMap = handlePriceData();
			
			for (Map.Entry<String, String> entry : spuMap.entrySet()) {
				
				AtelierDTO dto = new AtelierDTO();
				dto.setSpu(entry.getValue());
				String spuId = entry.getKey();
				dto.setSpuId(spuId);
				dto.setSku(skuMap.get(spuId));
				if(imgMap.get(spuId)==null){
					dto.setImage(new ArrayList<String>());	
				}else{
					dto.setImage(imgMap.get(spuId));
				}
				
				dto.setPrice(priceMap.get(spuId));
				
				supp.setMessageId(UUIDGenerator.getUUID());
				supp.setMessageDate(sim.format(new Date()));
				supp.setData(gson.toJson(dto));
				
			}
		}catch(Exception ex){
			
		}
		
	}
	/**
	 * 处理spu数据，返回spuId<->AtelierSpu map
	 * @return
	 */
	private Map<String,String> handleSpuData(){
		String spuData = getInterfaceData(spu_interface,new OutTimeConfig(1000*60*90,1000*60*60,1000*60*90));
		if(HttpUtil45.errorResult.equals(spuData)){
			return null;
		}else{
			Map<String,String> spuMap = new HashMap<String,String>();
			String[] spuStrings = spuData.split("\\r\\n"); 
			for (int i = 1; i < spuStrings.length; i++) {
				try {
					
					if (StringUtils.isNotBlank(spuStrings[i])) {	
						String data = spuStrings[i].replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","");
						String spuId = data.split(";")[0];
						spuMap.put(spuId, data);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			logger.info("spu的总数是======="+spuMap.size()); 
			return spuMap;
		}
	}
	
	/**
	 * 处理sku数据，返回AtelierSku集合
	 * @return
	 */
	private Map<String,List<String>> handleSkuData(){
		String skuData = getInterfaceData(sku_interface,new OutTimeConfig(1000*60*60,1000*60*5,1000*60*60));
		if(HttpUtil45.errorResult.equals(skuData)){
			return null;
		}else{
			Map<String,List<String>> skuMap = new HashMap<String,List<String>>();
			String[] skuStrings = skuData.split("\\r\\n");
			for (int i = 1; i < skuStrings.length; i++) {
				try {
					if (StringUtils.isNotBlank(skuStrings[i])) {	
						String data = skuStrings[i].replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","");
						String spuId = data.split(";")[0];
						if (skuMap.containsKey(spuId)) {
							skuMap.get(spuId).add(data);
						}else {
							List<String> list = new ArrayList<String>();
							list.add(data);
							skuMap.put(spuId, list);
						}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			logger.info("sku的总数是======="+skuMap.size()); 
			return skuMap;
		}
	}
	
	/**
	 * 处理图片数据，返回spuId<->图片集合 对照关系集合
	 * @return
	 */
	private Map<String,List<String>> handleImagData(){
		String imageData = getInterfaceData(image_interface,new OutTimeConfig(1000*60*60,1000*60*5,1000*60*60));
		if(HttpUtil45.errorResult.equals(imageData)){
			return null;
		}else{
			Map<String,List<String>> imgMap = new HashMap<String,List<String>>();
			String[] imageStrings = imageData.split("\\r\\n");
			for (int j = 2; j < imageStrings.length; j++) {
				try {
					if (StringUtils.isNotBlank(imageStrings[j])) {	
						String data = imageStrings[j].replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","");
						String spuId = data.split(";")[0];
						if (imgMap.containsKey(spuId)) {
							imgMap.get(spuId).add(data);
						}else {
							List<String> list = new ArrayList<String>();
							list.add(data);
							imgMap.put(spuId, list);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return imgMap;
		}
		
	}
	
	/**
	 * 处理价格数据，返回spuId<->价格  对应关系集合
	 * @return
	 */
	private Map<String,String> handlePriceData(){
		String priceData = getInterfaceData(price_interface,new OutTimeConfig(1000*60*60,1000*60*5,1000*60*60));
		if(HttpUtil45.errorResult.equals(priceData)){
			return null;
		}else{
			Map<String,String> priceMap = new HashMap<String,String>();
			String[] priceStrings = priceData.split("\\r\\n");        
	        for (int i = 1; i < priceStrings.length; i++) {
	        	try {	
	        		
	        		if (StringUtils.isNotBlank(priceStrings[i])) {	
						String data = priceStrings[i].replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","");
						String spuId = data.split(";")[0];
						priceMap.put(spuId, data);
	            	}       		
	    			
				} catch (Exception e) {
					e.printStackTrace();				
				}
	        }
	        return priceMap;
		}
		
	}
	
	/**
	 * 下载接口文件并返回数据
	 * @param api_interface
	 * @param outTimeConfig
	 * @return
	 */
	private String getInterfaceData(String api_interface,OutTimeConfig outTimeConfig) {
		logger.info("开始拉取"+api_interface+"的数据......"); 
		String data = HttpUtil45.postAuth(api_url+api_interface,null,outTimeConfig,userName,password);		
		int i=0;
		while((StringUtils.isBlank(data) || HttpUtil45.errorResult.equals(data)) && i<10){ 
			try {
				Thread.sleep(1000*3);
				data = HttpUtil45.postAuth(api_url+api_interface,null,outTimeConfig,userName,password);				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				i++;
			}		        	
		}
		logger.info("拉取"+api_interface+"用了=="+i+"次"); 
		return data;
	}	
}
