package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.Date;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.google.gson.Gson;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.bean.ReturnOrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa.API;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa.Errors;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa.FailResult;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa.Result;
import com.shangpin.ep.order.util.utils.UUIDGenerator;

@Component("inviqaOrderImpl")
public class InviqaOrderImpl implements IOrderService {
	private static Logger loggerError = Logger.getLogger("error");
	private static Logger logger = Logger.getLogger("info");
	
	
	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    
    private  String MAGENTO_API_KEY = null;
    private  String MAGENTO_API_SECRET = null;
    private  String MAGENTO_REST_API_URL = null;
    private  String token = null;
    private  String secret = null;
    @PostConstruct
    public void init(){
    	MAGENTO_API_KEY = supplierProperties.getInviqaConf().getMagentoApiKey();
	    MAGENTO_API_SECRET = supplierProperties.getInviqaConf().getMagentoApiSecret();
	    MAGENTO_REST_API_URL = supplierProperties.getInviqaConf().getMagentoRestApiUrl();
	    token = supplierProperties.getInviqaConf().getToken();
	    secret = supplierProperties.getInviqaConf().getSecret();
    }
   
    
	/**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		
		try{
			String skuId =  null;
			int qty = 0;
			String detail = orderDTO.getDetail();
			skuId = detail.split(":")[0];
			qty = Integer.parseInt(detail.split(":")[1]);
			
			String json = "{\"order_no\":\""+orderDTO.getSpOrderId()+"\",\"order_items\":[{\"sku_id\":\""+skuId+"\",\"quantity\":"+qty+"}]}";
			System.out.println("推送的数据:"+json);
			logger.info("推送的数据:"+json);
			OAuthService service = new ServiceBuilder().provider(API.class)
					.apiKey(MAGENTO_API_KEY).apiSecret(MAGENTO_API_SECRET).build();
			Token accessToken = new Token(token,
					secret);
			OAuthRequest request = new OAuthRequest(Verb.PUT,
					MAGENTO_REST_API_URL+"holdorders/"+ UUIDGenerator.getUUID(),
					service);
			request.addHeader("Content-type","application/json");
			request.addPayload(json.getBytes()); 
			service.signRequest(accessToken, request);
			Response response = request.send();
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
						orderDTO.setSupplierOrderNo(obj.getId());
						orderDTO.setPushStatus(PushStatus.LOCK_PLACED);
						orderDTO.setLockStockTime(new Date());
					}
				}
			}else if(code==400){
				FailResult fail = new Gson().fromJson(message, FailResult.class);
				Errors error = fail.getMessages().getError().get(0);
				orderDTO.setDescription(error.getMessage());
				orderDTO.setPushStatus(PushStatus.LOCK_PLACED_ERROR);
				//超过一天 不需要在做处理 订单状态改为其它状体
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							System.out.println("email");
//
//							SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"inviqa订单"+orderDTO.getSpOrderId()+"出现错误,已置为不做处理，原因："+orderDTO.getExcDesc(), messageType);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				t.start();

			}else if(code==500){
				orderDTO.setDescription(message);
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
//							SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"inviqa订单"+orderDTO.getSpOrderId()+"出现错误,已置为不做处理，原因："+orderDTO.getExcDesc(), messageType);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
			}
		}catch(Exception ex){
			loggerError.info(ex.getMessage());
		}
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		Response response = null;
		String message = null;
		String json = null;
		try{
			String skuId =  null;
			int qty = 0;
			String detail = orderDTO.getDetail();
			skuId = detail.split(":")[0];
			qty = Integer.parseInt(detail.split(":")[1]);
			
			json = "{\"purchase_no\":\""+orderDTO.getPurchaseNo()+"\",\"order_no\":\""+orderDTO.getSpOrderId()+"\",\"order_items\":[{\"sku_id\":\""+skuId+"\",\"quantity\":"+qty+"}]}";
			logger.info("推送的数据:"+json);
			OAuthService service = new ServiceBuilder().provider(API.class)
					.apiKey(MAGENTO_API_KEY).apiSecret(MAGENTO_API_SECRET).build();
			Token accessToken = new Token(token,
					secret);
			OAuthRequest request = new OAuthRequest(Verb.PUT,
					MAGENTO_REST_API_URL+"orders/"+orderDTO.getSupplierOrderNo(),
					service);
			
			request.addHeader("Content-type","application/json");
			request.addPayload(json.getBytes());
			service.signRequest(accessToken, request);
			response = request.send();
			System.out.println("responseCode："+response.getCode());
			System.out.println("responseMessage："+response.getMessage());
			System.out.println("responseMessage："+response.getBody());
			logger.info("responseCode："+response.getCode());
			logger.info("responseMessage："+response.getMessage());
			logger.info("responseBody："+response.getBody());
			int code = response.getCode();
			message = response.getBody();
			Result obj = new Gson().fromJson(message, Result.class);
			orderDTO.setConfirmTime(new Date());
			if(code==200){
				if(obj!=null){
					if("processing".equals(obj.getStatus().toLowerCase())){
						orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
					}
				}
			}else if(code==400){
				FailResult fail = new Gson().fromJson(message, FailResult.class);
				Errors error = fail.getMessages().getError().get(0);
				orderDTO.setDescription(error.getMessage());
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.API_ERROR);
			}else if(code==500){
				FailResult fail = new Gson().fromJson(message, FailResult.class);
				Errors error = fail.getMessages().getError().get(0);
				orderDTO.setDescription(error.getMessage());
				orderDTO.setErrorType(ErrorStatus.API_ERROR);
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			}else{
				orderDTO.setErrorType(ErrorStatus.API_ERROR);
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			}
		}catch(Exception ex){
			orderDTO.setDescription(ex.getMessage());
			orderDTO.setErrorType(ErrorStatus.API_ERROR);
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
		}
	}
//	private void setPurchaseExc(final OrderDTO orderDTO){
//		String result = orderService.setPurchaseOrderExc(orderDTO);
//		if("-1".equals(result)){
//			orderDTO.setStatus(OrderStatus.NOHANDLE);
//		}else if("1".equals(result)){
//			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
//		}else if("0".equals(result)){
//			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
//		}
//
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"spinnaker 采购单"+orderDTO.getSpPurchaseNo()+"已弃单.状态是:"+orderDTO.getStatus(), messageType);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		t.start();
//
//	}

	/**
	 * 解除库存锁
	 */
	@Override
	public void handleCancelOrder( OrderDTO deleteOrder) {
		deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED);
	}

	/**
	 * 退款
	 */
	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		String skuId =  null;
		int qty = 0;
		String detail = deleteOrder.getDetail();
		skuId = detail.split(":")[0];
		qty = Integer.parseInt(detail.split(":")[1]);
//		
		String json = "{\"purchase_no\":\""+deleteOrder.getPurchaseNo()+"\",\"order_no\":\""+deleteOrder.getSpOrderId()+"\",\"order_items\":[{\"sku_id\":\""+skuId+"\",\"quantity\":"+qty+"}]}";
		System.out.println("推送的数据:"+json);
		logger.info("推送的数据:"+json);
		OAuthService service = new ServiceBuilder().provider(API.class)
				.apiKey(MAGENTO_API_KEY).apiSecret(MAGENTO_API_SECRET).build();
		Token accessToken = new Token(token,
				secret);		
		OAuthRequest request = new OAuthRequest(Verb.DELETE,
				MAGENTO_REST_API_URL+"orders/"+deleteOrder.getSupplierOrderNo(),
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
					deleteOrder.setPushStatus(PushStatus.REFUNDED);
				}
			}
		}else if(code==400){
			FailResult fail = new Gson().fromJson(message, FailResult.class);
			Errors error = fail.getMessages().getError().get(0);
			deleteOrder.setDescription(error.getMessage());
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
		}else if(code==500){
			deleteOrder.setDescription(obj.getMessage());
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
		}else{
			deleteOrder.setDescription(obj.getMessage());
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
		}
	}

//	private void sendMail(final OrderDTO orderDTO) {
//		
//		try{
//			long tim = Long.parseLong(time);
//			//判断有异常的订单如果处理超过两小时，依然没有解决，则把状态置为不处理，并发邮件
//			if(DateTimeUtil.getTimeDifference(orderDTO.getCreateTime(),new Date())/(tim*1000*60)>0){
//				
//				String result = orderService.setPurchaseOrderExc(orderDTO);
//				if("-1".equals(result)){
//					orderDTO.setStatus(OrderStatus.NOHANDLE);
//				}else if("1".equals(result)){
//					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
//				}else if("0".equals(result)){
//					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
//				}else{
//					orderDTO.setStatus(OrderStatus.NOHANDLE);
//				}
//				//超过一天 不需要在做处理 订单状态改为其它状体
//				orderDTO.setExcState("0");
//				Thread t = new Thread(new Runnable() {
//					@Override
//					public void run() {
//						try {
//							System.out.println("email");
//
//							SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"inviqa订单"+orderDTO.getSpOrderId()+"出现错误,已置为不做处理，原因："+orderDTO.getExcDesc(), messageType);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				});
//				t.start();
//
//			}else{
//				orderDTO.setExcState("1");
//			}
//		}catch(Exception x){
//			logger.info(" 发邮件失败 ：" + x.getMessage());
//			System.out.println(" 发邮件失败 ：" + x.getMessage());
//		}
//		
//		
//	}

}
