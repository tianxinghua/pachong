package com.shangpin.ep.order.module.orderapiservice.impl.parisi;


import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.parisi.OrderOfSupplier;


@Component("parisiOrderService")
public class ParisiOrderServiceImpl implements IOrderService {

	
	@Autowired
    LogCommon logCommon;    

    @Autowired
    HandleException handleException;

    @Autowired
    ParisiOrderUtil parisiOrderUtil;

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	/**
	 * 锁库存
	 */
    @Override
   	public void handleSupplierOrder(OrderDTO orderDTO) {
    	
    	createOrder(orderDTO);
    	
   		orderDTO.setLockStockTime(new Date());
   		orderDTO.setPushStatus(PushStatus.LOCK_PLACED);
   		orderDTO.setLogContent("");
   		logCommon.loggerOrder(orderDTO, LogLeve.INFO);
   	}

   	/**
   	 * 推送订单
   	 */
   	@Override
   	public void handleConfirmOrder(OrderDTO orderDTO) {
   		confirmOrder(orderDTO);
   	}

   	@Override
   	public void handleCancelOrder(OrderDTO deleteOrder) {
   		cancelOrder(deleteOrder);
   		
   		deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED);
   		deleteOrder.setCancelTime(new Date());
   		deleteOrder.setLogContent("------取消锁库结束-------");
   		
   		logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_CANCELLED_LOG);
   	}

   	@Override
   	public void handleRefundlOrder(OrderDTO deleteOrder) {
   		
   		refundlOrder(deleteOrder);
   	}


   	public String handleException(String url,OrderDTO orderDTO,Map<String, String> map,Throwable e){		
   		handleException.handleException(orderDTO, e); 
   		return null;
   	}

	private void createOrder(OrderDTO orderDTO) {

		// 获取订单信息
		String detail = orderDTO.getDetail();
		String[] details = detail.split(":");
		String skuId = details[0];
		String qty = details[1];
		try {

			OrderOfSupplier order = parisiOrderUtil.pushOrder(orderDTO, skuId, qty);
			orderDTO.setLogContent("下单返回结果==" + order.toString() + ",推送参数：" + "SpOrderId:"+orderDTO.getSpOrderId()+"skuId:"+skuId+"qty:"+qty);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);

			if (order.getOrderDetail().getError() != null) {
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
				orderDTO.setDescription(orderDTO.getLogContent());
			} else {
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
				orderDTO.setConfirmTime(new Date());
				orderDTO.setSupplierOrderNo(order.getOrderDetail().getOrder_no());
			}
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			orderDTO.setDescription(orderDTO.getLogContent());
			orderDTO.setLogContent(e.getMessage());
			handleException.handleException(orderDTO, e);
		}
	}



	private void confirmOrder(OrderDTO orderDTO) {

		// 获取订单信息
		String detail = orderDTO.getDetail();
		String[] details = detail.split(":");
		String skuId = details[0];
		String qty = details[1];
		try {
			OrderOfSupplier order = parisiOrderUtil.confirmOrder(orderDTO, skuId, qty);

			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			orderDTO.setLogContent("下单返回结果==" + order.toString() + ",推送参数：" + "SpOrderId:"+orderDTO.getSpOrderId()+"PurchaseNo:"+orderDTO.getPurchaseNo()+"skuId:"+skuId+"qty:"+qty);

			if (order.getOrderDetail().getError() != null) {
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
				orderDTO.setDescription(orderDTO.getLogContent());
			} else {
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
				orderDTO.setConfirmTime(new Date());
				orderDTO.setSupplierOrderNo(order.getOrderDetail().getOrder_no());
			}
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			orderDTO.setDescription(orderDTO.getLogContent());
			orderDTO.setLogContent(e.getMessage());
			handleException.handleException(orderDTO, e);
		}
	}

	private void cancelOrder(OrderDTO orderDTO) {

		// 获取订单信息
		try {
			OrderOfSupplier order = parisiOrderUtil.cancelOrder(orderDTO);
			orderDTO.setLogContent("退单返回结果==" + order.toString() + ",推送参数：" + "SpOrderId:"+orderDTO.getSpOrderId());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);

			if (order.getOrderDetail().getError() != null) {
				orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
				orderDTO.setDescription(orderDTO.getLogContent());
			} else {
				orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED);
				orderDTO.setConfirmTime(new Date());
				orderDTO.setSupplierOrderNo(order.getOrderDetail().getOrder_no());
			}
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED_ERROR);
			orderDTO.setDescription(orderDTO.getLogContent());
			orderDTO.setLogContent(e.getMessage());
			handleException.handleException(orderDTO, e);
		}
	}

	private void refundlOrder(OrderDTO deleteOrder) {
		String detail = deleteOrder.getDetail();
		String[] details = detail.split(":");
		String skuId = details[0];
		try {
			// 获取退单信息
			try {
				OrderOfSupplier order = parisiOrderUtil.refund(deleteOrder, skuId);
				deleteOrder.setLogContent("退单返回结果==" + order.toString() + ",推送参数：" + "SpOrderId:"+deleteOrder.getSpOrderId()+"PurchaseNo:"+deleteOrder.getPurchaseNo()+"skuId:"+skuId);
				logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
				if (order.getOrderDetail().getError() == null) {
					deleteOrder.setRefundTime(new Date());
					deleteOrder.setPushStatus(PushStatus.REFUNDED);
				} else {
					deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
					deleteOrder.setErrorType(ErrorStatus.API_ERROR);
					deleteOrder.setLogContent(deleteOrder.getLogContent());
					deleteOrder.setDescription(deleteOrder.getLogContent());
				}
			} catch (Exception e) {
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
				deleteOrder.setDescription(deleteOrder.getLogContent());
				deleteOrder.setLogContent(e.getMessage());
			}
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
			deleteOrder.setDescription(e.getMessage());
			deleteOrder.setLogContent(e.getMessage());
		}
	}
	
	public static void main(String[] args){
		ParisiOrderServiceImpl serviceImpl = new ParisiOrderServiceImpl();
    	OrderDTO orderDTO = new OrderDTO();
    	orderDTO.setDetail("37372-18-6-9 mth:1");
    	orderDTO.setSpOrderId("0123456789");
    	orderDTO.setPurchaseNo("9876543210");
    	serviceImpl.handleSupplierOrder(orderDTO);
    	serviceImpl.handleCancelOrder(orderDTO);
    }
	
}
