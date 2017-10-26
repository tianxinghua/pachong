package com.shangpin.iog.srl.service;


import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by wangchao on 2017/10/26.
 */
@Component
public class ProductFetchUtil {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String stockUrl = "";
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		stockUrl = bdl.getString("stockUrl");
	}

	public String getProductStock(String skuNo) throws Exception {
		String stock = "";
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("references", skuNo);
		String resultData = HttpUtil45.get(stockUrl, new OutTimeConfig(1000 * 60, 1000 * 60 * 30, 1000 * 60 * 30),
				headerMap);
		JSONObject json = JSONObject.fromObject(resultData);
		JSONArray array = (JSONArray) json.get("catalog");
		if (array.size() == 0) {
			logger.info("skuNo:" + skuNo + "此sku不存在");
		}
		JSONObject data = (JSONObject) array.get(0);
		stock = String.valueOf(data.get("qty"));
		return stock;
	}

	public static void main(String[] args) {
		ProductFetchUtil util = new ProductFetchUtil();
		String skuNo = "811332";
		try {
			System.out.println(util.getProductStock(skuNo));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
