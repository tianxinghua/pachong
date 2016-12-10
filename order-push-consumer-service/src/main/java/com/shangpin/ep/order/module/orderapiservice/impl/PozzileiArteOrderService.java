package com.shangpin.ep.order.module.orderapiservice.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

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
import com.shangpin.ep.order.module.orderapiservice.impl.dto.spinnaker.ResponseObject;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

@SuppressWarnings("static-access")
@Component("pozzileiArteOrderService")
public class PozzileiArteOrderService implements IOrderService {



	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;

	private static OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60*5, 1000 * 60*5);
    private  String cancelUrl = null;
    private  String queryOrderUrl = null;
    private  String setOrderUrl = null;
    private  String dBContext = null;
    private  String key = null;
    
    @PostConstruct
    public void init(){
    	cancelUrl = supplierProperties.getPozzileiArte().getCancelUrl();
    	queryOrderUrl = supplierProperties.getPozzileiArte().getQueryOrderUrl();
    	setOrderUrl = supplierProperties.getPozzileiArte().getSetOrderUrl();
    	dBContext = supplierProperties.getPozzileiArte().getDBContext();
    	key = supplierProperties.getPozzileiArte().getKey();
    }
	/**
	 * 锁库存
	 */

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		createOrder(orderDTO);
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED);
		deleteOrder.setCancelTime(new Date());
		deleteOrder.setLogContent("------取消锁库结束-------");
		logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_CANCELLED_LOG);
	}

	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		
		refundlOrder(deleteOrder);
	}
	 /**
	 * 给对方推送订单或者退单
	 * @param soapRequestData
	 * @return
	 * @throws Exception
	 */	
	private String pozzileiArtePushOrder(String url, OrderDTO orderDTO,Map<String, String> map)  throws Exception{
		return HttpUtil45.get(url, defaultConfig ,map);
		 
	}
	public String handleException(String url,OrderDTO orderDTO,Map<String, String> map,Throwable e){		
		handleException.handleException(orderDTO, e); 
		return null;
	}
	private void createOrder( OrderDTO orderDTO) {

		// 获取订单信息
		String detail = orderDTO.getDetail();
		String[] details = detail.split(":");
		String skuId = details[0];
		String qty = details[1];
		String rtnData = null;
		try {
			 Map<String, String> map =new HashMap<String, String>();
			 map.put("DBContext", dBContext);
			 map.put("purchase_no", orderDTO.getPurchaseNo());
			 map.put("order_no", orderDTO.getSpOrderId());
			 map.put("barcode", skuId);
			 map.put("ordQty", qty);
			 map.put("key", key);
			 map.put("sellPrice", "0");
			 rtnData =pozzileiArtePushOrder(setOrderUrl,orderDTO, map);
			 orderDTO.setLogContent("推送订单返回结果="+rtnData+"推送的订单="+map.toString());
			 logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			
			Gson gson = new Gson();
			ResponseObject responseObject = gson.fromJson(rtnData, ResponseObject.class);
			if(null==responseObject.getStatus()){
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
				orderDTO.setDescription(orderDTO.getLogContent());
			}else if ("ko".equals(responseObject.getStatus().toLowerCase())) {
				if("0".equals(String.valueOf(responseObject.getId_b2b_order()))||"-1".equals(String.valueOf(responseObject.getId_b2b_order()))){   //无库存
					orderDTO.setPushStatus(PushStatus.NO_STOCK);
					orderDTO.setDescription(orderDTO.getLogContent());
				}else{
					orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
					orderDTO.setDescription(orderDTO.getLogContent());
				}

			} else {
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
				orderDTO.setConfirmTime(new Date());
				orderDTO.setSupplierOrderNo(String.valueOf(responseObject.getId_b2b_order()));
			}
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			orderDTO.setDescription(orderDTO.getLogContent());	
			handleException.handleException(orderDTO,e);
		}
	}
	

	private void refundlOrder(OrderDTO deleteOrder) {
		//查询状态
		String rtnData2 = null;
		try{
			Map<String, String> map =new HashMap<String, String>();
			 map.put("DBContext", dBContext);
			 map.put("purchase_no", deleteOrder.getPurchaseNo());
			 map.put("order_no", deleteOrder.getSpOrderId());
			 map.put("key", key);
			 rtnData2 =pozzileiArtePushOrder(queryOrderUrl,deleteOrder, map);
			 deleteOrder.setLogContent("查询订单返回结果="+rtnData2+"推送的订单="+map.toString());
			 logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
		}catch(Exception e){
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
			deleteOrder.setDescription(deleteOrder.getLogContent());
		}
		
		// 获取退单信息
		Gson gson = new Gson();

		ResponseObject response = gson.fromJson(rtnData2, ResponseObject.class);
		if("HO".equals(response.getStatus())){
			try {
				 Map<String, String> map =new HashMap<String, String>();
				 map.put("DBContext", dBContext);
				 map.put("purchase_no", deleteOrder.getPurchaseNo());
				 map.put("order_no", deleteOrder.getSpOrderId());
				 map.put("key", key);
				 String rtnData1 =pozzileiArtePushOrder(cancelUrl,deleteOrder, map);
				 deleteOrder.setLogContent("退单返回结果==" + rtnData1+",推送参数："+map.toString());
				 logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);

				ResponseObject responseObject = gson.fromJson(rtnData1, ResponseObject.class);
				if ("OK".equals(responseObject.getStatus())) {
					deleteOrder.setRefundTime(new Date());
					deleteOrder.setPushStatus(PushStatus.REFUNDED);
				} else {
					deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
					deleteOrder.setErrorType(ErrorStatus.API_ERROR);
					deleteOrder.setDescription(deleteOrder.getLogContent());
				}
			} catch (Exception e) {
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
				deleteOrder.setDescription(deleteOrder.getLogContent());
			}
		}else{
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			deleteOrder.setErrorType(ErrorStatus.API_ERROR);
			deleteOrder.setDescription(deleteOrder.getLogContent());
		}
	}
}
