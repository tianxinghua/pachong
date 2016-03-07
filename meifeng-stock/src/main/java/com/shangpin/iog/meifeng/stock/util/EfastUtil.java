package com.shangpin.iog.meifeng.stock.util;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class EfastUtil {
	
	public static String getSDStock(String skuid,String sdid){
//		1,2，7,8
		String url = "http://121.41.160.177/efast/efast_api/webservice/web/index.php";
		Map<String, String> param = new HashMap<String, String>();
		param.put("app_nick", "openapi");
		param.put("app_key", "d721b36f22dddf80e7ac");
		param.put("app_secret", "bcaa673e76c283b2de1598a0271ef9a8");
		param.put("app_act", "efast.sku.stock.get");
		param.put("sd_id",sdid);
		param.put("sku", skuid);
		String post = HttpUtil45.post(url, param , new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10));
		String returnStr = "0";
		try {
			returnStr = post.substring(post.indexOf("sl")+4, post.indexOf(","));
		} catch (Exception e) {
			returnStr = "0";
		}
		return returnStr;
	}
	
	public static String getTotalStock(String skuid){
		String[] sdids = new String[]{"1","2","7","8"};
		int total = 0;
		for (String sdid : sdids) {
			String sdStock = getSDStock(skuid, sdid);
			
			try {
				total+=Integer.valueOf(sdStock);
			} catch (NumberFormatException e) {
				continue;
			}
		}
		return String.valueOf(total);
	}
	public static void main(String[] args) {
		String totalStock = EfastUtil.getTotalStock("014300277040.5");
		System.out.println(totalStock);
	}
}
