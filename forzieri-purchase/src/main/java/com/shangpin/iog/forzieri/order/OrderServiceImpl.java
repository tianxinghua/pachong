package com.shangpin.iog.forzieri.order;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.security.auth.callback.ConfirmationCallback;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.dto.TokenDTO;
import com.shangpin.iog.forzieri.dto.NewAccessToken;
import com.shangpin.iog.forzieri.dto.PushOrderData;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.TokenService;
@Component
public class OrderServiceImpl extends AbsOrderService{
	@Autowired
	SkuPriceService skuPriceService;
	
	@Autowired
	TokenService tokenService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String accessToken = null;
	private static String refreshToken = null;
	private static String clientId = null;
	private static String clientsecret = null;
	private static String time = null;
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("param");
		}
		supplierId = bdl.getString("supplierId");
		clientId = bdl.getString("clientId");
		clientsecret = bdl.getString("clientsecret");
		time = bdl.getString("time");
	}
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		PushOrderData pushOrderData = pushOrder(orderDTO);
		if (pushOrderData.getStatusCode()=="401") {
			logger.info("accessToken过期");
			getAccessToken(refreshToken);
			handleSupplierOrder(orderDTO);
		}else if(pushOrderData.getStatusCode()=="200"){
			//推送订单成功
			 orderDTO.setExcState("0");
			 orderDTO.setSupplierOrderNo(pushOrderData.getData().getOrder_id());
			 orderDTO.setStatus(OrderStatus.PLACED);
		}else if(pushOrderData.getStatusCode()=="400"){
			//推送订单失败
			orderDTO.setExcDesc("订单失败，库存不足"+pushOrderData.getErrorCode());
			handlePurchaseOrderExc(orderDTO);
		}
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try {
			PushOrderData pushOrderData = confirmOrCancelOrder(orderDTO.getSupplierOrderNo(), "approved");
			if (pushOrderData.getStatusCode()=="401") {
				logger.info("付款确认订单时accessToken过期");
				getAccessToken(refreshToken);
				handleConfirmOrder(orderDTO);
			}else if(pushOrderData.getStatusCode()=="200"){
				//确认订单成功
				 orderDTO.setExcState("0");
				 orderDTO.setStatus(OrderStatus.CONFIRMED);
			}else {
				//确认订单失败
				orderDTO.setExcDesc("确认订单失败"+pushOrderData.getErrorCode());
				handlePurchaseOrderExc(orderDTO);
			}
		} catch (Exception e) {
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			e.printStackTrace();
		}
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		try {
			PushOrderData pushOrderData = confirmOrCancelOrder(deleteOrder.getSupplierOrderNo(), "cancelled");
			if (pushOrderData.getStatusCode()=="401") {
				logger.info("取消订单时accessToken过期");
				getAccessToken(refreshToken);
				handleCancelOrder(deleteOrder);
			}else if(pushOrderData.getStatusCode()=="200"){
				//取消订单成功
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.CANCELLED);
			}else {
				//取消订单失败
				logger.info("取消订单失败");
				deleteOrder.setExcDesc("取消订单失败"+pushOrderData.getErrorCode());
				deleteOrder.setExcState("1");
			}
		} catch (Exception e) {
			logger.info("取消订单失败");
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcState("1");
			e.printStackTrace();
		}
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	
	//推送订单，返回状态
	public PushOrderData pushOrder(OrderDTO orderDTO){
		TokenDTO tokenDTO = null;
		HttpResponse response = null;
		PushOrderData pushOrderData = null;
		Gson gson = new Gson();
		try {
			tokenDTO = tokenService.findToken(supplierId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		accessToken = tokenDTO.getAccessToken();
		refreshToken = tokenDTO.getRefreshToken();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost("https://api.forzieri.com/test/orders");
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
         try {
        	 response = httpClient.execute(httpPost);
        	 pushOrderData = gson.fromJson(EntityUtils.toString(response.getEntity()), PushOrderData.class);
        	 pushOrderData.setStatusCode(String.valueOf(response.getStatusLine().getStatusCode()));
        } catch (Exception e) {
			e.printStackTrace();
		}
		return pushOrderData;
	}
	//确认或取消订单
	public PushOrderData confirmOrCancelOrder(String orderNo,String oper){
		TokenDTO tokenDTO = null;
		HttpResponse response = null;
		PushOrderData pushOrderData = null;
		Gson gson = new Gson();
		try {
			tokenDTO = tokenService.findToken(supplierId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		accessToken = tokenDTO.getAccessToken();
		refreshToken = tokenDTO.getRefreshToken();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();

		HttpPut httpPut = new HttpPut("https://api.forzieri.com/test/orders");
        httpPut.setHeader("Authorization", "Bearer "+accessToken);
        httpPut.setHeader("X_HTTP_METHOD_OVERRIDE", "PUT");
        httpPut.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        StringEntity entity = new StringEntity("{\"id\":\""+orderNo+"\",\"status\":\""+oper+"\"}","utf-8");
        entity.setContentType("application/json");
        httpPut.setEntity(entity);
		try {
        	 response = httpClient.execute(httpPut);
        	 pushOrderData = gson.fromJson(EntityUtils.toString(response.getEntity()), PushOrderData.class);
        	 pushOrderData.setStatusCode(String.valueOf(response.getStatusLine().getStatusCode()));
        } catch (Exception e) {
			e.printStackTrace();
		}
		return pushOrderData;
	}
	// 刷新token
	public void getAccessToken(String refresh){

		TokenDTO tokenDTO = new TokenDTO();
		Gson gson = new Gson();
		HttpClient httpClient = new HttpClient();
//		PostMethod postMethod = new PostMethod("https://api.forzieri.com/v2/oauth/token");
		PostMethod postMethod = new PostMethod("https://api.forzieri.com/test/token");
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
			e.printStackTrace();
		}
	}
	//采购异常处理
	private void handlePurchaseOrderExc(OrderDTO orderDTO) {
		String result = setPurchaseOrderExc(orderDTO);
		if("-1".equals(result)){
			orderDTO.setStatus(OrderStatus.NOHANDLE);
		}else if("1".equals(result)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
		}else if("0".equals(result)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
		}
		orderDTO.setExcState("1");
	}
	public static void main(String[] args) {
		//submit order
		Gson gson = new Gson();
		HttpResponse response = null;
		PushOrderData fromJson = null;
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost("https://api.forzieri.com/test/orders");
		httpPost.setHeader("Authorization", "Bearer 75c8f6e0a4411a551a5f388e88695317b41baf13");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		String sku = "fz181110-001-00";
		String qty = "1";
		String client_sku = "asdads";
		String client_order_id = "asdasdads";
		StringEntity entity = new StringEntity("{\"items\":[{\"sku\":\""+sku+"\",\"qty\":\""+qty+"\",\"client_sku\":\""+client_sku+"\"}],\"client_order_id\":\""+client_order_id +"\"}","utf-8");
		 entity.setContentEncoding("UTF-8");    
         entity.setContentType("application/json");
         httpPost.setEntity(entity);
         try {
        	 response = httpClient.execute(httpPost);
//        	 fromJson = gson.fromJson(EntityUtils.toString(response.getEntity()), PushOrderData.class);
        	 System.out.println(response.getStatusLine().getStatusCode());
        } catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
			e.printStackTrace();
		}
         // approve order
         HttpPut httpPut = new HttpPut("https://api.forzieri.com/test/orders");
         httpPut.setHeader("Authorization", "Bearer 75c8f6e0a4411a551a5f388e88695317b41baf13");
         httpPut.setHeader("X_HTTP_METHOD_OVERRIDE", "PUT");
         httpPut.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
         StringEntity entity1 = new StringEntity("{\"id\":\""+fromJson.getData().getOrder_id()+"\",\"status\":\"approved\"}","utf-8");
         entity1.setContentType("application/json");
         httpPut.setEntity(entity1);
        try {
        	 response = httpClient.execute(httpPut);
        	 System.out.println(EntityUtils.toString(response.getEntity()));
        	 System.out.println(String.valueOf(response.getStatusLine().getStatusCode()));
        } catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
