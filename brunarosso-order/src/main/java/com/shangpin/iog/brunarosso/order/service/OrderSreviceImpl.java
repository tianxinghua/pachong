package com.shangpin.iog.brunarosso.order.service;

import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub;
import com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermato;
import com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoCliente;
import com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoClienteResponse;
import com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoResponse;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;

@Component
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
		
		orderDTO.setStatus(OrderStatus.PLACED);
		logger.info("下单成功!");
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		try {
			
			WS_SitoStub wS_SitoStub = new WS_SitoStub();
			OrdineConfermatoCliente ordineConfermato = new OrdineConfermatoCliente();
			String[] skuId_qty = orderDTO.getDetail().split(",")[0].split(":"); 
			String[] spuId_size = skuId_qty[0].split("-");
			ordineConfermato.setID_ARTICOLO(Long.parseLong(spuId_size[0]));//spuId
			ordineConfermato.setTAGLIA(spuId_size[1].replaceAll("\\+", "½"));//尺码 
			ordineConfermato.setQTA(Long.parseLong(skuId_qty[1]));//数量
			ordineConfermato.setCODICE(orderDTO.getSpPurchaseNo()); 
			ordineConfermato.setID_CLIENTE(20150918);
			logger.info("下单参数========spuId="+ordineConfermato.getID_ARTICOLO()+",尺码="+ordineConfermato.getTAGLIA()+",数量="+ordineConfermato.getQTA()+",采购单号="+ordineConfermato.getCODICE());
			OrdineConfermatoClienteResponse response = wS_SitoStub.ordineConfermatoCliente(ordineConfermato);
			String result = response.getOrdineConfermatoClienteResult();
			System.out.println(result); 
			logger.info("返回的结果======"+result);
			System.out.println(result); 			
			if(result.startsWith("OK")){
				orderDTO.setStatus(OrderStatus.CONFIRMED);
				orderDTO.setExcState("0");
			}else if(result.equals("ND")){//可能是没有库存
				orderDTO.setExcState("1");
				orderDTO.setExcDesc(result);
				orderDTO.setExcTime(new Date());
			}else{
				orderDTO.setExcState("1");
				orderDTO.setExcDesc(result);
				orderDTO.setExcTime(new Date());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error(e); 
			orderDTO.setExcState("1");
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcTime(new Date());
		}
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		deleteOrder.setStatus(OrderStatus.CANCELLED); 
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
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
	
	public static void main(String[] args) {
//		OrderSreviceImpl order = new OrderSreviceImpl();
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setSpPurchaseNo("CGD2016071100291");
//		orderDTO.setDetail("9386103-42:1,");
//		order.handleConfirmOrder(orderDTO); 
//		System.out.println("41+".replaceAll("+", "½"));
		
	}
	

}
