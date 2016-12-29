package com.shangpin.iog.lungolivigno.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.lungolivigno.dto.Billingcustomer;
import com.shangpin.iog.lungolivigno.dto.LoginDTO;
import com.shangpin.iog.lungolivigno.dto.RequestSaveOrderDTO;
import com.shangpin.iog.lungolivigno.dto.RequestStoreCode;
import com.shangpin.iog.lungolivigno.dto.ResponseCancelOrder;
import com.shangpin.iog.lungolivigno.dto.ResponseSaveOrderDTO;
import com.shangpin.iog.lungolivigno.dto.ResponseStoreCode;
import com.shangpin.iog.lungolivigno.dto.Result;
import com.shangpin.iog.lungolivigno.dto.Rows;
import com.shangpin.iog.lungolivigno.dto.Shippingcustomer;
import com.shangpin.iog.lungolivigno.dto.Sizes;
import com.shangpin.iog.lungolivigno.dto.User;
@Component
public class OrderService extends AbsOrderService{

	private static Logger logger = Logger.getLogger("info");
	private static Logger error = Logger.getLogger("error");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*5, 1000*60 * 5, 1000*60 * 5);	
	private static String sessionId = null;
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String supplierNo = "";
    
    private static String url_login = null;
    private static String url_saveOrder = null;
    private static String user_name = null;
    private static String user_password = null;
    private static String url_cancelOrder = null;
    private static String url_getStock = null;
    
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");  
        url_login = bdl.getString("url_login");
        url_saveOrder = bdl.getString("url_saveOrder");
        user_name = bdl.getString("user_name");
        user_password = bdl.getString("user_password");
        url_cancelOrder = bdl.getString("url_cancelOrder");
        url_getStock = bdl.getString("url_getStock");
        
        sessionId = getSessionId();
    }
	
	/**
	 * 下订单
	 */
	public void loopExecute() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	/**
	 * 确认支付
	 */
	public void confirmOrder() {
		this.confirmOrder(supplierId);
	}
	
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setStatus(OrderStatus.PLACED);		
	}
	
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {

		try{
			//构造下单参数
			//1、获取storecode
			String sku_stock = orderDTO.getDetail().split(",")[0];
			String sku = sku_stock.split(":")[0];
			String stock = sku_stock.split(":")[1];			
			String storecode = getStoreCode(sku.substring(0, sku.indexOf("-")),sku.substring(sku.indexOf("-")+1));
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
							orderDTO.setExcDesc(e1.getMessage());
							orderDTO.setExcState("1");
							orderDTO.setExcTime(new Date());
							e1.printStackTrace();
							error.error(e1);
						}					
					}else{
						orderDTO.setExcDesc(e.getMessage());
						orderDTO.setExcState("1");
						orderDTO.setExcTime(new Date());
						error.error(e);
					}
					
				}
				
			}else{
				orderDTO.setExcDesc("获取sessionId失败");
				orderDTO.setExcState("1");
				orderDTO.setExcTime(new Date());
				error.error("获取sessionId失败，请检查具体原因!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
		}catch(Exception ex){
			orderDTO.setExcDesc(ex.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}
	}
	
	private String getStoreCode(String skuId,String size) throws ServiceMessageException{
		
		String storeCode = null;
		RequestStoreCode requestStoreCode = new RequestStoreCode();
		List<String> sku = new ArrayList<String>();
		sku.add(skuId);
		requestStoreCode.setSku(sku);
		requestStoreCode.setWithDetail(true);
		String requestParam = new Gson().toJson(requestStoreCode);
		String getStockUrl = url_getStock+sessionId;
		logger.info("获取storecode url====="+getStockUrl+" 参数======="+requestParam); 
		String result = null;
		try {
			result = HttpUtil45.operateData("post", "json", getStockUrl, outTimeConf, null, requestParam,"", "");
		} catch (ServiceException e) {
			throw new ServiceMessageException("获取storecode失败,发生异常:"+e.toString());			
		}
		logger.info("获取storecode==========="+result); 
		if(StringUtils.isNotBlank(result) || HttpUtil45.errorResult.equals(result)){ 
			ResponseStoreCode response = new Gson().fromJson(result, ResponseStoreCode.class);			
			//session过期,重新刷新session
			if(response == null || response.getResult().size()==0){
				sessionId = getSessionId();
				getStockUrl = url_getStock+sessionId;
				try {
					result = HttpUtil45.operateData("post", "json", getStockUrl, outTimeConf, null, requestParam,"", "");
					response = new Gson().fromJson(result, ResponseStoreCode.class);
				} catch (ServiceException e1) {
					error.error(e1.toString()); 
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
			logger.info("返回storeCode=========="+storeCode+" found==="+found); 
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
		
		String url_createOrder = url_saveOrder+sessionId;
		logger.info("创建订单url============"+url_createOrder);
		logger.info("创建订单参数==========="+createOrderJsonParam); 
		String result = HttpUtil45.operateData("post", "json", url_createOrder, outTimeConf, null, createOrderJsonParam,"", "");
		logger.info("创建订单返回结果result====="+result);
		ResponseSaveOrderDTO orderResult = new Gson().fromJson(result, ResponseSaveOrderDTO.class);
		String supplierOrderNo = orderResult.getResult();		
		if(StringUtils.isNotBlank(supplierOrderNo) && orderResult.isStatus()){//下单成功
			orderDTO.setSupplierOrderNo(supplierOrderNo.trim());
			orderDTO.setStatus(OrderStatus.CONFIRMED);
			orderDTO.setExcState("0");
		
		}else{//其他都失败
			orderDTO.setExcDesc(orderResult.getErrMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		deleteOrder.setExcState("0");
		deleteOrder.setStatus(OrderStatus.CANCELLED); 
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		try {
			
			String cancleUrl = url_cancelOrder+getSessionId();
			logger.info("退单url============="+cancleUrl);
			String jsonParam = "{\"ID\":\""+deleteOrder.getSupplierOrderNo()+"\"}";
			logger.info("退单参数============"+jsonParam); 
			String result = HttpUtil45.operateData("post", "json", cancleUrl, outTimeConf, null, jsonParam,"", "");
			logger.info("退单返回结果============"+result);
			ResponseCancelOrder responseCancelOrder = new Gson().fromJson(result, ResponseCancelOrder.class);
			if(1 == responseCancelOrder.getResult()){
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.REFUNDED);
			}else{
				deleteOrder.setExcDesc("1");
				deleteOrder.setExcTime(new Date());
				deleteOrder.setExcDesc(responseCancelOrder.getErrMessage()); 
			}
			
		} catch (Exception e) {
			deleteOrder.setExcDesc("1");
			deleteOrder.setExcTime(new Date());
			deleteOrder.setExcDesc(e.getMessage()); 
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
	
	public static String getSessionId(){
		String sessionId = null;
		try{
			OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
			User user = new User();
			user.setUserName(user_name);
			user.setPassword(user_password);			
			String jsonValue = new Gson().toJson(user); 			
			String result = HttpUtil45.operateData("post", "json", url_login, outTimeConf, null, jsonValue, "", "");
			int i = 0;
			while(HttpUtil45.errorResult.equals(result) && i<100){
				result = HttpUtil45.operateData("post", "json", url_login, outTimeConf, null, jsonValue, "", "");
				i++;
			}
			logger.info("login result==="+result);
			LoginDTO LoginDTO = new Gson().fromJson(result, LoginDTO.class);
			sessionId = LoginDTO.getResult();
			
		}catch(Exception ex){
			error.error(ex); 
		}
		return sessionId;
	}
	
	public static void main(String[] args){
		
//		String spOrderId = "1234567-89";
//		if(spOrderId.contains("-")){
//			spOrderId = spOrderId.substring(0,spOrderId.indexOf("-"));
//		}
//		System.out.println(spOrderId);
		
		OrderService order = new OrderService();
//		try {
//			String storcode = order.getStoreCode("708074982162033", "1");
//			System.out.println(storcode); 
//		} catch (ServiceMessageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String storecode = "";
//		try {
//			storecode = order.getStoreCode("708074982162062","TU");
//		} catch (ServiceMessageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(storecode); 
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setDetail("703045002162002-6:1,");
		orderDTO.setSpOrderId("201612175142218");
		orderDTO.setPurchasePriceDetail("693.00");
//		
//		ReturnOrderDTO deleteOrder = new ReturnOrderDTO();
////		deleteOrder.setSpPurchaseNo("CGD20161107675218");
//		deleteOrder.setSpOrderId("201611104650865"); 
//		deleteOrder.setSupplierOrderNo("04650865"); 
//		
//		OrderService order = new OrderService();
////		order.handleSupplierOrder(orderDTO); 
		order.handleConfirmOrder(orderDTO); 
//		order.handleRefundlOrder(deleteOrder); 
	}

}
