package com.shangpin.iog.reebonz.order;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.facade.dubbo.dto.OAuth;
import com.shangpin.iog.facade.dubbo.dto.ResponseObject;

public class ReservationProStock {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String lockStockUrl = null;
	private static String client_id = null;
	private static String client_secret = null;
	private static String username = null;
	private static String password = null;
	private static OAuth oauth = null;
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
		}
		lockStockUrl = bdl.getString("lockStockUrl");
		client_id = bdl.getString("client_id");
		client_secret = bdl.getString("client_secret");
		username = bdl.getString("username");
		password = bdl.getString("password");
		if (oauth == null) {
			logger.info("商家授权");
			oauth = authApi();
		}
	}
	/*
	 * 锁库存
	 */
	public Map<String, String> lockStock(String order_id, String order_site,
										 String data) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", order_id);
		map.put("order_site", order_site);
		map.put("data", data);
		map.put("api_url", lockStockUrl);
		// 请求url
		ResponseObject obj = requestSource(map);
		Map<String, String> returnMap = new HashMap<String, String>();
		if ("1".equals(obj.getReturn_code())) {
			returnMap.put("1", obj.getReservation_id());
			logger.info("锁库存success");
		} else if("-1".equals(obj.getReturn_code())){
			logger.info("锁库存失败" + obj.getError_msg());
			returnMap.put("0", obj.getError_msg());
		}else if("0".equals(obj.getReturn_code())){
			logger.info("锁库存失败" + obj.getError_msg());
			returnMap.put("0", obj.getError_msg());
		}else {
			logger.info("锁库存失败" + obj.getError_msg());
			returnMap.put("0", obj.getError_msg());
		}
		return returnMap;
	}

	public static void main(String[] args) {
		ReservationProStock m = new ReservationProStock();
	}
	/*
	 * 授权
	 */
	public static OAuth authApi() {
		oauth = new OAuth();
		Map<String, String> map = new HashMap<String, String>();
		map.put("client_id", client_id);
		map.put("client_secret", client_secret);
		map.put("username", username);
		map.put("password", password);
		String json = MyJsonUtil.getAccessTokenJson(map);
		OAuth obj = new Gson().fromJson(json, OAuth.class);
		if (obj != null) {
			if ("1".equals(obj.getReturn_code())) {
				oauth.setReturn_code("1");
				oauth.setAccess_token(obj.getAccess_token());
				oauth.setRefresh_token(obj.getRefresh_token());
				logger.info("授权success");
			} else {
				oauth.setReturn_code("0");
				oauth.setError_msg(obj.getError_msg());
				logger.info("授权fail:" + obj.getError_msg());
			}
		}
		return oauth;
	}

	/*
	 * refresh授权
	 */
	public void refreshToken() {

		Map<String, String> map = new HashMap<String, String>();
		map.put("client_id", client_id);
		map.put("client_secret", client_secret);
		map.put("refresh_token", oauth.getRefresh_token());
		String json = MyJsonUtil.refreshTokenJson(map);
		OAuth obj = new Gson().fromJson(json, OAuth.class);
		if (obj != null) {
			if ("1".equals(obj.getReturn_code())) {
				oauth.setReturn_code("1");
				oauth.setAccess_token(obj.getAccess_token());
				oauth.setRefresh_token(obj.getRefresh_token());
				logger.info("刷新授权success");
			} else if ("INV_REFRESH_TOKEN".equals(obj.getError_code())) {
				logger.info("授权已过期，重新授权");
				System.out.println("授权已过期，重新授权");
				authApi();
			}
		}
	}

	/*
	 * 请求url资源
	 */
	public ResponseObject requestSource(Map<String, String> map) {

		ResponseObject obj = null;
		if ("1".equals(oauth.getReturn_code())) {
			map.put("client_id", client_id);
			map.put("access_token", oauth.getAccess_token());
			map.put("response_type", "json");
			// 请求API的返回结果
			String json = MyJsonUtil.getRequestSourceJson(map);
			logger.info("通过授权请求url的返回结果：" + json);
			if(HttpUtil45.errorResult.equals(json)){
				obj = new ResponseObject();
				obj.setReturn_code("0");
				obj.setError_msg(json);
				return obj;
			}
			obj = new Gson().fromJson(json, ResponseObject.class);
			if ("INV_TOKEN".equals(obj.getError_code())) {
				logger.info("无效的token:" + oauth.getAccess_token());
				System.out.println("无效的token:" + oauth.getAccess_token());
				// token可能已过期，刷新token延长周期
				// 重新请求url资源
				refreshToken();
				obj = requestSource(map);
			}
		} else {
			oauth = authApi();
			obj = new ResponseObject();
			obj.setReturn_code("0");
			obj.setError_msg(oauth.getError_msg());
		}
		return obj;
	}

}
