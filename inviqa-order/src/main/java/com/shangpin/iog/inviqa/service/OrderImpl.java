package com.shangpin.iog.inviqa.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.google.gson.Gson;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.inviqa.dto.API;
import com.shangpin.iog.inviqa.dto.Errors;
import com.shangpin.iog.inviqa.dto.FailResult;
import com.shangpin.iog.inviqa.dto.Result;

@Component
public class OrderImpl extends AbsOrderService {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String toReebonz = null;
	private static String subject = null;
	private static String messageText = null;
	private static String messageType = null;
	private static String time = null;
	private static String reebonzSendEmail;
	private static String MAGENTO_API_KEY = null;
    private static String MAGENTO_API_SECRET = null;
    private static String MAGENTO_REST_API_URL = null;
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		

		MAGENTO_API_KEY = bdl.getString("MAGENTO_API_KEY");
		MAGENTO_API_SECRET = bdl.getString("MAGENTO_API_SECRET");
		MAGENTO_REST_API_URL = bdl.getString("MAGENTO_REST_API_URL");
		
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		smtpHost = bdl.getString("smtpHost");
		from = bdl.getString("from");

		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		subject = bdl.getString("subject");
		messageText = bdl.getString("messageText");
		messageType = bdl.getString("messageType");
		time = bdl.getString("time");

	}

	public void loopExecute() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	public void confirmOrder() {
		this.confirmOrder(supplierId);

	}
	
	
	 private static ApplicationContext factory;
	    private static void loadSpringContext()
	    {
	        factory = new AnnotationConfigApplicationContext(AppContext.class);
	    }

	    public static void main(String[] args)
	    {
	        //加载spring
	        loadSpringContext();
	        logger.info(" schedule start  ");
	        OrderImpl o = new OrderImpl();
			o.handleRefundlOrder(null);
	    }
	/**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(final OrderDTO orderDTO) {
		String skuId =  null;
		int qty = 0;
//		String detail = orderDTO.getDetail();
//		skuId = detail.split(":")[0];
//		qty = Integer.parseInt(detail.split(":")[1]);
		
		 OrderDTO orderDTO1 = new OrderDTO();
		skuId = "JW0010-LIGHT-GOLD-One Size";
		qty = 1;
		orderDTO1.setSpOrderId("9999999");
		
		String json = "{\"order_no\":\""+orderDTO1.getSpOrderId()+"\",\"order_items\":[{\"sku_id\":\""+skuId+"\",\"quantity\":"+qty+"}]}";
		System.out.println("推送的数据:"+json);
		logger.info("推送的数据:"+json);
		OAuthService service = new ServiceBuilder().provider(API.class)
				.apiKey(MAGENTO_API_KEY).apiSecret(MAGENTO_API_SECRET).build();
		Token accessToken = new Token("q5gj1n97vkjwspptteb5nonjidte2j7c",
				"virjysp6wpnwqgio1k2ohbsvds09ti2o");
		OAuthRequest request = new OAuthRequest(Verb.PUT,
				"http://glamorous-staging.space48.com/api/rest/shangpin/holdorders/"+ UUIDGenerator.getUUID(),
				service);
		
		request.addHeader("Content-type","application/json");
		request.addPayload(json.getBytes()); 
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("responseCode："+response.getCode());
		System.out.println("responseMessage："+response.getMessage());
		System.out.println("responseBody："+response.getBody());
		logger.info("responseCode："+response.getCode());
		logger.info("responseMessage："+response.getMessage());
		logger.info("responseBody："+response.getBody());
		int code = response.getCode();
		String message = null;
		message = response.getBody();
		if(code==200){
			Result obj = new Gson().fromJson(message, Result.class); 
			if(obj!=null){
				if("placed".equals(obj.getStatus().toLowerCase())){
					orderDTO.setExcState("0");
					orderDTO.setSupplierOrderNo(obj.getId());
					orderDTO.setStatus(OrderStatus.PLACED);
				}
			}
		}else if(code==400){
			FailResult fail = new Gson().fromJson(message, FailResult.class);
			Errors error = fail.getMessages().getError().get(0);
			orderDTO.setExcDesc(error.getMessage());
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
			Thread t = new Thread(	 new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("email");

						SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"inviqa订单"+orderDTO.getSpOrderId()+"出现错误,已置为不做处理，原因："+orderDTO.getExcDesc(), messageType);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();

		}else if(code==500){
			
			orderDTO.setStatus(OrderStatus.NOHANDLE);
			Thread t = new Thread(	 new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("email");

						SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"inviqa订单"+orderDTO.getSpOrderId()+"出现错误,已置为不做处理，原因："+orderDTO.getExcDesc(), messageType);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}else{
			orderDTO.setStatus(OrderStatus.NOHANDLE);
			orderDTO.setExcState("1");
		}
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		String skuId =  null;
		int qty = 0;
//		String detail = orderDTO.getDetail();
//		skuId = detail.split(":")[0];
//		qty = Integer.parseInt(detail.split(":")[1]);9df6a7e632dd483c87b72b7b0cb128e3
		
		skuId = "JW0010-LIGHT-GOLD-One Size";
		qty = 1;
		orderDTO = new OrderDTO();
		orderDTO.setSpOrderId("9999999");
		orderDTO.setSpPurchaseNo("CGD1234567");
		orderDTO.setSupplierOrderNo("1");
		
		String json = "{\"purchase_no\":\""+orderDTO.getSpPurchaseNo()+"\",\"order_no\":\""+orderDTO.getSpOrderId()+"\",\"order_items\":[{\"sku_id\":\""+skuId+"\",\"quantity\":"+qty+"}]}";
		System.out.println("推送的数据:"+json);
		logger.info("推送的数据:"+json);
		OAuthService service = new ServiceBuilder().provider(API.class)
				.apiKey(MAGENTO_API_KEY).apiSecret(MAGENTO_API_SECRET).build();
		Token accessToken = new Token("q5gj1n97vkjwspptteb5nonjidte2j7c",
				"virjysp6wpnwqgio1k2ohbsvds09ti2o");
		OAuthRequest request = new OAuthRequest(Verb.PUT,
				"http://glamorous-staging.space48.com/api/rest/shangpin/orders/"+orderDTO.getSupplierOrderNo(),
				service);
		
		request.addHeader("Content-type","application/json");
		request.addPayload(json.getBytes());
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("responseCode："+response.getCode());
		System.out.println("responseMessage："+response.getMessage());
		System.out.println("responseMessage："+response.getBody());
		logger.info("responseCode："+response.getCode());
		logger.info("responseMessage："+response.getMessage());
		logger.info("responseBody："+response.getBody());
		int code = response.getCode();
		String message = response.getBody();
		Result obj = new Gson().fromJson(message, Result.class);
		if(code==200){
			if(obj!=null){
				if("processing".equals(obj.getStatus().toLowerCase())){
					orderDTO.setExcState("0");
					orderDTO.setStatus(OrderStatus.CONFIRMED);
				}
			}
		}else if(code==400){
			FailResult fail = new Gson().fromJson(message, FailResult.class);
			Errors error = fail.getMessages().getError().get(0);
			orderDTO.setExcDesc(error.getMessage());
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
			orderDTO.setExcState("0");
		}else if(code==500){
			orderDTO.setExcState("1");
			orderDTO.setStatus(OrderStatus.NOHANDLE);
		}else{
			orderDTO.setExcDesc("");
			orderDTO.setExcState("1");
		}
	}

	/**
	 * 解除库存锁
	 */
	@Override
	public void handleCancelOrder( ReturnOrderDTO deleteOrder) {
		deleteOrder.setStatus(OrderStatus.CANCELLED);
	}

	/**
	 * 退款
	 */
	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		String skuId =  null;
		int qty = 0;
		String detail = deleteOrder.getDetail();
		skuId = detail.split(":")[0];
		qty = Integer.parseInt(detail.split(":")[1]);
		
//		skuId = "JW0010-LIGHT-GOLD-One Size";
//		qty = 1;
//		deleteOrder = new ReturnOrderDTO();
//		deleteOrder.setSpOrderId("9999999");
//		deleteOrder.setSpPurchaseNo("CGD1234567");
//		deleteOrder.setSupplierOrderNo("1");
//		
		String json = "{\"purchase_no\":\""+deleteOrder.getSpPurchaseNo()+"\",\"order_no\":\""+deleteOrder.getSpOrderId()+"\",\"order_items\":[{\"sku_id\":\""+skuId+"\",\"quantity\":"+qty+"}]}";
		System.out.println("推送的数据:"+json);
		logger.info("推送的数据:"+json);
		OAuthService service = new ServiceBuilder().provider(API.class)
				.apiKey(MAGENTO_API_KEY).apiSecret(MAGENTO_API_SECRET).build();
		Token accessToken = new Token("q5gj1n97vkjwspptteb5nonjidte2j7c",
				"virjysp6wpnwqgio1k2ohbsvds09ti2o");
						
		OAuthRequest request = new OAuthRequest(Verb.DELETE,
				"http://glamorous-staging.space48.com/api/rest/shangpin/orders/"+deleteOrder.getSupplierOrderNo(),
				service);
		
		request.addHeader("Content-type","application/json");
		request.addPayload(json.getBytes());
		service.signRequest(accessToken, request);
		
		Response response = request.send();
		System.out.println("responseCode："+response.getCode());
		System.out.println("responseMessage："+response.getMessage());
		System.out.println("responseBody："+response.getBody());
		logger.info("responseCode："+response.getCode());
		logger.info("responseMessage："+response.getMessage());
		logger.info("responseBody："+response.getBody());
		int code = response.getCode();
		String message = response.getBody();
		Result obj = new Gson().fromJson(message, Result.class);
		if(code==200){
			if(obj!=null){
				if("canceled".equals(obj.getStatus().toLowerCase())){
					deleteOrder.setExcState("0");
					deleteOrder.setStatus(OrderStatus.REFUNDED);
				}
			}
		}else if(code==400){
			FailResult fail = new Gson().fromJson(message, FailResult.class);
			Errors error = fail.getMessages().getError().get(0);
			deleteOrder.setExcDesc(error.getMessage());
			deleteOrder.setExcState("1");
		}else if(code==500){
			deleteOrder.setExcState("1");
			deleteOrder.setStatus(OrderStatus.NOHANDLE);
		}else{
			deleteOrder.setExcDesc("");
			deleteOrder.setExcState("1");
		}
	}

	private void sendMail(final OrderDTO orderDTO) {
		
		try{
			long tim = Long.parseLong(time);
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
				Thread t = new Thread(	 new Runnable() {
					@Override
					public void run() {
						try {
							System.out.println("email");

							SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"inviqa订单"+orderDTO.getSpOrderId()+"出现错误,已置为不做处理，原因："+orderDTO.getExcDesc(), messageType);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				t.start();

			}else{
				orderDTO.setExcState("1");
			}
		}catch(Exception x){
			logger.info(" 发邮件失败 ：" + x.getMessage());
			System.out.println(" 发邮件失败 ：" + x.getMessage());
		}
		
		
	}
	@Override
	public void handleEmail(OrderDTO orderDTO) {
		
	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		
	}

}
