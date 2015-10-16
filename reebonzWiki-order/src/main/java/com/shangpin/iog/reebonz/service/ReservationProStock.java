package com.shangpin.iog.reebonz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.reebonz.dto.OAuth;
import com.shangpin.iog.reebonz.dto.Order;
import com.shangpin.iog.reebonz.dto.RequestObject;
import com.shangpin.iog.reebonz.dto.ResponseObject;
import com.shangpin.iog.reebonz.util.MyJsonUtil;

public class ReservationProStock {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String lockStockUrl = null;
	private static String unlockStockUrl = null;
	private static String pushOrderUrl = null;
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
		unlockStockUrl = bdl.getString("unlockStockUrl");
		pushOrderUrl = bdl.getString("pushOrderUrl");
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
			returnMap.put("0", obj.getReservation_id());
			logger.info("锁库存success");
		} else {
			logger.info("锁库存失败" + obj.getError_msg());
			returnMap.put("1", obj.getError_msg());
		}
		return returnMap;
	}

	/*
	 * 推送订单
	 */
	public Map<String, String> pushOrder(String reservationId, String order_id,
			String purchaseNo, String data) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("email", "reebonz@shangpin.com");
		map.put("order_site", "shangpin");
		map.put("eps_order_id", "SP_ID:" + order_id + ",SP_PO:" + purchaseNo);
		map.put("delivery_first_name", "shangpin");
		map.put("delivery_last_name", "shangpin");
		map.put("delivery_street1", "shangpin");
		map.put("delivery_street2", "shangpin");
		map.put("delivery_country_code", "1");
		map.put("delivery_city", "china");
		map.put("delivery_postal_code", "100000");
		map.put("delivery_phone", "1");
		map.put("billing_first_name", "shangpin");
		map.put("billing_last_name", "shangpin");
		map.put("billing_street1", "shangpin");
		map.put("billing_street2", "shangpin");
		map.put("billing_country_code", "1");
		map.put("billing_city", "china");
		map.put("billing_postal_code", "100000");
		map.put("billing_phone", "1");
		map.put("payment_method", "xx");
		map.put("reservation_id", reservationId);
		map.put("bags", data);
		map.put("api_url", pushOrderUrl);
		ResponseObject returnObj = requestSource(map);
		Map<String, String> returnMap = new HashMap<String, String>();
		if (returnObj != null) {
			String result = returnObj.getReturn_code();
			if ("1".equals(result)) {
				returnMap.put("0", returnObj.getOrder_id());
				returnMap.put("return_orderID",returnObj.getOrder_id());
				logger.info("推送订单success：" + returnObj.getOrder_id());
			} else if ("0".equals(result)) {
				returnMap.put("1", returnObj.getError_msg());
				logger.info("推送订单fail：" + returnObj.getError_msg());
			}
		}
		return returnMap;
	}

	/*
	 * 解库存锁
	 */
	public Map<String, String> unlockStock(String reservation_id,
			String order_id, String user_id, String confirmation_code) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("reservation_id", reservation_id);
		map.put("order_id", order_id);
		map.put("user_id", user_id);
		map.put("confirmation_code", confirmation_code);
		map.put("api_url", unlockStockUrl);

		// 请求url
		ResponseObject obj = requestSource(map);
		Map<String, String> returnMap = new HashMap<String, String>();
		if ("1".equals(obj.getReturn_code())) {
			logger.info("解锁success");
		} else {
			returnMap.put("1", obj.getError_msg());
			logger.info("锁库存fail:" + obj.getError_msg());
		}
		return returnMap;
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
			if(HttpUtil45.errorResult.equals(json)){
				obj = new ResponseObject();
				obj.setReturn_code("0");
				obj.setError_msg(json);
				return obj;
			}
			obj = new Gson().fromJson(json, ResponseObject.class);
			if ("INV_TOKEN".equals(obj.getError_code())) {
				logger.info("无效的token:" + oauth.getAccess_token());
				// token可能已过期，刷新token延长周期
				// 重新请求url资源
				refreshToken();
				requestSource(map);
			}
		} else {
			obj = new ResponseObject();
			obj.setReturn_code("0");
			obj.setError_msg(oauth.getError_msg());
		}
		return obj;
	}

}
