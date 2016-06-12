package com.shangpin.iog.deliberti.stock.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.deliberti.stock.dto.Product;

public class DataUtil extends Thread{
	private Map<String,String> list;
	private String spuId;
	public DataUtil(Map<String,String> list, String spuId) {
		super();
		this.list = list;
		this.spuId = spuId;
	}

	
	@Override
	public void run() {
		Product product = getData(spuId);
		for (String sizestock : product.getStock()) {
			list.put(product.getSpuId()+"-"+sizestock.split("~")[0], sizestock.split("~")[1]);
		}
	}
	
	
	private Product getData(String spuId){
		String string = HttpUtil45.get("http://gicos.it/outsrc.php?do=products&cart="+spuId, new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10), null);
		return fillDTO(new Product(),string.split("\\|"));
	}
	
	private <T> T fillDTO(T t, String[] data) {
		try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				fields[i].set(t, data[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
}
