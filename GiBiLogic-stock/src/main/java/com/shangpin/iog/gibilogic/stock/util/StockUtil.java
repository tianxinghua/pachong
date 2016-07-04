package com.shangpin.iog.gibilogic.stock.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gibilogic.stock.dto.Product;

public class StockUtil {
	
	public Map<String,String> getStockMap(){
		String productJson = "";
		int pageNum = 1;
		Gson gson = new GsonBuilder().create();
		HashMap<String, String> stockMap = new HashMap<String, String>();
		Map<String,Product> json = null;
		while(true){
			System.out.println("页码"+pageNum);
			productJson = HttpUtil45.get("http://shop.areadocks.it/en/api/product?pagesize=100&page="+pageNum++, new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10),null);
			json = gson.fromJson(productJson, new TypeToken<Map<String, Product>>(){}.getType());
			if (json.size()==0) {
				break;
			}
			for (Entry<String, Product> entry : json.entrySet()) {
				
				if (stockMap.containsKey(entry.getValue().getSkuId())) {
					continue;
				}else{
					stockMap.put(entry.getValue().getSkuId(), entry.getValue().getStock());
				}
			}
		}
		return stockMap;
	}
}
