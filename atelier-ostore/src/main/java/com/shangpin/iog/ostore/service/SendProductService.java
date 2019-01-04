package com.shangpin.iog.ostore.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.ostore.dto.AtelierDTO;
import com.shangpin.product.AbsSaveProduct;

public class SendProductService extends AbsSaveProduct{
	
	@Autowired
	FetchProductService fetchProductService;
	
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
				Gson gson = new Gson();
				supp.setData(gson.toJson(dto));
				pushMessage(gson.toJson(supp));
				
			}
		}catch(Exception ex){
			
		}
	}
	
	private Map<String,String> handleSpuData(){
		String spuData = fetchProductService.fetchSpuData();
		if(spuData==null){
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
			return spuMap;
		}
	}
	
	/**
	 * 处理sku数据，返回AtelierSku集合
	 * @return
	 */
	private Map<String,List<String>> handleSkuData(){
		String skuData = fetchProductService.fetchSkuData();
		if(skuData==null){
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
			return skuMap;
		}
	}
	
	/**
	 * 处理图片数据，返回spuId<->图片集合 对照关系集合
	 * @return
	 */
	private Map<String,List<String>> handleImagData(){
		String imageData = fetchProductService.fetchImagData();
		if(imageData==null){
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
		String priceData =  fetchProductService.fetchPriceData();
		if(priceData == null){
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
}
