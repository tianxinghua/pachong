package com.shangpin.ep.order.module.orderapiservice.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.antonacci.ResponseObject;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import com.shangpin.ep.order.util.utils.UUIDGenerator;

/**
 * Created by zhaogenchun on 17/03/10.
 */
@Component("antonacciOrderImpl")
public class AntonacciOrderImpl implements IOrderService {
	private static Logger logger = Logger.getLogger("info");
	@Autowired
	LogCommon logCommon;
	@Autowired
	SupplierProperties supplierProperties;
	@Autowired
	HandleException handleException;

	public static void main(String[] args) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setSpOrderId("201703231100111");
		orderDTO.setSupplierSkuNo("A16-800687VJ00250-V1382-XL");
		orderDTO.setPurchaseNo("CDGF201703231100111");
		
//		new AntonacciOrderImpl().handleSupplierOrder(orderDTO);
//		new AntonacciOrderImpl().handleConfirmOrder(orderDTO);
//		new AntonacciOrderImpl().handleCancelOrder(orderDTO);
		new AntonacciOrderImpl().handleRefundlOrder(orderDTO);

		
	}

	/**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		String json = " {\"order_no\":\"" + orderDTO.getSpOrderId() + "\",\"order_items\":\"[{'sku_id':'"
				+ orderDTO.getSupplierSkuNo() + "','quantity':1}]\"}";
		createOrder(orderDTO, "lock", "holdorders", json);
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		// 在线推送订单
		String json = " {\"purchase_no\":\"" + orderDTO.getPurchaseNo() + "\",\"order_no\":\"" + orderDTO.getSpOrderId()
				+ "\",\"order_items\":\"[{'sku_id':'" + orderDTO.getSupplierSkuNo() + "','quantity':1}]\"}";
		createOrder(orderDTO, "confirm", "orders", json);
	}

	/**
	 * 在线取消订单
	 */
	@Override
	public void handleCancelOrder(OrderDTO orderDTO) {
		Gson gson = new Gson();
		String json = "{\"order_no\":\"" + orderDTO.getSpOrderId() + "\",\"status\":\"free\"}";
		try {
			String uuid = null;
			if (orderDTO.getSupplierOrderNo() == null) {
				uuid = UUIDGenerator.getUUID();
			} else {
				uuid = orderDTO.getSupplierOrderNo();
			}
			String result = getResponse(json,"holdorders",uuid);
			logger.info("取消订单推送返回结果==" + result + ",推送的数据：" + json);
			orderDTO.setLogContent("取消订单推送返回结果==" + result + ",推送的数据：" + json);
			ResponseObject respon = gson.fromJson(result, ResponseObject.class);
			if(respon!=null&&"free".equals(respon.getStatus())){
				orderDTO.setCancelTime(new Date());
				orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED);
			}else{
				orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED_ERROR);
                orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
                orderDTO.setDescription(orderDTO.getLogContent());
			} 		
		} catch (Exception e) {
			logger.info("取消订单推送返回结果==" + e.getMessage() + ",推送的数据：" + json);
			orderDTO.setLogContent("取消订单推送返回结果==" + e.getMessage() + ",推送的数据：" + json);
			orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED_ERROR);
            orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
            orderDTO.setDescription(orderDTO.getLogContent());
		}
	}

	@Override
	public void handleRefundlOrder(OrderDTO orderDTO) {
		Gson gson = new Gson();
		String json = " {\"purchase_no\":\"" + orderDTO.getPurchaseNo() + "\",\"order_no\":\"" + orderDTO.getSpOrderId()
				+ "\",\"order_items\":\"[{'sku_id':'" + orderDTO.getSupplierSkuNo() + "','quantity':1}]\"}";
		try {
			String uuid = null;
			if (orderDTO.getSupplierOrderNo() == null) {
				uuid = UUIDGenerator.getUUID();
			} else {
				uuid = orderDTO.getSupplierOrderNo();
			}
			String result = getResponse(json,"orders",uuid);
			logger.info("退款推送返回结果==" + result + ",推送的数据：" + json);
			orderDTO.setLogContent("退款订单推送返回结果==" + result + ",推送的数据：" + json);
			ResponseObject respon = gson.fromJson(result, ResponseObject.class);
			if(respon!=null&&"cancelled".equals(respon.getStatus())){
				orderDTO.setRefundTime(new Date());
				orderDTO.setPushStatus(PushStatus.REFUNDED);
			}else if(respon.getError()!=null){
				orderDTO.setPushStatus(PushStatus.REFUNDED_ERROR);
                orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
                orderDTO.setDescription(orderDTO.getLogContent());
			}else{
				orderDTO.setPushStatus(PushStatus.REFUNDED_ERROR);
                orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
                orderDTO.setDescription(orderDTO.getLogContent());
			} 		
		} catch (Exception e) {
			logger.info("退款推送返回结果==" + e.getMessage() + ",推送的数据：" + json);
			orderDTO.setLogContent("退款推送返回结果==" + e.getMessage() + ",推送的数据：" + json);
			orderDTO.setPushStatus(PushStatus.REFUNDED_ERROR);
            orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
            orderDTO.setDescription(orderDTO.getLogContent());
		}
	}
	/**
	 * 在线推送订单: status未支付：锁库存 status已支付：推单
	 */
	private void createOrder(OrderDTO orderDTO, String type, String uri, String json) {
		// 获取订单信息df4cef37300d41f1910a894c3f927556
		Gson gson = new Gson();
		String rtnData = null;
		String uuid = null;
		if (orderDTO.getSupplierOrderNo() == null) {
			uuid = UUIDGenerator.getUUID();
		} else {
			uuid = orderDTO.getSupplierOrderNo();
		}
		try {
			rtnData = clutherPushOrder(orderDTO, uri, uuid, json);
			logger.info(uri + "," + type + "推送订单返回结果==" + rtnData + ",推送的数据：" + json);
			orderDTO.setLogContent(uri + "," + type + "推送订单返回结果==" + rtnData);
			if (HttpUtil45.errorResult.equals(rtnData)) {
				orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
				setType(orderDTO, type, uuid, "ERROR");
			}
			ResponseObject respon = gson.fromJson(rtnData, ResponseObject.class);
			setType(orderDTO, type, uuid, respon.getStatus());
		} catch (Exception e) {
			orderDTO.setLogContent(uri + "," + type + "推送订单返回结果==" + e.getMessage());
			setType(orderDTO, type, uuid, "ERROR");
		}
	}

	private void setType(OrderDTO orderDTO, String type, String id,String flag ) {
		if ("confirm".equals(type)) {
			if ("confirmed".equals(flag)) {
				orderDTO.setConfirmTime(new Date());
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
			}else if("ERROR".equals(flag)){
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
                orderDTO.setDescription(orderDTO.getLogContent());
			} else {
				orderDTO.setPushStatus(PushStatus.NO_STOCK);
				orderDTO.setDescription(orderDTO.getLogContent());
			}
		}
		if ("lock".equals(type)) {
			orderDTO.setSupplierOrderNo(id);
			if ("placed".equals(flag)) {
				orderDTO.setLockStockTime(new Date());
				orderDTO.setPushStatus(PushStatus.LOCK_PLACED);
			}else if("ERROR".equals(flag)){
				orderDTO.setPushStatus(PushStatus.LOCK_PLACED_ERROR);
                orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
                orderDTO.setDescription(orderDTO.getLogContent());
			}else{
				orderDTO.setPushStatus(PushStatus.NO_STOCK);
				orderDTO.setDescription(orderDTO.getLogContent());
			} 
		}
	}

	public String handleException(OrderDTO orderDTO, String url, String type, Throwable e) {
		handleException.handleException(orderDTO, e);
		return null;
	}

	private String clutherPushOrder(OrderDTO orderDTO, String url, String uuid, String json)
			throws Exception {

		return HttpUtil45.operateData("put", "json", "http://www.luxury888.it/" + url + "/" + uuid,
				new OutTimeConfig(1000 * 60 * 2, 1000 * 60 * 2, 1000 * 60 * 2), null, json, null, null, null);
	}

	private static final int TIMEOUT = 5 * 1000;

	private static String getResponse(String json,String uri,String uuid) throws Exception {
		URL url = new URL("http://www.luxury888.it/"+uri+"/"+uuid);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("DELETE");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
		connection.setConnectTimeout(TIMEOUT);
		connection.setRequestProperty("content-type", "application/json; charset=utf-8");
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
		// 组织要传递的参数
		out.write(json);
		out.flush();
		out.close();
		// 获取返回的数据
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = null;
		StringBuffer content = new StringBuffer();
		while ((line = in.readLine()) != null) {
			// line 为返回值，这就可以判断是否成功
			content.append(line);
		}
		in.close();
		return content.toString();
	}

}
