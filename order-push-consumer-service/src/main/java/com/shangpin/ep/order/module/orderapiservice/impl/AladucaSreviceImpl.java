package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.bean.ReturnOrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.util.xml.SoapXmlUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.ResourceBundle;

/**
 * 下订单类，并且完成在线验证
 * @author sunny
 *
 */
@Component("aladucaSreviceImpl")
public class AladucaSreviceImpl implements IOrderService {
	    
    @Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
	
    
    /**
	 * 给对方推送订单或者退单
	 * @param soapRequestData
	 * @return
	 * @throws Exception
	 */	
	public String aladucaPushOrder(String serviceUrl,String sopAction, String contentType,String soapRequestData,OrderDTO order) throws Exception {
		return SoapXmlUtil.getSoapXml(serviceUrl,
				sopAction, contentType, soapRequestData);
	}
	
	public String handleException(String serviceUrl,String sopAction, String contentType,String soapRequestData,OrderDTO order,Throwable e){		
		handleException.handleException(order, e); 
		return null;
	}
	
	
    @Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
    	orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
	}

	/**
	 * 在线推送订单，已支付
	 */
	@Override
	public void handleConfirmOrder(OrderDTO spOrder) {
		
		try{
			String order = spOrder.getPurchaseNo();
			String[] sku_stocks = spOrder.getDetail().split(",");
			for(String sku_stock : sku_stocks){
				try {
					String[] tem = sku_stock.split(":");
					String soapRequestData =  "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
							"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"+
							"  <soap12:Body>"+
							"    <InsertOrder xmlns=\"http://service.alducadaosta.com/EcSrv\">"+
							"      <Identifier>"+supplierProperties.getAladuca().getIdentifier()+"</Identifier>"+
							"      <Order>"+order+"</Order>"+  
							"      <SkuID>"+tem[0].trim()+"</SkuID>"+
							"      <Quantity>"+tem[1].trim()+"</Quantity>"+  
							"    </InsertOrder>"+
							"  </soap12:Body>"+
							"</soap12:Envelope>";
					spOrder.setLogContent("推送的订单=========="+soapRequestData);
					logCommon.loggerOrder(spOrder, LogLeve.INFO);
					String str = aladucaPushOrder(supplierProperties.getAladuca().getServiceUrl(),supplierProperties.getAladuca().getSopAction(),supplierProperties.getAladuca().getContentType(),soapRequestData,
							 spOrder);
					spOrder.setLogContent("返回的结果========" + str+" 推送的订单=========="+soapRequestData);
					logCommon.loggerOrder(spOrder, LogLeve.INFO);
					if (StringUtils.isNotBlank(str)) {
						String startStr = "<InsertOrderResult>";
						String endStr = "</InsertOrderResult>";
						String retMessage = str.substring(
								str.indexOf(startStr) + startStr.length(),
								str.indexOf(endStr));						
						if (retMessage.toUpperCase().equals("OK")) {// 下单成功
							spOrder.setConfirmTime(new Date()); 
							spOrder.setPushStatus(PushStatus.ORDER_CONFIRMED);
							
						}else if(retMessage.equals("Fail, No Stock!")){//无库存
							spOrder.setConfirmTime(new Date()); 
							spOrder.setPushStatus(PushStatus.NO_STOCK); 
							
						}else {// 下单失败	
							spOrder.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
							spOrder.setErrorType(ErrorStatus.OTHER_ERROR);							
							spOrder.setDescription("下单失败：" + retMessage);
						}
					} else {
						spOrder.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
						spOrder.setErrorType(ErrorStatus.OTHER_ERROR);							
						spOrder.setDescription("采购单：" + order
								+ " 在请求验证时发生错误，未返回响应信息");										
					}

				} catch (Exception e) {
					spOrder.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					handleException.handleException(spOrder,e);
					spOrder.setLogContent("推送订单异常========= "+e.getMessage());
					logCommon.loggerOrder(spOrder, LogTypeStatus.CONFIRM_LOG);
				}
			}
			
		}catch(Exception ex){
			//下单失败
			spOrder.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(spOrder,ex);
			spOrder.setLogContent("推送订单异常========= "+ex.getMessage());
			logCommon.loggerOrder(spOrder, LogTypeStatus.CONFIRM_LOG);
		}
		
	}

	@Override
	public void handleCancelOrder(OrderDTO orderDTO) {	
		orderDTO.setCancelTime(new Date()); 
		orderDTO.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API); 
	}

	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {		

		String order = deleteOrder.getPurchaseNo();
		
		try{
			String skuId = deleteOrder.getDetail().split(",")[0].split(":")[0];
			String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
					"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
					"<soap:Body>"+
					"<DeleteOrder xmlns=\"http://service.alducadaosta.com/EcSrv\">"+
					"<Identifier>"+supplierProperties.getAladuca().getIdentifier()+"</Identifier>"+
					"<Order>"+order+"</Order>"+
					"<SkuID>"+skuId+"</SkuID>"+
					"</DeleteOrder>"+
					"</soap:Body>"+
					"</soap:Envelope>";
			deleteOrder.setLogContent("退款订单=========="+soapRequestData);
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
			String str = aladucaPushOrder(supplierProperties.getAladuca().getDeleteOrderUrl(), supplierProperties.getAladuca().getDeleteOrderSop(), supplierProperties.getAladuca().getContentType(), soapRequestData,deleteOrder);
			deleteOrder.setLogContent("退单返回信息===="+str);
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
			String startStr = "<DeleteOrderResult>";
			String endStr = "</DeleteOrderResult>";
			String retMessage = str.substring(str.indexOf(startStr)+startStr.length(), str.indexOf(endStr));
			deleteOrder.setLogContent("截取的信息===="+retMessage);
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
			if(retMessage.toUpperCase().equals("OK")){//退款成功
				deleteOrder.setRefundTime(new Date());
				deleteOrder.setPushStatus(PushStatus.REFUNDED);
				
			}else{//退款失败
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
				deleteOrder.setDescription(retMessage);
			}
			
		}catch(Exception e){
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			handleException.handleException(deleteOrder, e); 		
			deleteOrder.setLogContent("推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
		}
		
	}

	
//	public static void main(String[] args){
//		OrderSreviceImpl o = new OrderSreviceImpl();
//		ReturnOrderDTO spOrder = new ReturnOrderDTO();
//		spOrder.setSpPurchaseNo("CGD2016021300");
//		spOrder.setDetail("203275406502:1");
//		o.handleRefundlOrder(spOrder);
//		
//	}
}
