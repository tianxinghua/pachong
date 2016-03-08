
package com.shangpin.iog.kix.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.kix.dto.Count;
import com.shangpin.iog.kix.dto.Data;
import com.shangpin.iog.kix.dto.MetaField;
import com.shangpin.iog.kix.dto.MetaFields;
import com.shangpin.iog.kix.dto.Product;
import com.shangpin.iog.kix.dto.Variant;

public class KIXUtil {
	
	public List<Product> getAllProducts(){
		Gson gson = new Gson();
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/products.json?collection_id=213136323&limit=250&page=";
		String string = null;
		Data data = null;
		List<Product> productList = new ArrayList<Product>();
		int pageNum = getPageNum();
		for (int i = 1; i <=pageNum; i++) {
			string = HttpUtil45.get(url, new OutTimeConfig(1000*60*30, 1000*60*60, 1000*60*60), null);
			data = gson.fromJson(string, Data.class);
			for (Product product : data.getProducts()) {
				productList.add(product);
			}
		}
		return productList; 
	}
	private int getPageNum(){
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/products/count.json?collection_id=213136323";
		String str = HttpUtil45.get(url, new OutTimeConfig(1000*60*30, 1000*60*60, 1000*60*60), null);
		Gson gson = new Gson();
		Count count = gson.fromJson(str, Count.class);
		return Integer.valueOf(count.getCount())%250==0?Integer.valueOf(count.getCount())/250:Integer.valueOf(count.getCount())/250+1;
	}
	public Map<String,String> getMetaField(String productId){
		String url = "https://kix-files.myshopify.com/admin/products/"+productId+"/metafields.json";
		String string = HttpUtil45.get(url, new OutTimeConfig(1000*60*30, 1000*60*60, 1000*60*60), null);
		Gson gson = new Gson();
		MetaFields metaFields = gson.fromJson(string, MetaFields.class);
		Map<String,String> returnMap = new HashMap<String, String>();
		for (MetaField metaField : metaFields.getMetafields()) {
			returnMap.put(metaField.getKey(), metaField.getValue());
		}
		return returnMap;
	}
	
	//variants
	public Map<String,String> getStockMap(){
		Gson gson = new Gson();
		Map<String,String> returnMap = new HashMap<String, String>();
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/products.json?fields=variants&collection_id=213136323&limit=250&page=";
		String string = null;
		Data data = null;
		int pageNum = getPageNum();
		for (int i = 1; i <=pageNum; i++) {
			string = HttpUtil45.get(url, new OutTimeConfig(1000*60*30, 1000*60*60, 1000*60*60), null);
			data = gson.fromJson(string, Data.class);
			for (Product product : data.getProducts()) {
				for (Variant variant : product.getVariants()) {
					returnMap.put(variant.getId(), variant.getInventory_quantity());
				}
			}
		}
		return returnMap;
	}
	public static void main(String[] args) {
		new KIXUtil().getStockMap();
	}
}

