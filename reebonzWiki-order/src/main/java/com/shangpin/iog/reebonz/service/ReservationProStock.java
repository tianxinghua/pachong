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
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.reebonz.dto.OAuth;
import com.shangpin.iog.reebonz.dto.Order;
import com.shangpin.iog.reebonz.dto.RequestObject;
import com.shangpin.iog.reebonz.dto.ResponseObject;
import com.shangpin.iog.reebonz.util.MyJsonUtil;

public class ReservationProStock {

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static Logger logMongo = Logger.getLogger("mongodb");
	private static ResourceBundle bdl = null;
	private static String lockStockUrl = "http://ladon.sit.titan.reebonz-dev.com/api/eps_product_reservation";
	private static String unlockStockUrl = "http://ladon.sit.titan.reebonz-dev.com/api/eps_product_reservation_confirmation";
	private static String pushOrderUrl = "http://www.reebonz-dev.com/eps/api/order";
	private static String access_token = null;
	private static String refresh_token = null;
	private static String client_id = "qo+cOYWsSfWi8WxFKo66bA==";
	private static String client_secret = "pIJ+ch5ZFnVd1ckrLoNl6Fbf6FtqK1bDOciwq4UYOC8OhY9AnzpyqU8Q1tbu9VBcdTu+PRgR81cgLE1lGoglFQ==";
	private static String username = "uat_client_3";
	private static String password = "ZeNsTH3iAUoz5wjrtqGBIQ==";
	private static OAuth oauth = null;
	 static {
	 if(null==bdl)
		 bdl=ResourceBundle.getBundle("conf");
		 lockStockUrl = bdl.getString("eventUrl");
		 unlockStockUrl = bdl.getString("productUrl");
		 access_token = bdl.getString("access_token");
		 refresh_token = bdl.getString("refresh_token");
		 client_id = bdl.getString("client_id");
		 client_secret = bdl.getString("client_secret");
		 pushOrderUrl = bdl.getString("pushOrderUrl");
	 }

	/*
	 * 锁库存
	 */
	public static void lockStock(String order_id, String order_site, String data)
			throws ServiceException {

		oauth = authApi();
		// ---------------------准备的临时数据start-------------------------------
		order_id = "987654321";
		order_site = "shangpin";

		List<RequestObject> list = new ArrayList<RequestObject>();
		RequestObject obj1 = new RequestObject();
		obj1.setEvent_id("250");
		obj1.setSku("A62002T620593905");
		obj1.setOption_code("");
		obj1.setQty("1");
		list.add(obj1);
		JSONArray array = JSONArray.fromObject(list);
		// ---------------------准备的临时数据end-------------------------------

		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", order_id);
		map.put("order_site", order_site);
		map.put("data", array.toString());
		map.put("api_url", lockStockUrl);
		// 请求url
		ResponseObject obj = requestSource(map);
		String reservationId = null;
		if ("1".equals(obj.getReturn_code())) {
			reservationId = obj.getReservation_id();
			System.out.println("锁库存success:" + reservationId);
			//推送订单
			pushOrder(reservationId);
		} else {
			System.out.println("锁库存fail:" + obj.getError_msg());
		}

	}

	/*
	 * 推送订单
	 */
	public static void pushOrder(String reservationId) {

		/*---------------------准备的临时数据end-------------------------------*/
//		reservationId = "2564";
		List<RequestObject> list = new ArrayList<RequestObject>();
		RequestObject obj = new RequestObject();
		obj.setEvent_id("1000");
		obj.setSku("XXM0GW05470RE0C407");
		obj.setOption_code("SH2090");
		obj.setQty("1");
		list.add(obj);
		JSONArray array = JSONArray.fromObject(list);
		/*---------------------准备的临时数据end-------------------------------*/

		Map<String, String> map = new HashMap<String, String>();
		map.put("email", "reebonz@shangpin.com");
		map.put("order_site", "shangpin");
		map.put("eps_order_id", "SHANGPIN:123456");
		map.put("delivery_first_name", "1");
		map.put("delivery_last_name", "2");
		map.put("delivery_street1", "3");
		map.put("delivery_street2", "4");
		map.put("delivery_country_code", "5");
		map.put("delivery_city", "6");
		map.put("delivery_postal_code", "7");
		map.put("delivery_phone", "8");
		map.put("billing_first_name", "9");
		map.put("billing_last_name", "10");
		map.put("billing_street1", "11");
		map.put("billing_street2", "12");
		map.put("billing_country_code", "13");
		map.put("billing_city", "14");
		map.put("billing_postal_code", "15");
		map.put("billing_phone", "16");
		map.put("payment_method", "17");
		map.put("reservation_id", reservationId);
		map.put("bags", array.toString());

		map.put("api_url", pushOrderUrl);
		ResponseObject returnObj = requestSource(map);

		if (returnObj != null) {
			String result = returnObj.getReturn_code();
			if ("1".equals(result)) {
				System.out
						.println("推送订单success：" + returnObj.getOrder_id());
				unlockStock("reservationId", "", "","");
			} else if ("0".equals(result)) {
				System.out.println("推送订单fail：" + returnObj.getError_msg());
			}
		}

	}

	/*
	 * 解库存锁 1. reservation_id - Reservation id. (mandatory) 2. order_id - Order
	 * id. (mandatory) 3. user_id - Order id. (optional) 4. confirmation_code -
	 * "deducted"(确认)或 "voided"(放弃)
	 */
	public static void unlockStock(String reservation_id, String order_id,
			String user_id, String confirmation_code){

		/*---------------------准备的临时数据start-------------------------------*/
		reservation_id = "2566";
		order_id = "123456";
		user_id = "123456";
		confirmation_code = "deducted";// voided
		/*---------------------准备的临时数据end-------------------------------*/

		Map<String, String> map = new HashMap<String, String>();
		map.put("reservation_id", reservation_id);
		map.put("order_id", order_id);
		map.put("user_id", user_id);
		map.put("confirmation_code", confirmation_code);
		map.put("api_url", unlockStockUrl);

		// 请求url
		ResponseObject obj = requestSource(map);
		if ("1".equals(obj.getReturn_code())) {
			System.out.println("解锁success");
		} else {
			System.out.println("锁库存fail:" + obj.getError_msg());
		}
	}

	/*
	 * 授权
	 */
	public static OAuth authApi() {
		
		Map<String, String> map = new HashMap<String,String>();
		map.put("client_id",client_id);
		map.put("client_secret", client_secret);
		map.put("username", username);
		map.put("password", password);
		String json = MyJsonUtil.getAccessTokenJson(map);
		OAuth oauth = null;
		OAuth obj = new Gson().fromJson(json, OAuth.class);
		if (obj != null) {
			if ("1".equals(obj.getReturn_code())) {
				oauth = new OAuth();
				oauth.setAccess_token(obj.getAccess_token());
				oauth.setRefresh_token(obj.getRefresh_token());
				System.out.println("授权success");
			} else if ("0".equals(obj.getReturn_code())) {
				System.out.println("授权fail:" + obj.getError_msg());
			}
		}
		return oauth;
	}

	/*
	 * refresh授权
	 */
	public static void refreshToken() {
		Map<String, String> map = new HashMap<String,String>();
	
		map.put("client_id",client_id);
		map.put("client_secret",client_secret);
		map.put("refresh_token", oauth.getRefresh_token());
		String json = MyJsonUtil.refreshTokenJson(map);
		OAuth obj = new Gson().fromJson(json, OAuth.class);
		if (obj != null) {
			if ("1".equals(obj.getReturn_code())) {
				oauth.setAccess_token(obj.getAccess_token());
				oauth.setRefresh_token(obj.getRefresh_token());
				System.out.println("刷新授权success");
			} else if ("0".equals(obj.getReturn_code())) {
				System.out.println("刷新授权fail:" + obj.getError_msg());
			}
		}
	}

	/*
	 * 请求url资源
	 */
	public static ResponseObject requestSource(Map<String, String> map) {

		ResponseObject obj = null;
		// Authentication API 授权
		map.put("client_id", client_id);
		// Request Resource API
		map.put("access_token", oauth.getAccess_token());
		map.put("response_type", "json");
		// 请求API的返回结果
		String json = MyJsonUtil.getRequestSourceJson(map);
		obj = new Gson().fromJson(json, ResponseObject.class);
		if("INV_TOKEN".equals(obj.getError_code())){
			System.out.println("无效的token:"+oauth.getAccess_token());
			refreshToken();
			requestSource(map);
		}
		return obj;
	}

	public static void main(String[] args) throws Exception {
//		 lockStock("","","");
//		unlockStock("", "", "", "");
		// pushOrder(null);
		// refreshToken();
		// authApi();
		// requestSource();
	}

}
