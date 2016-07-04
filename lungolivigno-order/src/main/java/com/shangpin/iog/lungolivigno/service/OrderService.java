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
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.lungolivigno.dto.Billingcustomer;
import com.shangpin.iog.lungolivigno.dto.LoginDTO;
import com.shangpin.iog.lungolivigno.dto.RequestSaveOrderDTO;
import com.shangpin.iog.lungolivigno.dto.ResponseSaveOrderDTO;
import com.shangpin.iog.lungolivigno.dto.Rows;
import com.shangpin.iog.lungolivigno.dto.Shippingcustomer;
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
    
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");  
        url_login = bdl.getString("url_login");
        url_saveOrder = bdl.getString("url_saveOrder");
        
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
	
	/**
	 * 创建订单
	 * @param orderDTO
	 * @param createOrderStr 下订单所需参数json格式
	 * @param headMap
	 */
	public void createOrder(OrderDTO orderDTO,String createOrderJsonParam,String sessionId) throws ServiceException{
		
		String url_createOrder = url_saveOrder+sessionId;
		String result = HttpUtil45.operateData("post", "json", url_createOrder, outTimeConf, null, createOrderJsonParam,"", "");
		logger.info("创建订单返回结果result====="+result);
		ResponseSaveOrderDTO orderResult = new Gson().fromJson(result, ResponseSaveOrderDTO.class);
		String status = orderResult.getResult();
//		if(status.equals("0")??){//库存不足
//			orderDTO.setExcDesc(orderResult.getResult());
//			orderDTO.setExcState("1");
//			orderDTO.setExcTime(new Date());
//			//采购异常处理
//			doOrderExc(orderDTO);
//		}else if(status.equals("1")??){//下单成功
//			orderDTO.setSupplierOrderNo(orderResult.getResult());
//			orderDTO.setStatus(OrderStatus.CONFIRMED);
//			orderDTO.setExcState("0");
//		}else{//其他都失败
//			orderDTO.setExcDesc(orderResult.getResult());
//			orderDTO.setExcState("1");
//			orderDTO.setExcTime(new Date());
//		}
	}
	
	/**
	 * 采购异常处理
	 * @param orderDTO
	 */
	public void doOrderExc(OrderDTO orderDTO){
		String reResult = setPurchaseOrderExc(orderDTO);
		if("-1".equals(reResult)){
			orderDTO.setStatus(OrderStatus.NOHANDLE);
		}else if("1".equals(reResult)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
		}else if("0".equals(reResult)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
		}
		orderDTO.setExcState("0");
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {

		try{
			//构造下单参数
			RequestSaveOrderDTO requestSaveOrderDTO = new RequestSaveOrderDTO();
			//ID是啥
			requestSaveOrderDTO.setOrderDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
			Billingcustomer billingCustomer = new Billingcustomer();
			//ID??
			billingCustomer.setFirstName("");
			billingCustomer.setLastName("");
			billingCustomer.setAddress("");
			billingCustomer.setZipCode("");
			billingCustomer.setCity("");
			billingCustomer.setState("");
			billingCustomer.setCountry("");
			billingCustomer.setPhone("");
			billingCustomer.setEmail("");
//			billingCustomer.setVatNumber("");//增值税税号
//			billingCustomer.setFiscalCode(""); //财政代码
			requestSaveOrderDTO.setBillingCustomer(billingCustomer);
			Shippingcustomer Shippingcustomer = new Shippingcustomer();
			//ID??
			Shippingcustomer.setFirstName("");
			Shippingcustomer.setLastName("");
			Shippingcustomer.setAddress("");
			Shippingcustomer.setZipCode("");
			Shippingcustomer.setCity("");
			Shippingcustomer.setState("");
			Shippingcustomer.setCountry("");
			Shippingcustomer.setPhone("");
			Shippingcustomer.setEmail("");
//			Shippingcustomer.setVatNumber("");//增值税税号
//			Shippingcustomer.setFiscalCode(""); //财政代码
			requestSaveOrderDTO.setShippingCustomer(Shippingcustomer);
			List<Rows> rows = new ArrayList<Rows>();
			Rows row = new Rows();								
			String sku_stock = orderDTO.getDetail().split(",")[0];
			String sku = sku_stock.split(":")[0];
			String stock = sku_stock.split(":")[1];
			row.setSku(sku.substring(0, sku.indexOf("-")));
			row.setSizeIndex(sku.substring(sku.indexOf("-")+1)); 
			row.setQty(Integer.parseInt(stock));
//			skuPriceService.getNewSkuPriceBySku(supplierId, sku);
//			row.setPrice(111); 
			rows.add(row);
			requestSaveOrderDTO.setRows(rows);	
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
	
//	public void confirmOrder(OrderDTO orderDTO,Map<String,String> headMap) throws Exception{
	
//	try{
//	Map<String,String> headMap = new HashMap<String,String>();
//	headMap.put("Authorization", token.getToken_type()+" "+token.getAccess_token());		
//	try{
//		confirmOrder(orderDTO,headMap);
//	}catch(Exception ex){
//		if(ex.getMessage().equals("状态码:401")){
//			token = getToken();
//			headMap.put("Authorization", token.getToken_type()+" "+token.getAccess_token());
//			try{
//				confirmOrder(orderDTO,headMap);
//			}catch(Exception e){
//				error.error(e);
//				orderDTO.setExcDesc(e.getMessage());
//				orderDTO.setExcState("1");
//				orderDTO.setExcTime(new Date());
//			}				
//		}else{
//			error.error(ex);
//			orderDTO.setExcDesc(ex.getMessage());
//			orderDTO.setExcState("1");
//			orderDTO.setExcTime(new Date());
//		}
//	}
//}catch(Exception ex){
//	error.error(ex);
//	orderDTO.setExcDesc(ex.getMessage());
//	orderDTO.setExcState("1");
//	orderDTO.setExcTime(new Date());
//}
	
//		String uri = url_getOrderConfirmState+orderDTO.getSupplierOrderNo();
//		logger.info("confirm uri====="+uri);
//		String confirmResult = HttpUtil45.get(uri, outTimeConf, null, headMap, "", "");
//		logger.info("confirm result====="+confirmResult);
//		ConfirmResult confirm = new Gson().fromJson(confirmResult, ConfirmResult.class);
//		String confirmStr = confirm.getData().getConfirmedType();
//		if(StringUtils.isBlank(confirmStr) || confirmStr.equals("待确认")){//可以认为待确认
//			
//		}else if(confirmStr.equals("已部分确认")){
//			orderDTO.setExcDesc(confirmStr);
//			orderDTO.setExcState("1");
//			orderDTO.setExcTime(new Date());
//			//采购异常
//			doOrderExc(orderDTO);
//			
//		}else if(confirmStr.equals("已全部确认")){
//			orderDTO.setStatus(OrderStatus.CONFIRMED);
//			orderDTO.setExcState("0");
//			
//		}else if(confirmStr.equals("已被取消")){
//			orderDTO.setExcDesc(confirmStr);
//			orderDTO.setExcState("1");
//			orderDTO.setExcTime(new Date());
//			//采购异常
//			doOrderExc(orderDTO);
//		}
//		
//		
//	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		
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
			user.setUserName("ll_web");
			user.setPassword("lng.r45h");			
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
	
//	public static void main(String[] args){
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setDetail("12367-UNI:1,");
//		orderDTO.setSupplierOrderNo("1603000267"); 
//		
//		ReturnOrderDTO deleteOrder = new ReturnOrderDTO();
//		deleteOrder.setSupplierOrderNo("1603000267");
//		
//		OrderService order = new OrderService();
////		order.handleSupplierOrder(orderDTO); 
//		order.handleConfirmOrder(orderDTO); 
////		order.handleCancelOrder(deleteOrder); 
//	}

}
