package com.shangpin.ep.order.module.orderapiservice.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.shangpin.ep.order.module.order.service.impl.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.service.impl.OpenApiService;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.sku.mapper.HubSkuMapper;
import com.shangpin.ep.order.util.axis.Orders_v1_0Stub;
import com.shangpin.ep.order.util.axis.Orders_v1_0Stub.ArrayOfOrderDetail;
import com.shangpin.ep.order.util.axis.Orders_v1_0Stub.CreateOrder;
import com.shangpin.ep.order.util.axis.Orders_v1_0Stub.CreateOrderResponse;
import com.shangpin.ep.order.util.axis.Orders_v1_0Stub.OrderDetail;

/**
 * Created by lubaijiang
 */

@Component("stefaniaService")
public class StefaniaService implements IOrderService {

	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;

    @Autowired
    OpenApiService openApiService;

    @Autowired
	PriceService priceService;
    
    /**
     * 推送订单
     * @param createOrder
     * @param orderDTO
     * @return
     * @throws Exception
     */
    public CreateOrderResponse stefaniaPushOrder(CreateOrder createOrder,OrderDTO orderDTO) throws Exception{
    	Orders_v1_0Stub orders_v1_0Stub = new Orders_v1_0Stub();
    	return orders_v1_0Stub.createOrder(createOrder);
    }
    
    public CreateOrderResponse handleException(CreateOrder createOrder,OrderDTO orderDTO,Throwable e){
    	handleException.handleException(orderDTO, e); 
		return null;
    }
	
	@SuppressWarnings("static-access")
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
	
		try {
			BigDecimal priceInt = priceService.getPurchasePrice(orderDTO.getSupplierId(),"",orderDTO.getSpSkuNo());
			orderDTO.setLogContent("【stefania在创建订单时获取采购价："+priceInt.toString()+"】"); 
			logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);		
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		try {
			// TODO 支付逻辑
//			System.setProperty("javax.net.ssl.trustStore", supplierProperties.getStefania().getJssecacerts()+ File.separator+"jssecacerts");
			
			CreateOrder createOrder = new CreateOrder();
//			createOrder.setAuthKey("270Api002#3gU8zXs");
//			createOrder.setChannel("SHANGPIN");
			createOrder.setAuthKey(supplierProperties.getStefania().getAuthKey());
			createOrder.setChannel(supplierProperties.getStefania().getChannel());
			createOrder.setCustomerID("");
			createOrder.setDestCustID("");
			createOrder.setDestinationID(""); 
			createOrder.setRefID(orderDTO.getPurchaseNo()); 
			ArrayOfOrderDetail arrayOfOrderDetail =  new ArrayOfOrderDetail();
			OrderDetail[] orderDetail = new OrderDetail[1];
			OrderDetail detail = new OrderDetail();
			detail.setSKU(orderDTO.getDetail().split(",")[0].split(":")[0]);
			detail.setQTY(new BigDecimal(Integer.valueOf(orderDTO.getDetail().split(",")[0].split(":")[1])));

//			BigDecimal priceInt = openApiService.getPurchasePrice(supplierProperties.getStefania().getOpenApiKey(), supplierProperties.getStefania().getOpenApiSecret(), orderDTO.getPurchaseNo(), orderDTO.getSpSkuNo());
//			BigDecimal priceInt = new BigDecimal(orderDTO.getPurchasePriceDetail());
			BigDecimal priceInt = priceService.getPurchasePrice(orderDTO.getSupplierId(),"",orderDTO.getSpSkuNo());
			orderDTO.setLogContent("【stefania在推送订单时获取采购价："+priceInt.toString()+"】");
			if(priceInt.intValue()==10){
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
				orderDTO.setDescription("下单失败：未获取到采购单" );
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				return ;
			}
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			String serviceRate = priceService.GetServiceRate(orderDTO.getSupplierNo());
			BigDecimal price = priceInt.divide(new BigDecimal(serviceRate),5).setScale(0, BigDecimal.ROUND_HALF_UP);
			orderDTO.setPurchasePriceDetail(price.toString());
//			detail.setPRICE(new BigDecimal(orderDTO.getPurchasePriceDetail()));
			detail.setPRICE(price);
			orderDetail[0] = detail;
			arrayOfOrderDetail.setOrderDetail(orderDetail); 
			createOrder.setOrderDetailsList(arrayOfOrderDetail);
			orderDTO.setLogContent("下单参数=================AuthKey:"+createOrder.getAuthKey()+" Channel:"+createOrder.getChannel()+" RefID:"+createOrder.getRefID()+" SKU:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getSKU()
					+" QTY:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getQTY()+" PRICE:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getPRICE());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			System.out.println("下单参数=================AuthKey:"+createOrder.getAuthKey()+" Channel:"+createOrder.getChannel()+" RefID:"+createOrder.getRefID()+" SKU:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getSKU()
					+" QTY:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getQTY()+" PRICE:"+createOrder.getOrderDetailsList().getOrderDetail()[0].getPRICE());
			CreateOrderResponse createOrderResponse = stefaniaPushOrder(createOrder,orderDTO);
			orderDTO.setLogContent("返回结果 ERROR========"+createOrderResponse.getCreateOrderResult().getError()
					+"  MessageTxt======"+createOrderResponse.getCreateOrderResult().getMessageTxt()
					+"  ErrorTxt======"+createOrderResponse.getCreateOrderResult().getErrorTxt());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			System.out.println("返回结果 ERROR========"+createOrderResponse.getCreateOrderResult().getError()
					+"  MessageTxt======"+createOrderResponse.getCreateOrderResult().getMessageTxt()
					+"  ErrorTxt======"+createOrderResponse.getCreateOrderResult().getErrorTxt()); 
			if(createOrderResponse.getCreateOrderResult().getError() == 0){
				orderDTO.setSupplierOrderNo(createOrderResponse.getCreateOrderResult().getMessageTxt());
				orderDTO.setConfirmTime(new Date()); 
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
			}else if("Items in the returned list are not found available on stock.".equals(createOrderResponse.getCreateOrderResult().getErrorTxt())){
				orderDTO.setConfirmTime(new Date()); 
				orderDTO.setPushStatus(PushStatus.NO_STOCK); 
			}else if(createOrderResponse.getCreateOrderResult().getError() == 310 && createOrderResponse.getCreateOrderResult().getErrorTxt().contains("Duplicate order ID")){
				orderDTO.setConfirmTime(new Date()); 
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
			}else{
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
				orderDTO.setDescription("下单失败：" + createOrderResponse.getCreateOrderResult().getErrorTxt());
			}
			
			
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
				
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date()); 
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API); 	
	}

	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		deleteOrder.setRefundTime(new Date());
		deleteOrder.setPushStatus(PushStatus.NO_REFUNDED_API); 
		
	}

	
	public static void main(String[] args) {
		StefaniaService orderService = new StefaniaService();
		OrderDTO orderDTO = new OrderDTO();
		//CGD2016082400193 
		orderDTO.setPurchaseNo("CGDF2017060437340");
		orderDTO.setDetail("FY0811#S0F#F0BVE######48:1,");
		orderDTO.setPurchasePriceDetail("172.13");
		orderService.handleConfirmOrder(orderDTO); 
	}

}
