package com.shangpin.iog.justFashion.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
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
import com.shangpin.iog.justFashion.wsdl.Orders_v1_0Stub;
import com.shangpin.iog.justFashion.wsdl.Orders_v1_0Stub.ArrayOfOrderDetail;
import com.shangpin.iog.justFashion.wsdl.Orders_v1_0Stub.CreateOrder;
import com.shangpin.iog.justFashion.wsdl.Orders_v1_0Stub.CreateOrderResponse;
import com.shangpin.iog.justFashion.wsdl.Orders_v1_0Stub.OrderDetail;

/**
 * Created by lubaijiang
 */

@Component
public class OrderService extends AbsOrderService{

	private static Logger logger = Logger.getLogger("info");
	private static Logger errorLog = Logger.getLogger("error");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*5, 1000*60 * 5, 1000*60 * 5);
	
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String supplierNo = "";
    
    private static String authKey = null;
    private static String channel = null;
    private static String jssecacerts = null;
       
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
        
        authKey = bdl.getString("authKey");
        channel = bdl.getString("channel");
        jssecacerts = bdl.getString("jssecacerts");
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
			
		try {
			// TODO 锁库存逻辑
			orderDTO.setStatus(OrderStatus.PLACED); 
			
		} catch (Exception e) {
			errorLog.error(e);
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}
		
	}
	
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		try {
			// TODO 支付逻辑
			System.setProperty("javax.net.ssl.trustStore", jssecacerts+File.separator+"jssecacerts");
			Orders_v1_0Stub orders_v1_0Stub = new Orders_v1_0Stub();
			CreateOrder createOrder = new CreateOrder();
			createOrder.setAuthKey(authKey);
			createOrder.setChannel(channel);
			createOrder.setCustomerID("");
			createOrder.setDestCustID("");
			createOrder.setDestinationID(""); 
			createOrder.setRefID(orderDTO.getSpPurchaseNo()); 
			ArrayOfOrderDetail arrayOfOrderDetail =  new ArrayOfOrderDetail();
			OrderDetail[] orderDetail = new OrderDetail[1];
			OrderDetail detail = new OrderDetail();
			detail.setSKU(orderDTO.getDetail().split(",")[0].split(":")[0]);
			detail.setQTY(new BigDecimal(Integer.valueOf(orderDTO.getDetail().split(",")[0].split(":")[1]))); 
			BigDecimal priceInt = new BigDecimal(orderDTO.getPurchasePriceDetail());
			BigDecimal price = priceInt.divide(new BigDecimal(1.05),5).setScale(0, BigDecimal.ROUND_HALF_UP);
			detail.setPRICE(price);
			orderDetail[0] = detail;
			arrayOfOrderDetail.setOrderDetail(orderDetail); 
			createOrder.setOrderDetailsList(arrayOfOrderDetail);
			logger.info("下单参数=================AuthKey:"+createOrder.getAuthKey()+" Channel:"+createOrder.getChannel()+" RefID:"+createOrder.getRefID()+" SKU:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getSKU()
					+" QTY:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getQTY()+" PRICE:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getPRICE()); 
			System.out.println("下单参数=================AuthKey:"+createOrder.getAuthKey()+" Channel:"+createOrder.getChannel()+" RefID:"+createOrder.getRefID()+" SKU:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getSKU()
					+" QTY:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getQTY()+" PRICE:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getPRICE());
			CreateOrderResponse createOrderResponse = orders_v1_0Stub.createOrder(createOrder);
			logger.info("返回结果 ERROR========"+createOrderResponse.getCreateOrderResult().getError()
					+"  MessageTxt======"+createOrderResponse.getCreateOrderResult().getMessageTxt()
					+"  ErrorTxt======"+createOrderResponse.getCreateOrderResult().getErrorTxt());
			System.out.println("返回结果 ERROR========"+createOrderResponse.getCreateOrderResult().getError()
					+"  MessageTxt======"+createOrderResponse.getCreateOrderResult().getMessageTxt()
					+"  ErrorTxt======"+createOrderResponse.getCreateOrderResult().getErrorTxt()); 
			if(createOrderResponse.getCreateOrderResult().getError() == 0){
				orderDTO.setSupplierOrderNo(createOrderResponse.getCreateOrderResult().getMessageTxt());
				orderDTO.setExcState("0");
				orderDTO.setStatus(OrderStatus.CONFIRMED); 
			}else if("Items in the returned list are not found available on stock.".equals(createOrderResponse.getCreateOrderResult().getErrorTxt())){
				orderDTO.setExcState("0");
				orderDTO.setExcTime(new Date()); 
				orderDTO.setStatus(OrderStatus.SHOULD_PURCHASE_EXP);
				orderDTO.setExcDesc(createOrderResponse.getCreateOrderResult().getErrorTxt());
			}else{
				orderDTO.setExcState("1");
				orderDTO.setExcTime(new Date()); 
				orderDTO.setExcDesc(createOrderResponse.getCreateOrderResult().getErrorTxt());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errorLog.error(e);
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}
				
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		
		try {
			// TODO 退单逻辑
			deleteOrder.setExcState("0");
			deleteOrder.setStatus(OrderStatus.CANCELLED); 
			
		} catch (Exception e) {
			errorLog.error(e);
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
		}
				
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		
		try {
			// TODO 退款逻辑
			deleteOrder.setExcState("0");
			deleteOrder.setStatus(OrderStatus.REFUNDED); 
			
		} catch (Exception e) {
			errorLog.error(e);
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
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
	
	public static void main(String[] args) {
		OrderService orderService = new OrderService();
		OrderDTO orderDTO = new OrderDTO();
		//CGD2016082400193 
		orderDTO.setSpPurchaseNo("CGD2016082900003");
		orderDTO.setDetail("I1W8182#F048700#I765######46:1,");
		orderDTO.setPurchasePriceDetail("78.49");
		orderService.handleConfirmOrder(orderDTO); 
	}

}
