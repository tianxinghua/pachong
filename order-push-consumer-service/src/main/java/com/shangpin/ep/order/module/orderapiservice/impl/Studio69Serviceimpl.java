package com.shangpin.ep.order.module.orderapiservice.impl;



import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.axis2.transport.http.impl.httpclient4.HttpTransportPropertiesImpl.Authenticator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.studio69.Response;
import com.shangpin.ep.order.module.sku.mapper.HubSkuMapper;
import com.shangpin.ep.order.util.axis.studio69.API_STUDIO69Stub;
import com.shangpin.ep.order.util.axis.studio69.API_STUDIO69Stub.CancelOrder;
import com.shangpin.ep.order.util.axis.studio69.API_STUDIO69Stub.CancelOrderResponse;
import com.shangpin.ep.order.util.axis.studio69.API_STUDIO69Stub.CreateNewOrder;
import com.shangpin.ep.order.util.axis.studio69.API_STUDIO69Stub.CreateNewOrderResponse;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import com.shangpin.ep.order.util.xml.ObjectXMLUtil;

/**
 * Created by lubaijiang
 */

@Component("studio69Serviceimpl")
public class Studio69Serviceimpl implements IOrderService{

	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    @Autowired
    HubSkuMapper skuDAO;
    
    /**
     * 推送订单
     * @param createNewOrder
     * @param orderDTO
     * @return
     * @throws Exception
     */
    public CreateNewOrderResponse studio69PushOrder(CreateNewOrder createNewOrder,OrderDTO orderDTO) throws Exception{
    	API_STUDIO69Stub stub = new API_STUDIO69Stub();	
    	return stub.createNewOrder(createNewOrder);
    }
    public CreateNewOrderResponse handleException(CreateNewOrder createNewOrder,OrderDTO orderDTO,Throwable e){
    	handleException.handleException(orderDTO, e); 
		return null;
    }
    
	
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);	
	}
	
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		try {
			// TODO 支付逻辑				
			CreateNewOrder createNewOrder = new CreateNewOrder();
			String size = orderDTO.getDetail().split(",")[0].split(":")[0].split("-")[1];
			String buyerInfo = "<buyerInfo>"
					+ "<Name>Genertec Italia S.r.l.</Name>"
					+ "<Address>Via Leopardi 27</Address>"					
					+ "<zipcode>22075</zipcode>"
					+ "<Corriere>Fedex 0123456789</Corriere>"
					+ "<Notes>"+orderDTO.getPurchaseNo()+"</Notes>"
					+ "</buyerInfo>";
			String goodsList = "<GoodsList>"
					+ "<Good>"
					+ "<ID>"+orderDTO.getDetail().split(",")[0].split(":")[0].split("-")[0]+"</ID>"
					+ "<Size>"+size+"</Size>" 
					+ "<Qty>"+orderDTO.getDetail().split(",")[0].split(":")[1]+"</Qty>"
					+ "<Price>"+orderDTO.getPurchasePriceDetail()+"</Price>"
					+ "</Good>"
					+ "</GoodsList>";			
			if(size.contains("+")){
				createNewOrder.setBuyerInfo(buyerInfo);
				createNewOrder.setGoodsList(goodsList);
				createNewOrder.setOrderID(orderDTO.getPurchaseNo());
				System.out.println("goodsList================="+orderDTO.getPurchaseNo()+":"+goodsList);				
				orderDTO.setLogContent("goodsList================="+orderDTO.getPurchaseNo()+":"+goodsList);
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				Response response = createOrder(orderDTO, createNewOrder);
				//如果尺码为+时下单失败，那么有有可能是½类型的尺码。
				if("Failed".equals(response.getResult()) && "Goods Stock doesn't enough".equals(response.getMessage())){
					size = size.replaceAll("\\+", "½");
					String newGoodsList = "<GoodsList>"
							+ "<Good>"
							+ "<ID>"+orderDTO.getDetail().split(",")[0].split(":")[0].split("-")[0]+"</ID>"
							+ "<Size>"+size+"</Size>" 
							+ "<Qty>"+orderDTO.getDetail().split(",")[0].split(":")[1]+"</Qty>"
							+ "<Price>"+orderDTO.getPurchasePriceDetail()+"</Price>"
							+ "</Good>"
							+ "</GoodsList>";					
					orderDTO.setLogContent("含有+号的sku第一次下单失败，第二次下单参数======"+newGoodsList);
					logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
					createNewOrder.setGoodsList(newGoodsList);
					createOrder(orderDTO, createNewOrder);
				}
			}else{				
				createNewOrder.setBuyerInfo(buyerInfo);
				createNewOrder.setGoodsList(goodsList);
				createNewOrder.setOrderID(orderDTO.getPurchaseNo());
				System.out.println("goodsList================="+orderDTO.getPurchaseNo()+":"+goodsList);
				orderDTO.setLogContent("goodsList================="+orderDTO.getPurchaseNo()+":"+goodsList); 
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				createOrder(orderDTO, createNewOrder);
			}
			
			
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
				
	}

	private Response createOrder(OrderDTO orderDTO, CreateNewOrder createNewOrder)
			throws Exception {
		CreateNewOrderResponse  cresponse = studio69PushOrder(createNewOrder,orderDTO);
		OMElement   result = cresponse.getCreateNewOrderResult().getExtraElement();
		System.out.println(result.toString());
		orderDTO.setLogContent("下单返回结果============="+result.toString());
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		Response response = ObjectXMLUtil.xml2Obj(Response.class, result.toString());
		if("Success".equals(response.getResult())){
			orderDTO.setConfirmTime(new Date()); 
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
		}else if("Failed".equals(response.getResult()) && "Goods Stock doesn't enough".equals(response.getMessage())){//库存不足
			orderDTO.setConfirmTime(new Date()); 
			orderDTO.setPushStatus(PushStatus.NO_STOCK); 
		}else{
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
			orderDTO.setDescription("下单失败：" + response.getMessage());
		}
		return response;
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date()); 
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API); 	
	}

	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		
		try {
			// TODO 退款逻辑
			API_STUDIO69Stub stub = new API_STUDIO69Stub();	
			CancelOrder cancelOrder = new CancelOrder();
			cancelOrder.setOrderID(deleteOrder.getPurchaseNo());
			deleteOrder.setLogContent("退款的订单是==========="+deleteOrder.getPurchaseNo()); 
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
			CancelOrderResponse resonse = stub.cancelOrder(cancelOrder);
			OMElement result = resonse.getCancelOrderResult().getExtraElement();
			System.out.println(result.toString());
			deleteOrder.setLogContent("取消返回结果==========="+result.toString()); 
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
			Response response = ObjectXMLUtil.xml2Obj(Response.class, result.toString());
			if("Success".equals(response.getResult())){
				deleteOrder.setRefundTime(new Date());
				deleteOrder.setPushStatus(PushStatus.NO_REFUNDED_API); 
			}else{
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
				deleteOrder.setDescription(response.getResult());
			}
			
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			handleException.handleException(deleteOrder, e); 		
			deleteOrder.setLogContent("推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
		}
		
	}

	public static void main(String[] args) {
		
		try {
			//下单=================================
			Studio69Serviceimpl order = new Studio69Serviceimpl();
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setDetail("2366-38+:1,");
			orderDTO.setPurchaseNo("CGD2016103106874");
			orderDTO.setPurchasePriceDetail("160");
			order.handleConfirmOrder(orderDTO); 

//			API_STUDIO69Stub stub = new API_STUDIO69Stub();	
//			CancelOrder cancelOrder = new CancelOrder();
//			cancelOrder.setOrderID("CGD2016091200298");
//			CancelOrderResponse resonse = stub.cancelOrder(cancelOrder);
//			OMElement result = resonse.getCancelOrderResult().getExtraElement();
//			System.out.println(result.toString());
//			logger.info("取消返回结果==========="+result.toString()); 
//			String result = "<Response><Result>Failed</Result><Message>order id not exist</Message></Response>";
//			Response response = ObjectXMLUtil.xml2Obj(Response.class, result.toString());
//			
//			System.out.println(response.getResult());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
