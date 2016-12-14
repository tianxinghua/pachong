package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import com.shangpin.ep.order.module.orderapiservice.impl.dto.kix.Customer;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.kix.Data;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.kix.Order;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.kix.OrderDetail;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

@Component
public class KixOrderImpl  implements IOrderService {
	
	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;  

    /**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		
		Gson gson = new Gson();
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders.json";
		String skuId = orderDTO.getDetail().split(",")[0].split(":")[0];
		String qty = orderDTO.getDetail().split(",")[0].split(":")[1];
		String jsonValue = genOrderStr("pending", qty, skuId);
		try {
//			String operateData = HttpUtil45.operateData("post", "json", url, outTimeConf , null, jsonValue , null,null, null);
			String operateData = kixPushOrder(orderDTO, url,jsonValue);
			orderDTO.setLogContent("锁库存推送返回结果=="+operateData+"，锁库存推送的数据："+jsonValue);
        	logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
			Data fromJson = gson.fromJson(operateData, Data.class);
			orderDTO.setSupplierOrderNo(fromJson.getOrder().getId());
			orderDTO.setLockStockTime(new Date());
        	orderDTO.setPushStatus(PushStatus.LOCK_PLACED);
		
		} catch (Exception e) {
			if (e.getMessage().contains("422")) {
				 orderDTO.setDescription(orderDTO.getLogContent());
            	 orderDTO.setPushStatus(PushStatus.NO_STOCK);
			}else{
		        orderDTO.setPushStatus(PushStatus.LOCK_PLACED_ERROR);
	            orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
	            orderDTO.setLogContent(e.getMessage());
	            logCommon.recordLog(e.getMessage(), e);
			}
		}
		
	}

	/**
	 * 推送订单
	 */
	@SuppressWarnings("static-access")
	@Override
	public void handleConfirmOrder(final OrderDTO orderDTO) {
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders/"+orderDTO.getSupplierOrderNo()+".json";
		String jsonValue = "{\"order\":{\"id\":"+orderDTO.getSupplierOrderNo()+",\"note\":\""+orderDTO.getPurchaseNo()+"have been paid\"}}";
		try {
			//不会有库存不足的情况，全部是网络异常支付失败，先判断异常状态
//				String operateData = HttpUtil45.operateData("put", "json", url, outTimeConf , null, jsonValue , null, null,null);
			String operateData = kixPushOrder(orderDTO, url,jsonValue);
				orderDTO.setLogContent("confirm返回的结果=" + operateData+",推送的参数="+jsonValue);
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				//确认订单成功
				orderDTO.setConfirmTime(new Date());
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
		} catch (Exception e) {
			//确认订单失败
			orderDTO.setErrorType(ErrorStatus.API_ERROR);
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			orderDTO.setDescription("orderNo"+orderDTO.getSupplierOrderNo()+"网络原因付款失败"+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
		
	}


	/**
	 * 解除库存锁
	 */
	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders/"+deleteOrder.getSupplierOrderNo()+"/cancel.json";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		Map<String, String> param = new HashMap<String, String>();
		param.put("restock", "true");
		param.put("reason", "customer cancle order");
		String post = "";
		try {
			post = HttpUtil45.post(url, param , outTimeConf);
			deleteOrder.setLogContent("取消订单推送返回结果=="+post+"，取消订单推送的数据："+param.toString());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_LOG);
			//取消订单成功
			deleteOrder.setCancelTime(new Date());
			deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED);
		} catch (Exception e) {
			//取消订单失败
			deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED_ERROR);
			deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
			deleteOrder.setDescription(deleteOrder.getLogContent());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_CANCELLED_LOG);
		}
		
	}

	/**
	 * 退款
	 */
	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		//TODO 上线前放开
		String url = "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/orders/"+deleteOrder.getSupplierOrderNo()+"/transactions.json";
		String skuId = deleteOrder.getDetail().split(",")[0].split(":")[0];
		String price = deleteOrder.getPurchasePriceDetail();
		String transfaction = "{\"transaction\":{\"kind\":\"refund\",\"amount\":\""+price+"\"}}";
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		try {
			String operateData = kixPushOrder(deleteOrder, url,transfaction);
			//HttpUtil45.operateData("put", "json", url, outTimeConf , null, transfaction , null,null,  null);
	    	 deleteOrder.setLogContent("退款返回结果=="+transfaction+"，退款推送的数据："+operateData);
	     	 logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_LOG);
			deleteOrder.setRefundTime(new Date());
            deleteOrder.setPushStatus(PushStatus.REFUNDED);
			//退款取消订单成功
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
        	deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
            deleteOrder.setDescription(deleteOrder.getLogContent());
		}
		
	}

	private String genOrderStr(String status,String qty,String id){
		Customer customer = new Customer("2692871043");
		
		Order order = new Order();
		order.setInventory_behaviour("decrement_obeying_policy");
		order.setFinancial_status(status);
		order.setCustomer(customer);
		List<OrderDetail> line_items = new ArrayList<OrderDetail>();
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setQuantity(qty);
		orderDetail.setVariant_id(id);
		line_items.add(orderDetail);
		order.setLine_items(line_items );
		
		Data data = new Data(order);
		Gson gson = new Gson();
		String json = gson.toJson(data);
		return json;
	}
  /**
	 * 给对方推送订单或者退单
	 * @param orderDTO
	 * @return
	 * @throws Exception
	 */
//    @HystrixCommand(fallbackMethod = "handleException")
	private String kixPushOrder(OrderDTO orderDTO, String url, String json)  throws Exception{
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		return HttpUtil45.operateData("put", "json", url, outTimeConf , null, json , null,null,  null);
	}
	
	public String handleException(OrderDTO orderDTO, String url, String json,Throwable e){		
		handleException.handleException(orderDTO, e); 
		return null;
	}
	private static Iterable<? extends NameValuePair> map2NameValuePair(
			Map<String, String> param) {
		Iterator<Entry<String, String>> kvs = param.entrySet().iterator();
		List<NameValuePair> nvs = new ArrayList<NameValuePair>(param.size());
		while (kvs.hasNext()) {
			Entry<String, String> kv = kvs.next();
			NameValuePair nv = new BasicNameValuePair(kv.getKey(),
					kv.getValue());
			nvs.add(nv);
		}
		return nvs;
	}
	public static void main(String[] args) {
		KixOrderImpl ompl = new KixOrderImpl();
//		ReturnOrderDTO orderDTO = new ReturnOrderDTO();
		String d = "728590487:1";
//		orderDTO.setDetail(d);
//		orderDTO.setSpOrderId("201609134249189");
//		orderDTO.setCreateTime(new Date());
		
		OrderDTO orderDTO1 = new OrderDTO();
		orderDTO1.setDetail(d);
		orderDTO1.setSpOrderId("201612145135193");
		orderDTO1.setCreateTime(new Date());
		orderDTO1.setPurchasePriceDetail("1");
		
//		ompl.handleRefundlOrder(orderDTO);//(orderDTO);
		ompl.handleSupplierOrder(orderDTO1);//(orderDTO);
	}

}
