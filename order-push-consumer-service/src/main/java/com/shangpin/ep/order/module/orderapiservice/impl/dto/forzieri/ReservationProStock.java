package com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;

public class ReservationProStock {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String lockStockUrl = null;
	private static String unlockStockUrl = null;
	private static String pushOrderUrl = null;
	private static String client_id = "NTkyZThjNGI2NGY2NTY1";
	private static String client_secret = "3db7c25746f4a11bd8f877b1132dc4a5e739535e";
	private static String username = "shangpin";
	private static String password = "AAWMGirAD9qxG@xuWsTmZKCbfNXJN6A,";
	private static OAuth oauth = null;
//	static {
//		if (null == bdl) {
//			bdl = ResourceBundle.getBundle("conf");
//		}
//		lockStockUrl = bdl.getString("lockStockUrl");
//		unlockStockUrl = bdl.getString("unlockStockUrl");
//		pushOrderUrl = bdl.getString("pushOrderUrl");
//		client_id = bdl.getString("client_id");
//		client_secret = bdl.getString("client_secret");
//		username = bdl.getString("username");
//		password = bdl.getString("password");
//
//		if (oauth == null) {
//			logger.info("商家授权");
//			oauth = authApi();
//		}
//	}
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

	/*
	 * 推送订单
	 */
	public Map<String, String> pushOrder(String reservationId, String order_id,
										 String purchaseNo, String data) {

		Map<String, String> map = getOrder();
		map.put("eps_order_id", "SP_ID:" + order_id + ",SP_PO:" + purchaseNo);
		map.put("reservation_id", reservationId);
		map.put("bags", data);
		System.out.println("推送订单的数据：[eps_order_id:"+map.get("eps_order_id")+",reservationId:"+reservationId+",bags:"+data);
		logger.info("推送订单的数据：[orderId:"+map.get("eps_order_id")+",reservationId:"+reservationId+",data:"+data);
		ResponseObject returnObj = requestSource(map);
		Map<String, String> returnMap = null;
		if (returnObj != null) {
			
			String result = returnObj.getReturn_code();
			if ("1".equals(result)) {
				returnMap = new HashMap<String, String>();
				returnMap.put("0", returnObj.getOrder_id()); 
				returnMap.put("return_orderID",returnObj.getOrder_id());
				logger.info("推送订单success：" + returnObj.getOrder_id());
			} else if ("0".equals(result)) {
				if("INV_RESERVATION_ID".equals(returnObj.getError_code())){
					returnMap = pushOrderAgain(order_id,purchaseNo,data);
				}else if(HttpUtil45.errorResult.equals(returnObj.getError_msg())){
					returnMap = new HashMap<String, String>();
					returnMap.put("-1", returnObj.getError_msg());
					logger.info("推送订单fail：" + returnObj.getError_msg());
				}else if("INV_EPS_ORD_ID".equals(returnObj.getError_code())){
					returnMap = new HashMap<String, String>();
					returnMap.put("2", returnObj.getError_msg()); 
					logger.info("推送订单success：" + returnObj.getOrder_id());
				}else{
					returnMap = new HashMap<String, String>();
					returnMap.put("1", returnObj.getError_msg());
					logger.info("推送订单fail：" + returnObj.getError_msg());
				}
		
			}
		}
		return returnMap;
	}
	public Map<String, String> pushOrderAgain(String order_id,
			 String purchaseNo, String data) {
		Map<String, String> map = getOrder();
		map.put("eps_order_id", "SP_ID:" + order_id + ",SP_PO:" + purchaseNo);
		map.put("reservation_id", null);
		map.put("bags", data);
		System.out.println("实时推送订单的数据：[eps_order_id:"+map.get("eps_order_id")+",reservationId:null"+",bags:"+data);
		logger.info("实时推送订单的数据：[eps_order_id:"+map.get("eps_order_id")+",reservationId:null"+",bags:"+data);
		ResponseObject returnObj = requestSource(map);
		Map<String, String> returnMap = new HashMap<String, String>();
		if (returnObj != null) {
			String result = returnObj.getReturn_code();
			if ("1".equals(result)) {
				returnMap.put("0", returnObj.getOrder_id());
				returnMap.put("return_orderID",returnObj.getOrder_id());
				logger.info("实时推送订单success：" + returnObj.getOrder_id());
			} else if ("0".equals(result)) {
				returnMap.put("1", returnObj.getError_msg());
				logger.info("实时推送订单fail：" + returnObj.getError_msg());
			}
		}
		return returnMap;
	}
	private Map<String, String> getOrder(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", "reebonz@shangpin.com");
		map.put("order_site", "shangpin");
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
		map.put("api_url", pushOrderUrl);
		return map;
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
//	public static void main(String[] args) {
//		ReservationProStock m = new ReservationProStock();
//		m.unlockStock("265","123456","","voided");
//	}
	
	public static void main(String[] args) {
		Map map = new HashMap();
		map.put("Authorization","Bearer 1568010f445ffc910f7b34757ceae04f809d4b91");
	String s =	HttpUtil45.get("https://api.forzieri.com/test/products/az30277-002-00",null,null,map,null,null);
		getToken("cf7c04739bcead47f23baec81e4af78925d3656e");
//		refreshToken("24f3117bba86000472b423bd8228ef76be823194");
	}
	/*
	 * 授权
	 */
	public static OAuth authApi(String code) {
		oauth = new OAuth();
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
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
//				oauth.setError_msg(obj.getError_msg());
//				logger.info("授权fail:" + obj.getError_msg());
			}
		}
		return oauth;
	}/*
	 * 授权
	 */
	public static OAuth getToken(String code) {
		oauth = new OAuth();
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
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
//				oauth.setError_msg(obj.getError_msg());
//				logger.info("授权fail:" + obj.getError_msg());
			}
		}
		return oauth;
	}

	/*
	 * refresh授权
	 */
	public static void refreshToken(String refreshToken) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("refresh_token",refreshToken);
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
				authApi("");
			}
		}
	}

	/*
	 * 请求url资源
	 */
	public ResponseObject requestSource(Map<String, String> map) {

		ResponseObject obj = null;
//		if ("1".equals(oauth.getReturn_code())) {
			map.put("client_id", client_id);
			map.put("access_token", "");
			map.put("code", "json");
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
//				refreshToken();
				obj = requestSource(map);
			}
//		} else {
//			oauth = authApi();
//			obj = new ResponseObject();
//			obj.setReturn_code("0");
//			obj.setError_msg(oauth.getError_msg());
//		}
		return obj;
	}

}