package com.shangpin.iog.facade.dubbo.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.dto.TokenDTO;
import com.shangpin.iog.facade.dubbo.dto.NewAccessToken;
import com.shangpin.iog.facade.dubbo.dto.PushOrderData;
import com.shangpin.iog.service.TokenService;

@Component
public class ForzieriPushOrderServiceImpl{
	@Autowired
	TokenService tokenService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String accessToken = null;
	private static String refreshToken = null;
	private static String clientId = null;
	private static String clientsecret = null;
	private static String tokenurl = null;
	private static String orderurl = null;
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		clientId = bdl.getString("clientId");
		clientsecret = bdl.getString("clientsecret");
		tokenurl = bdl.getString("tokenurl");
		orderurl = bdl.getString("orderurl");
	}
	public void handleSupplierOrder(OrderDTO orderDTO) {
		try {
			PushOrderData pushOrderData = pushOrder(orderDTO);
			System.out.println(pushOrderData.getStatusCode());
			if (pushOrderData.getStatusCode().equals("401")) {
				logger.info("accessToken过期");
				getAccessToken(refreshToken);
				handleSupplierOrder(orderDTO);
			}else if(pushOrderData.getStatusCode().equals("200")){
				//推送订单成功
				 orderDTO.setExcState("0");
				 orderDTO.setSupplierOrderNo(pushOrderData.getData().getOrder_id());
				 orderDTO.setStatus("placed");
			}else if(pushOrderData.getStatusCode().equals("400")){
				//推送订单失败
				orderDTO.setExcDesc("订单失败，库存不足"+pushOrderData.getErrorCode());
				orderDTO.setExcTime(new Date());
			}
		} catch (Exception e) {
			logger.info("post推送订单失败,orderNo:"+orderDTO.getSupplierOrderNo());
		}
	}
	
	//推送订单，返回状态
		public PushOrderData pushOrder(OrderDTO orderDTO) throws Exception{
			TokenDTO tokenDTO = null;
			HttpResponse response = null;
			PushOrderData pushOrderData = null;
			Gson gson = new Gson();
			try {
				tokenDTO = tokenService.findToken(supplierId);
			} catch (SQLException e) {
				logger.info("token未找到");
				e.printStackTrace();
			}
			accessToken = tokenDTO.getAccessToken();
			refreshToken = tokenDTO.getRefreshToken();
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			CloseableHttpClient httpClient = httpClientBuilder.build();
//			HttpPost httpPost = new HttpPost("https://api.forzieri.com/test/orders");
			HttpPost httpPost = new HttpPost(orderurl);
			httpPost.setHeader("Authorization", "Bearer "+accessToken);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			String sku = orderDTO.getDetail().split(",")[0].split(":")[0];
			String qty = orderDTO.getDetail().split(",")[0].split(":")[1];
			String client_sku = orderDTO.getDetail().split(",")[0].split(":")[0];
			String client_order_id = orderDTO.getSpOrderId();
			StringEntity entity = new StringEntity("{\"items\":[{\"sku\":\""+sku+"\",\"qty\":\""+qty+"\",\"client_sku\":\""+client_sku+"\"}],\"client_order_id\":\""+client_order_id +"\"}","utf-8");
			entity.setContentEncoding("UTF-8");    
	        entity.setContentType("application/json");
	        httpPost.setEntity(entity);
	        logger.info("开始推送订单sku："+sku+",client_order_id"+client_order_id);
	        response = httpClient.execute(httpPost);
	        String str = EntityUtils.toString(response.getEntity());
	        logger.info("推送信息sku:"+sku+"qty:"+qty+"client_sku"+client_sku+"client_order_id"+client_order_id+"推送订单返回信息"+str);
	        pushOrderData = gson.fromJson(str, PushOrderData.class);
	        pushOrderData.setStatusCode(String.valueOf(response.getStatusLine().getStatusCode()));
			return pushOrderData;
		}
		//确认或取消订单
		public PushOrderData confirmOrCancelOrder(String orderNo,String oper) throws Exception{
			TokenDTO tokenDTO = null;
			HttpResponse response = null;
			PushOrderData pushOrderData = null;
			Gson gson = new Gson();
			try {
				tokenDTO = tokenService.findToken(supplierId);
			} catch (SQLException e) {
				logger.info("token未找到");
				e.printStackTrace();
			}
			accessToken = tokenDTO.getAccessToken();
			refreshToken = tokenDTO.getRefreshToken();
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			CloseableHttpClient httpClient = httpClientBuilder.build();
			
//			HttpPost httpPost = new HttpPost("https://api.forzieri.com/test/orders/"+orderNo);
			HttpPost httpPost = new HttpPost(orderurl+"/"+orderNo);
	        httpPost.setHeader("Authorization", "Bearer "+accessToken);
	        httpPost.setHeader("X_HTTP_METHOD_OVERRIDE", "PUT");
	        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	        StringEntity entity = new StringEntity("{\"status\":\""+oper+"\"}","utf-8");
	        entity.setContentType("application/json");
	        httpPost.setEntity(entity);
			logger.info(oper+"订单开始，orderNo:"+orderNo);
			response = httpClient.execute(httpPost);
			String str = EntityUtils.toString(response.getEntity());
			logger.info(oper+"订单推送信息orderNo:"+orderNo+",返回信息:"+str);
			pushOrderData = gson.fromJson(str, PushOrderData.class);
			pushOrderData.setStatusCode(String.valueOf(response.getStatusLine().getStatusCode()));
			return pushOrderData;
		}
		// 刷新token
		public void getAccessToken(String refresh){

			TokenDTO tokenDTO = new TokenDTO();
			Gson gson = new Gson();
			HttpClient httpClient = new HttpClient();
			PostMethod postMethod = new PostMethod(tokenurl);
//			PostMethod postMethod = new PostMethod("https://api.forzieri.com/test/token");
			postMethod.addParameter("grant_type", "refresh_token");
			postMethod.addParameter("client_id", clientId);
			postMethod.addParameter("client_secret", clientsecret);
			postMethod.addParameter("refresh_token", refresh);
			try {
				int executeMethod = httpClient.executeMethod(postMethod);
				if (executeMethod==200) {
					
					NewAccessToken newAccessToken = gson.fromJson(postMethod.getResponseBodyAsString(), NewAccessToken.class);
					accessToken = newAccessToken.getAccess_token();
					refreshToken = newAccessToken.getRefresh_token();
					tokenDTO.setSupplierId(supplierId);
					tokenDTO.setAccessToken(accessToken);
					tokenDTO.setRefreshToken(refreshToken);
					tokenDTO.setCreateDate(new Date());
					tokenDTO.setExpireTime(newAccessToken.getExpires_in());
					tokenService.refreshToken(tokenDTO);
				}else {
					logger.info("刷新token失败");
				}
			} catch (Exception e) {
				logger.info("获取token失败");
				e.printStackTrace();
			}
		}
}
