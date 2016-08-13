package com.shangpin.iog.monnierfreres.service;

import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
@Component
public class OrderService extends AbsOrderService{

	private static Logger logger = Logger.getLogger("info");
	private static Logger error = Logger.getLogger("error");
	
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String supplierNo = "";
   
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
      
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
		orderDTO.setStatus(OrderStatus.CONFIRMED);				 
	}


	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {		
		deleteOrder.setStatus(OrderStatus.CANCELLED);
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		deleteOrder.setStatus(OrderStatus.REFUNDED); 
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
	
	
	public static void main(String[] args){
//		OrderService order = new OrderService();
//		
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setDetail("12385-52:1,");		
//		
//		ReturnOrderDTO deleteOrder = new ReturnOrderDTO();
//		deleteOrder.setSupplierOrderNo("1606000011");		
		
//		orderDTO.setSpPurchaseNo("CGD2016052601093"); 
//		order.handleSupplierOrder(orderDTO); 
		
//		orderDTO.setSupplierOrderNo("1606000011");
//		order.handleConfirmOrder(orderDTO); 	
		
//		order.handleCancelOrder(deleteOrder);
//		order.handleRefundlOrder(deleteOrder); 
		
//		Map<String,String> headerMap = new HashMap<String,String>();
//		headerMap.put("Authorization", token.getToken_type()+" "+token.getAccess_token());
//		String result = HttpUtil45.get("http://open.1magway.com/test/Order/GetStatusList", outTimeConf, null, headerMap, "", "");
//		System.out.println(result); 
	}

}
