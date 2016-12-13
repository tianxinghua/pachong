package com.shangpin.ep.order.module.orderapiservice.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.Billingcustomer;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.LoginDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.RequestSaveOrderDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.RequestStoreCode;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.ResponseCancelOrder;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.ResponseSaveOrderDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.ResponseStoreCode;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.Result;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.Rows;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.Shippingcustomer;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.Sizes;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno.User;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

@Component("lungolivignoOrderService")
public class LungolivignoOrderService implements IOrderService{

	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    
    
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*5, 1000*60 * 5, 1000*60 * 5);	
	    
    @Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
	}	
	
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {

		String sessionId = getSessionId();
		try{			
			//构造下单参数
			//1、获取storecode
			String sku_stock = orderDTO.getDetail().split(",")[0];
			String sku = sku_stock.split(":")[0];
			String stock = sku_stock.split(":")[1];			
			String storecode = getStoreCode(sessionId, orderDTO,sku.substring(0, sku.indexOf("-")),sku.substring(sku.indexOf("-")+1));
			//2、下单参数
			RequestSaveOrderDTO requestSaveOrderDTO = new RequestSaveOrderDTO();
			String spOrderId = orderDTO.getSpOrderId();
			if(spOrderId.contains("-")){
				spOrderId = spOrderId.substring(0,spOrderId.indexOf("-"));
			}
			requestSaveOrderDTO.setID(spOrderId);
			requestSaveOrderDTO.setOrderDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
			Billingcustomer billingCustomer = new Billingcustomer();
			billingCustomer.setID("4646305");
			billingCustomer.setFirstName("Cindy");
			billingCustomer.setLastName("Chan");
			billingCustomer.setAddress("Flat 303-309,Hi-Tech Centre,9 Choi Yuen Road,Sheung Shui,N.T.");
			billingCustomer.setZipCode("");
			billingCustomer.setCity("Yoursender International Logistics (HongKong) LMD.CO.");
			billingCustomer.setState("");
			billingCustomer.setCountry("CHINA");
			billingCustomer.setPhone("00852-24249188");
			billingCustomer.setEmail("amanda.lee@shangpin.com");
			billingCustomer.setVatNumber("235865");//增值税税号
			billingCustomer.setFiscalCode(""); //财政代码
			requestSaveOrderDTO.setBillingCustomer(billingCustomer);
			Shippingcustomer Shippingcustomer = new Shippingcustomer();
			Shippingcustomer.setID("4645773");
			Shippingcustomer.setFirstName("Cindy");
			Shippingcustomer.setLastName("Chan");
			Shippingcustomer.setAddress("Flat 303-309,Hi-Tech Centre,9 Choi Yuen Road,Sheung Shui,N.T.");
			Shippingcustomer.setZipCode("");
			Shippingcustomer.setCity("Yoursender International Logistics (HongKong) LMD.CO.");
			Shippingcustomer.setState("");
			Shippingcustomer.setCountry("China");
			Shippingcustomer.setPhone("00852-24249188");
			Shippingcustomer.setEmail("amanda.lee@shangpin.com");
			Shippingcustomer.setVatNumber("235865");//增值税税号
			Shippingcustomer.setFiscalCode(""); //财政代码
			requestSaveOrderDTO.setShippingCustomer(Shippingcustomer);
			List<Rows> rows = new ArrayList<Rows>();
			Rows row = new Rows();
			row.setSku(sku.substring(0, sku.indexOf("-")));
			row.setSizeIndex(sku.substring(sku.indexOf("-")+1)); 
			row.setQty(Integer.parseInt(stock));
			row.setPrice(Double.valueOf(orderDTO.getPurchasePriceDetail())); 
			row.setFinalPrice(Double.valueOf(orderDTO.getPurchasePriceDetail())); 
			row.setPickStoreCode(storecode); 
			rows.add(row);
			requestSaveOrderDTO.setRows(rows);	
			requestSaveOrderDTO.setPaymentMode("monthly payment");
			String createOrderStr = new Gson().toJson(requestSaveOrderDTO);
			if(StringUtils.isNotBlank(sessionId)){			
				try{
					createOrder(orderDTO,createOrderStr,sessionId);				
				}catch(Exception e){
					
					if(e.getMessage().equals("状态码:401")){//sessionId 过期
						sessionId = getSessionId();
						try {
							createOrder(orderDTO,createOrderStr,sessionId);
						} catch (ServiceException e1) {	
							orderDTO.setDescription(e1.getMessage());
							orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
							orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);	
							orderDTO.setLogContent(e1.toString());
							logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);							
							
						}					
					}else{
						orderDTO.setDescription(e.getMessage());
						orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
						orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);	
						orderDTO.setLogContent(e.toString());
						logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);		
					}
					
				}
				
			}else{
				orderDTO.setDescription("获取sessionId失败");
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);	
				orderDTO.setLogContent("获取sessionId失败，请检查具体原因!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			}
		}catch(Exception e){
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
	}
	
	private String getStoreCode(String sessionId, OrderDTO orderDTO,String skuId,String size) throws ServiceMessageException{
		
		String storeCode = null;
		RequestStoreCode requestStoreCode = new RequestStoreCode();
		List<String> sku = new ArrayList<String>();
		sku.add(skuId);
		requestStoreCode.setSku(sku);
		requestStoreCode.setWithDetail(true);
		String requestParam = new Gson().toJson(requestStoreCode);
		String getStockUrl = supplierProperties.getLungolivigno().getUrl_getStock()+sessionId;
		orderDTO.setLogContent("获取storecode url====="+getStockUrl+" 参数======="+requestParam); 
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		String result = null;
		try {
			result = HttpUtil45.operateData("post", "json", getStockUrl, outTimeConf, null, requestParam,null,"", "");
		} catch (ServiceException e) {
			throw new ServiceMessageException("获取storecode失败,发生异常:"+e.toString());			
		}
		orderDTO.setLogContent("获取storecode==========="+result); 
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		if(StringUtils.isNotBlank(result) || HttpUtil45.errorResult.equals(result)){ 
			ResponseStoreCode response = new Gson().fromJson(result, ResponseStoreCode.class);			
			//session过期,重新刷新session
			if(response == null || response.getResult().size()==0){
				sessionId = getSessionId();
				getStockUrl = supplierProperties.getLungolivigno().getUrl_getStock()+sessionId;
				try {
					result = HttpUtil45.operateData("post", "json", getStockUrl, outTimeConf, null, requestParam,null,"", "");
					response = new Gson().fromJson(result, ResponseStoreCode.class);
				} catch (ServiceException e1) {
					orderDTO.setLogContent(e1.toString()); 
					logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
					throw new ServiceMessageException("获取storecode失败,发生异常:"+e1.toString());
				}
			}
			
			boolean found = false;  
			for(int i=0;i<response.getResult().size() && !found;i++){
				Result resultDTO = response.getResult().get(i); 
				for(Sizes sizeDTO : resultDTO.getSizes()){						
					if(Integer.parseInt(size) == sizeDTO.getSizeIndex()){
						if(sizeDTO.getQty()>0){
							storeCode = resultDTO.getStoreCode();
							found = true;
							break;
						}
					}
				}
			}
			orderDTO.setLogContent("返回storeCode=========="+storeCode+" found==="+found); 
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			return storeCode;
			
		}else{
			throw new ServiceMessageException("获取storecode失败,发生异常。");
		}
		
	}
	
	/**
	 * 创建订单
	 * @param orderDTO
	 * @param createOrderStr 下订单所需参数json格式
	 * @param headMap
	 */
	public void createOrder(OrderDTO orderDTO,String createOrderJsonParam,String sessionId) throws ServiceException{
		
		String url_createOrder = supplierProperties.getLungolivigno().getUrl_saveOrder()+sessionId;
		orderDTO.setLogContent("创建订单url============"+url_createOrder);
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		orderDTO.setLogContent("创建订单参数==========="+createOrderJsonParam); 
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		String result = HttpUtil45.operateData("post", "json", url_createOrder, outTimeConf, null, createOrderJsonParam,null,"", "");
		orderDTO.setLogContent("创建订单返回结果result====="+result+" 创建订单参数==========="+createOrderJsonParam);
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		ResponseSaveOrderDTO orderResult = new Gson().fromJson(result, ResponseSaveOrderDTO.class);
		String supplierOrderNo = orderResult.getResult();		
		if(StringUtils.isNotBlank(supplierOrderNo) && orderResult.isStatus()){//下单成功
			orderDTO.setSupplierOrderNo(supplierOrderNo.trim());
			orderDTO.setConfirmTime(new Date()); 
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED); 
		
		}else{//其他都失败
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
			orderDTO.setDescription("下单失败：" + orderResult.getErrMessage());
		}
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date()); 
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API); 
	}

	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		try {
			
			String cancleUrl = supplierProperties.getLungolivigno().getUrl_cancelOrder()+getSessionId();
			deleteOrder.setLogContent("退单url============="+cancleUrl);
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);	
			String jsonParam = "{\"ID\":\""+deleteOrder.getSupplierOrderNo()+"\"}";
			deleteOrder.setLogContent("退单参数============"+jsonParam); 
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);	
			String result = HttpUtil45.operateData("post", "json", cancleUrl, outTimeConf, null, jsonParam,null,"", "");
			deleteOrder.setLogContent("退单返回结果============"+result);
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);	
			ResponseCancelOrder responseCancelOrder = new Gson().fromJson(result, ResponseCancelOrder.class);
			if(1 == responseCancelOrder.getResult()){
				deleteOrder.setRefundTime(new Date());
				deleteOrder.setPushStatus(PushStatus.REFUNDED);
			}else{
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
				deleteOrder.setDescription(responseCancelOrder.getErrMessage());
			}
			
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			handleException.handleException(deleteOrder, e); 
			deleteOrder.setLogContent("退款发生异常============"+e.getMessage());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);		
		}
	}

	
	public String getSessionId(){
		String sessionId = null;
		try{
			OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
			User user = new User();
			user.setUserName(supplierProperties.getLungolivigno().getUser_name());
			user.setPassword(supplierProperties.getLungolivigno().getUser_password());			
			String jsonValue = new Gson().toJson(user); 			
			String result = HttpUtil45.operateData("post", "json", supplierProperties.getLungolivigno().getUrl_login(), outTimeConf, null, jsonValue,null, "", "");
			int i = 0;
			while(HttpUtil45.errorResult.equals(result) && i<100){
				result = HttpUtil45.operateData("post", "json", supplierProperties.getLungolivigno().getUrl_login(), outTimeConf, null, jsonValue,null, "", "");
				i++;
			}
			LogCommon.recordLog("login result==="+result);
			LoginDTO LoginDTO = new Gson().fromJson(result, LoginDTO.class);
			sessionId = LoginDTO.getResult();
			
		}catch(Exception ex){
			LogCommon.recordLog(ex.toString(),LogLeve.ERROR); 
		}
		return sessionId;
	}
	

}
