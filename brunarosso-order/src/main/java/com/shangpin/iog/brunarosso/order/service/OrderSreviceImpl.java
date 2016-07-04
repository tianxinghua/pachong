package com.shangpin.iog.brunarosso.order.service;

import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub;
import com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermato;
import com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoResponse;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;

public class OrderSreviceImpl extends AbsOrderService{

	private static String supplierId = "";
    private static String supplierNo = "";
    private static Logger logger = Logger.getLogger("info");
    private static LoggerUtil loggerError = LoggerUtil.getLogger("error");
    
    private static ResourceBundle bdl=null;
    
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
		try {
			this.checkoutOrderFromWMS(supplierNo, supplierId, true);
		} catch (Exception e) {
			loggerError.error(e); 
		}		
	}

	/**
	 * 确认支付
	 */
	public void confirmOrder() {
		try {
			this.confirmOrder(supplierId);
		} catch (Exception e) {
			loggerError.error(e); 
		}
		
	}
	
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		
		logger.info("下单成功!");
		orderDTO.setStatus(OrderStatus.PAYED);
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try {
			
			WS_SitoStub wS_SitoStub = new WS_SitoStub();
			OrdineConfermato ordineConfermato = new OrdineConfermato();
			String[] skuId_qty = orderDTO.getDetail().split(",")[0].split(":"); 
			String[] spuId_size = skuId_qty[0].split("-");
			ordineConfermato.setID_ARTICOLO(Long.parseLong(spuId_size[0]));
			ordineConfermato.setTAGLIA(spuId_size[1]);
			ordineConfermato.setQTA(Long.parseLong(skuId_qty[1]));
			OrdineConfermatoResponse response = wS_SitoStub.ordineConfermato(ordineConfermato);
			String result = response.getOrdineConfermatoResult();
			System.out.println(result); 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
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
	
	public static void main(String[] args) {
		OrderSreviceImpl order = new OrderSreviceImpl();
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setDetail("3131784-39:1,");
		order.handleConfirmOrder(orderDTO); 
		
	}
	

}
