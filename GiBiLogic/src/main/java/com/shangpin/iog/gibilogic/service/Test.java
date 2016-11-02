package com.shangpin.iog.gibilogic.service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gibilogic.dto.Product;

public class Test {
	public static void main(String[] args) {
//		String str = HttpUtil45.get("http://shop.areadocks.it/en/api/category?page=1&pagesize=100",
//				new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
//						1000 * 60 * 10), null);
//		System.out.println(str);
//
//		URL url;
//		try {
//			url = new URL("http://shop.areadocks.it/en/api/category?page=1&pagesize=100");
//			URLConnection conn = url.openConnection();
//			InputStream inStream = conn.getInputStream();
//			String string = IOUtils.toString(inStream);
//			
//			System.out.println(string);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		GibiLogicServiceIml gib = new GibiLogicServiceIml();
//		Map<String, Product> productMap = gib.getProductJson();
//		System.out.println(productMap);
		String productJson = "";
		int pageNum = 1;
		Gson gson = new GsonBuilder().create();
		HashMap<String, Product> jsonMap = new HashMap<String, Product>();
		Map<String,Product> json = null;
		StringBuffer kk  = new StringBuffer();
		while(true){
			System.out.println("pageNum=="+pageNum);
			productJson = HttpUtil45.get("http://shop.areadocks.it/en/api/product?pagesize=100&page="+pageNum++, new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10),null);
			json = gson.fromJson(productJson, new TypeToken<Map<String, Product>>(){}.getType());
			if (json.size()==0) {
				break;
			}

			for (Map.Entry<String, Product> entry : json.entrySet()) {
				Product product = entry.getValue();
				kk.append(" vir_product_id:" + product.getVirtuemart_product_id() + " sku:" + product.getSkuId() + " parent_id:" + product.getProduct_parent_id()
						+  " size:" +  product.getSize() + " color:" +  product.getColore() +  "  pic:" + product.getImage());
				kk.append("\r\n");
//				if (jsonMap.containsKey(entry.getValue().getSkuId())) {
//					Product product = jsonMap.get(entry.getValue().getSkuId());
//					product.setCategory(product.getCategory()+","+entry.getValue().getCategory());
//				}else{
//					jsonMap.put(entry.getValue().getSkuId(), entry.getValue());
//				}
			}
		}

		FileOutputStream fop = null;
		File file;
		String content = kk.toString();


		try {
			file = new File("e:/tmp/lzr.txt");
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
