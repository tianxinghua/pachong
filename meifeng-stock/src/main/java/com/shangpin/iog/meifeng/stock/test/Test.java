package com.shangpin.iog.meifeng.stock.test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class Test {
	public static void main(String[] args) {
		// http://218.242.57.204:8081/efast/efast_api/webservice/web/index.php
		// http://218.242.249.190:8081/efast_zzm/efast_api/webservice/web/index.php
		/**
		 * app_act   efast.sku.stock.get
		 * sd_id    
		 * sku   关联条码
		 */
		// http://192.168.150.93/efastrjm/efast_api/webservice/web/index.php
		String url = "http://121.41.160.177/efast/efast_api/webservice/web/index.php";
		Map<String, String> param = new HashMap<String, String>();
		param.put("app_nick", "openapi");
		param.put("app_key", "d721b36f22dddf80e7ac");
		param.put("app_secret", "bcaa673e76c283b2de1598a0271ef9a8");
		param.put("app_act", "efast.sku.stock.get");
		param.put("sd_id", "1");
		param.put("sku", "08010038583");
		String post = HttpUtil45.post(url, param , new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10));
		System.out.println(post);
		System.out.println(post.substring(post.indexOf("sl")+4, post.indexOf(",")));
		// http://218.242.249.190:8081/efast/efast_api/webservice/web/index.php?app_act=efast.items.list.get&page_no=1&app_nick=openapi&app_key=d721b36f22dddf80e7ac&app_secret=bcaa673e76c283b2de1598a0271ef9a8
		
		
	}
}
