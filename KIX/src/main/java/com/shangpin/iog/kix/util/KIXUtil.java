package com.shangpin.iog.kix.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.kix.dto.Data;
import com.shangpin.iog.kix.dto.MetaField;
import com.shangpin.iog.kix.dto.MetaFields;
import com.shangpin.iog.kix.dto.Product;

public class KIXUtil {
	
	public List<Product> getAllProducts(){
//		/fields
		Gson gson = new Gson();
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/products.json?limit=250&page=";
		String string = null;
		Data data = null;
		List<Product> productList = new ArrayList<Product>();
		//https://kix-files.myshopify.com/admin/products/382788195/metafields.json
//		https://kix-files.myshopify.com/admin/products.json?fields=variants
		int pageNum = getPageNum();
		for (int i = 1; i <=pageNum; i++) {
			string = HttpUtil45.get(url, new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10), null);
			data = gson.fromJson(string, Data.class);
			for (Product product : data.getProducts()) {
				productList.add(product);
			}
		}
		return productList; 
	}
	private int getPageNum(){
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/count.json";
		String count = HttpUtil45.get(url, new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10), null);
		return Integer.valueOf(count)%250==0?Integer.valueOf(count)/250:Integer.valueOf(count)/250+1;
	}
	public Map<String,String> getMetaField(String productId){
	
		String url = "https://kix-files.myshopify.com/admin/products/"+productId+"/metafields.json";
		String string = HttpUtil45.get(url, new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10), null);
		Gson gson = new Gson();
		MetaFields metaFields = gson.fromJson(string, MetaFields.class);
		Map<String,String> returnMap = new HashMap<String, String>();
		for (MetaField metaField : metaFields.getMetafields()) {
			returnMap.put(metaField.getKey(), metaField.getValue());
		}
		return returnMap;
	}
}
