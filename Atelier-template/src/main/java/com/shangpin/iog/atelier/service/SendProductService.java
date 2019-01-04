package com.shangpin.iog.atelier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SendProductService{
	
	/**
	 * 推送消息到队列
	 */
	public Map<String,String> handleSpuData(String spuData){
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
	public Map<String,List<String>> handleSkuData(String skuData){
		
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
	public Map<String,List<String>> handleImagData(String imageData){
		
		if(imageData==null){
			return null;
		}else{
			Map<String,List<String>> imgMap = new HashMap<String,List<String>>();
			String[] imageStrings = imageData.split("\\r\\n");
			for (int j = 2; j < imageStrings.length; j++) {
				try {
					if (StringUtils.isNotBlank(imageStrings[j])) {	
						String data = imageStrings[j].replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","");
						String spuId = data.split(";")[0].replace("-","");
						if (imgMap.containsKey(spuId)) {
							imgMap.get(spuId).add(data);
						}else {
							List<String> list = new ArrayList<String>();
							list.add(data);
							imgMap.put(spuId, list);
						}
					}
				} catch (Exception e) {
				}
			}
			return imgMap;
		}
		
	}
	
	/**
	 * 处理价格数据，返回spuId<->价格  对应关系集合
	 * @return
	 */
	public Map<String,String> handlePriceData(String priceData){
		
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
