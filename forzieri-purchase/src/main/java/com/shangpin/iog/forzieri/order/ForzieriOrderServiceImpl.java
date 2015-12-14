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
import org.apache.http.ParseException;
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
public class ForzieriOrderServiceImpl extends AbsOrderService{
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
	@Override
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
				 orderDTO.setStatus(OrderStatus.PLACED);
			}else if(pushOrderData.getStatusCode().equals("400")){
				//推送订单失败
				orderDTO.setExcDesc("订单失败，库存不足"+pushOrderData.getErrorCode());
				orderDTO.setExcTime(new Date());
				sendMail(orderDTO);
			}
		} catch (Exception e) {
			logger.info("post推送订单失败,orderNo:"+orderDTO.getSupplierOrderNo());
		}
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try {
			PushOrderData pushOrderData = confirmOrCancelOrder(orderDTO.getSupplierOrderNo(), "approved");
			if (pushOrderData.getStatusCode().equals("401")) {
				logger.info("付款确认订单时accessToken过期");
				getAccessToken(refreshToken);
				handleConfirmOrder(orderDTO);
			}else if(pushOrderData.getStatusCode().equals("200")){
				//确认订单成功
				 orderDTO.setExcState("0");
				 orderDTO.setStatus(OrderStatus.CONFIRMED);
			}else {
				//确认订单失败
				orderDTO.setExcDesc("返回状态码不为200确认订单失败"+pushOrderData.getErrorCode());
				orderDTO.setExcTime(new Date());
				handlePurchaseOrderExc(orderDTO);
			}
		} catch (Exception e) {
			logger.info("orderNo"+orderDTO.getSupplierOrderNo()+"网络原因付款失败"+e.getMessage());
			orderDTO.setExcDesc("orderNo"+orderDTO.getSupplierOrderNo()+"网络原因付款失败"+e.getMessage());
			orderDTO.setExcTime(new Date());
			orderDTO.setExcState("1");
			e.printStackTrace();
		}
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		try {
			PushOrderData pushOrderData = confirmOrCancelOrder(deleteOrder.getSupplierOrderNo(), "cancelled");
			if (pushOrderData.getStatusCode().equals("401")) {
				logger.info("取消订单时accessToken过期");
				getAccessToken(refreshToken);
				handleCancelOrder(deleteOrder);
			}else if(pushOrderData.getStatusCode().equals("200")){
				//取消订单成功
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.CANCELLED);
			}else {
				//取消订单失败
				logger.info("取消订单失败");
				deleteOrder.setExcDesc("取消订单失败"+pushOrderData.getErrorCode());
				deleteOrder.setStatus(OrderStatus.CANCELLED);
				deleteOrder.setExcTime(new Date());
				deleteOrder.setExcState("0");
			}
		} catch (Exception e) {
			logger.info("post请求取消订单失败,orderNo:"+deleteOrder.getSupplierOrderNo()+e.getMessage());
			deleteOrder.setExcDesc("post请求取消订单失败,orderNo:"+deleteOrder.getSupplierOrderNo());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
			e.printStackTrace();
		}
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		try {
			PushOrderData pushOrderData = confirmOrCancelOrder(deleteOrder.getSupplierOrderNo(), "cancelled");
			if (pushOrderData.getStatusCode().equals("401")) {
				logger.info("支付后取消订单时accessToken过期");
				getAccessToken(refreshToken);
				handleCancelOrder(deleteOrder);
			}else if(pushOrderData.getStatusCode().equals("200")){
				//退款取消订单成功
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.REFUNDED);
			}else {
				//退款取消订单失败
				logger.info("退款取消订单失败");
				deleteOrder.setExcDesc("退款取消订单失败"+pushOrderData.getErrorCode());
				deleteOrder.setStatus(OrderStatus.REFUNDED);
				deleteOrder.setExcTime(new Date());
				deleteOrder.setExcState("0");
			}
		} catch (Exception e) {
			logger.info("post退款订单失败orderNo:"+deleteOrder.getSupplierOrderNo()+e.getMessage());
			deleteOrder.setExcDesc("post退款订单失败orderNo:"+deleteOrder.getSupplierOrderNo());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
			e.printStackTrace();
		}
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
//		HttpPost httpPost = new HttpPost("https://api.forzieri.com/test/orders");
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
		
//		HttpPost httpPost = new HttpPost("https://api.forzieri.com/test/orders/"+orderNo);
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
//		PostMethod postMethod = new PostMethod("https://api.forzieri.com/test/token");
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
		orderDTO.setExcState("0");
	}
	
	private void sendMail(OrderDTO orderDTO) {
		logger.info("处理采购异常 orderNo:"+orderDTO.getSupplierOrderNo());
		try{
			long tim = 60l;
			//判断有异常的订单如果处理超过两小时，依然没有解决，则把状态置为不处理，并发邮件
			if(DateTimeUtil.getTimeDifference(orderDTO.getCreateTime(),new Date())/(tim*1000*60)>0){ 
				
				String result = setPurchaseOrderExc(orderDTO);
				if("-1".equals(result)){
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}else if("1".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
				}else if("0".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
				}else{
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}
				//超过一天 不需要在做处理 订单状态改为其它状体
				orderDTO.setExcState("0");
			}else{
				orderDTO.setExcState("1");
			}
		}catch(Exception x){
			logger.info("订单超时" + x.getMessage());
		}
		
	}
	public static void main(String[] args) {
		//submit order
		Gson gson = new Gson();
		HttpResponse response = null;
		PushOrderData fromJson = null;
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost("https://api.forzieri.com/test/orders");
		httpPost.setHeader("Authorization", "Bearer b5b455a0b249da5a2882bc3f27714ab34734ccca");
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
        	 fromJson = gson.fromJson(EntityUtils.toString(response.getEntity()), PushOrderData.class);
        	 System.out.println(response.getStatusLine().getStatusCode());
        } catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
			e.printStackTrace();
		}
         // approve order
//         HttpPut httpPut = new HttpPut("https://api.forzieri.com/test/orders");
         HttpPost httpPost2 = new HttpPost("https://api.forzieri.com/test/orders/"+fromJson.getData().getOrder_id());
         httpPost2.setHeader("Authorization", "Bearer ef197db73b29b7e9dde889dd7123b18451176fcc");
         httpPost2.setHeader("X_HTTP_METHOD_OVERRIDE", "PUT");
         httpPost2.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
         StringEntity entity1 = new StringEntity("{\"status\":\"approved\"}","utf-8");
         entity1.setContentType("application/json");
         httpPost2.setEntity(entity1);
        try {
        	 response = httpClient.execute(httpPost2);
        	 System.out.println(EntityUtils.toString(response.getEntity()));
        	 System.out.println(String.valueOf(response.getStatusLine().getStatusCode()));
        } catch (Exception e) {
			e.printStackTrace();
		}



		HttpPost httpPost3 = new HttpPost("https://api.forzieri.com/test/orders/"+fromJson.getData().getOrder_id());
		httpPost3.setHeader("Authorization", "Bearer ef197db73b29b7e9dde889dd7123b18451176fcc");
		httpPost3.setHeader("X_HTTP_METHOD_OVERRIDE", "PUT");
		httpPost3.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		StringEntity entity2 = new StringEntity("{\"status\":\"cancelled\"}","utf-8");
		entity.setContentType("application/json");
		httpPost3.setEntity(entity2);
		try {
			response = httpClient.execute(httpPost3);
			System.out.println(EntityUtils.toString(response.getEntity()));
			System.out.println(String.valueOf(response.getStatusLine().getStatusCode()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
