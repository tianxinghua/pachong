package com.shangpin.iog.ostore.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;

@Component("atelierOServiceImpl") 
public class OrderServiceImpl extends AbsOrderService{
	private static Logger logger = Logger.getLogger("info");
	private static Logger errorLog = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo ;
	private static String url = null;
	private static String user = null;
	private static String password = null;
	private static String createOrder_interface = null;
	private static String setStatus_interface = null;
	private static String getStatus_interface = null;
	
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		url = bdl.getString("url");
		user = bdl.getString("user");
		password = bdl.getString("password");
		createOrder_interface = bdl.getString("createOrder_interface");
		setStatus_interface = bdl.getString("setStatus_interface");
		getStatus_interface = bdl.getString("getStatus_interface");
	}
	
	/**
	 * 下订单
	 */
	public void loopExecute() {
		try {
			this.checkoutOrderFromWMS(supplierNo, supplierId, true);
		} catch (Exception e) {
			errorLog.error(e); 
		}		
	}

	/**
	 * 确认支付
	 */
	public void confirmOrder() {
		try {
			this.confirmOrder(supplierId);
		} catch (Exception e) {
			errorLog.error(e); 
		}
		
	}
	
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		
		orderDTO.setExcState("0");
		orderDTO.setStatus(OrderStatus.PLACED);		
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("ID_ORDER_WEB", orderDTO.getSpOrderId());
			param.put("ID_CUSTOMER_WEB", "1461");			
			param.put("DESTINATIONROW1", "");
			param.put("DESTINATIONROW2", "");
			param.put("DESTINATIONROW3", "");
			String skuId = orderDTO.getDetail().split(",")[0].split(":")[0];
			String barcode = skuId.split("-")[1];
			param.put("BARCODE", barcode);
			String qty = orderDTO.getDetail().split(",")[0].split(":")[1];
			param.put("QTY", qty);
			BigDecimal priceInt = new BigDecimal(orderDTO.getPurchasePriceDetail());
			String price = priceInt.divide(new BigDecimal(1.05),5).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			param.put("PRICE", price);
			
			logger.info("下订单的参数==========="+param.toString()); 
			//下单
			String returnData = HttpUtil45.postAuth(url+createOrder_interface, param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),user,password);
			
			logger.info("返回的结果============"+returnData); 
			if (returnData.contains("OK")) {
				 orderDTO.setExcState("0");				 
				 orderDTO.setStatus(OrderStatus.CONFIRMED);
			} else {
				//推送订单失败
				orderDTO.setExcState("1");
				orderDTO.setExcDesc(returnData);
				orderDTO.setExcTime(new Date()); 				
			}
		} catch (Exception e) {
			orderDTO.setExcDesc("网络原因付款失败"+e.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date()); 
			errorLog.error(e); 
			e.printStackTrace();
		}
		
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		try {
			deleteOrder.setExcState("0");
			deleteOrder.setStatus(OrderStatus.CANCELLED);
			logger.info(deleteOrder.getSpPurchaseNo()+"取消成功================="); 
		} catch (Exception e) {
			errorLog.error(e); 
			deleteOrder.setExcState("1");
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcTime(new Date()); 
		}
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		try {
			deleteOrder.setExcState("0");
			deleteOrder.setStatus(OrderStatus.REFUNDED);
			logger.info(deleteOrder.getSpPurchaseNo()+"退款成功================="); 
		} catch (Exception e) {
			errorLog.error(e); 
			deleteOrder.setExcState("1");
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcTime(new Date()); 
		}
		
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		
	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	
//	//采购异常处理
//	private void handlePurchaseOrderExc(OrderDTO orderDTO) {
//		String result = setPurchaseOrderExc(orderDTO);
//		if("-1".equals(result)){
//			orderDTO.setStatus(OrderStatus.NOHANDLE);
//		}else if("1".equals(result)){
//			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
//		}else if("0".equals(result)){
//			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
//		}
//		orderDTO.setExcState("0");
//	}
//	private void sendMail(OrderDTO orderDTO) {
//		
//		try{
//			long tim = 60l;
//			//判断有异常的订单如果处理超过两小时，依然没有解决，则把状态置为不处理，并发邮件
//			if(DateTimeUtil.getTimeDifference(orderDTO.getCreateTime(),new Date())/(tim*1000*60)>0){ 
//				
//				String result = setPurchaseOrderExc(orderDTO);
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
//			}else{
//				orderDTO.setExcState("1");
//			}
//		}catch(Exception x){
//			logger.info("订单超时" + x.getMessage());
//		}
//	}
//	//TODO 暂时当做付款确认，功能确定后修改
//	public String setStatusOrder(OrderDTO orderDTO,String status){
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("CODICE", orderDTO.getSupplierOrderNo());
//		param.put("ID_CLIENTE", "");
//		param.put("ID_STATUS", status);
//		String returnData = HttpUtil45.post(url+"OrderAmendment", param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
//		return returnData;
//	}
//	//TODO 暂时当取消订单使用，功能明确后修改
//	public String orderAmendment(ReturnOrderDTO deleteOrder){
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("ID_ORDER_WEB", deleteOrder.getSupplierOrderNo());
//		//TODO 参数设置？
//		param.put("ID_CLIENTE_WEB", "");
//		String skuId = deleteOrder.getDetail().split(",")[0].split(":")[0];
//		String barcode = skuId.split("-")[1];
//		param.put("BARCODE", barcode);
//		//TODO 取消订单设置成0
//		String qty = deleteOrder.getDetail().split(",")[0].split(":")[1];
//		param.put("QTY", qty);
//		String returnData = HttpUtil45.post(url+"OrderAmendment", param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
//		return returnData;
//	}
	
	
	
	public void setStatus(String code,String status){
		Map<String,String> param = new HashMap<String,String>();
		param.put("CODE", code);
		param.put("STATUS", status);//NEW PROCESSING SHIPPED CANCELED (for delete ORDER)
		String returnData = HttpUtil45.postAuth(url+setStatus_interface, param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),user,password);
		System.out.println(returnData); 
	}
	
	
	public void getStatus(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("CODE", "201607274082586");
		String returnData = HttpUtil45.postAuth(url+getStatus_interface, param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),user,password);
		System.out.println(returnData); 
	}
	
	public static void main(String[] args) {
		OrderServiceImpl orderService = new OrderServiceImpl();
//		OrderDTO orderDTO = new OrderDTO();
////    	orderDTO.setSpPurchaseNo("CGD2016072500097");
//		orderDTO.setSpOrderId("201607254074169");
//    	orderDTO.setDetail("6794202-2107151150283:1,");
//    	orderDTO.setPurchasePriceDetail("170.41");
//    	orderService.handleConfirmOrder(orderDTO); 
		orderService.getStatus();
	}
	
	
}
