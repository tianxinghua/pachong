package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.supplier.bean.SupplierSkuRequestDto;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

@Component("srlOrderService")
public class SrlOrderService implements IOrderService {
	
	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    @Autowired
	private OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60*2, 1000 * 60*2);
    
    public String handleException(String url, Map<String,String> param, OutTimeConfig outTimeConf, String userName, String password,OrderDTO order,Throwable e){
    	handleException.handleException(order, e); 
		return null;
    }
	
	@SuppressWarnings("static-access")
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
	}

	@SuppressWarnings("static-access")
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		try {
			String spOrderId = orderDTO.getSpOrderId();
			if(spOrderId.contains("-")){
				spOrderId = spOrderId.substring(0, spOrderId.indexOf("-")); 
			}
			String skuId = orderDTO.getDetail().split(",")[0].split(":")[0];
			int qty = Integer.valueOf(orderDTO.getDetail().split(",")[0].split(":")[1]);
			//调用确认下单接口
			String rtnData = "";
			SupplierSkuRequestDto dto = new SupplierSkuRequestDto();
			dto.setSupplierId(orderDTO.getSupplierId());
			dto.setSkuId(skuId);
			
			String queryJson = "{\"skuId\":\""+skuId+"\",\"supplierId\":\""+orderDTO.getSupplierId()+"\"}";
			String supplierPrice  =  HttpUtil45.operateData("post","json",
					supplierProperties.getSrl().getBusinessApi()+"supplier-sku-handle/supplier-sku",
					defaultConfig,null,queryJson,null,"","");
			orderDTO.setLogContent("请求获取srl供应商商品供价参数=================param:"+queryJson);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			String requestdata = "{\"sku\":\""+skuId+"\",\"qty\":\""+String.valueOf(qty)+"\",\"supplyPrice\":\""+supplierPrice+"\"}";
			if(supplierPrice!=null&&!supplierPrice.equals("")) {
				rtnData  =  HttpUtil45.operateData3("post","json",
				supplierProperties.getSrl().getSalesUpdate()+"?sellid="+spOrderId,
				defaultConfig,null,requestdata,null,"","");
				orderDTO.setLogContent("下单参数=================param:"+requestdata+ "sellid:"+ spOrderId +"返回结果rtnData:"+rtnData);
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				JSONObject jsStr = (JSONObject) JSONObject.parse(rtnData);
				if (jsStr.get("success").equals(true)) {
					orderDTO.setConfirmTime(new Date()); 
					orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED); 
				} else {
					//推送订单失败
					orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
					orderDTO.setDescription("下单失败：" + rtnData);
				}
			}else {
				//推送订单失败
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
				orderDTO.setDescription(requestdata+"供价不存在!");
			}
			
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
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

	@SuppressWarnings("static-access")
	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		try {
			String spOrderId = deleteOrder.getSpOrderId();
			if (spOrderId.contains("-")) {
				spOrderId = spOrderId.substring(0, spOrderId.indexOf("-"));
			}
			String skuId = null;
			int qty = 0;
			String detail = deleteOrder.getDetail();
			skuId = detail.split(":")[0];
			qty = Integer.parseInt(detail.split(":")[1]);
			String rtnData = "";
			// 调用对方退单接口
			String queryJson = "{\"skuId\":\"" + skuId + "\",\"supplierId\":\"" + deleteOrder.getSupplierId() + "\"}";
			String supplierPrice = HttpUtil45.operateData("post", "json",
					supplierProperties.getSrl().getBusinessApi()+"supplier-sku-handle/supplier-sku", defaultConfig,
					null, queryJson, null, "", "");
			deleteOrder.setLogContent("请求获取srl供应商商品供价参数=================param:" + queryJson);
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.CONFIRM_LOG);
			String requestdata = "{\"sku\":\"" + skuId + "\",\"qty\":\"" + String.valueOf(qty) + "\",\"supplyPrice\":\""
					+ supplierPrice + "\"}";

			if (supplierPrice != null && !supplierPrice.equals("")) {
				rtnData = HttpUtil45.operateData3("post", "json",
						supplierProperties.getSrl().getReturnsUpdate() + "?sellid=" + spOrderId, defaultConfig, null,
						requestdata, null, "", "");
				deleteOrder.setLogContent("退单参数=================param:" + requestdata + "sellid:"+ spOrderId + "返回结果rtnData:" + rtnData);
				logCommon.loggerOrder(deleteOrder, LogTypeStatus.CONFIRM_LOG);
				JSONObject jsStr = (JSONObject) JSONObject.parse(rtnData);
				if (jsStr.get("success").equals(true)) {
					deleteOrder.setRefundTime(new Date());
					deleteOrder.setPushStatus(PushStatus.REFUNDED);
				} else {
					deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
					deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
					deleteOrder.setDescription("退单失败"+rtnData);
				}
			} else {
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
				deleteOrder.setDescription("退单失败"+rtnData);
			}
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			handleException.handleException(deleteOrder, e);
			deleteOrder.setLogContent("退款发生异常============" + e.getMessage());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
		}

	}
	
	public static void main(String[] args) {
//		OutTimeConfig defaultConfig1 = new OutTimeConfig(1000 * 2, 1000 * 60*2, 1000 * 60*2);
		OrderDTO dto = new OrderDTO();
		dto.setSpOrderId("test12323122");
		dto.setSupplierId("2016030701799");
		SrlOrderService t = new SrlOrderService();
		t.handleConfirmOrder(dto);
		t.handleRefundlOrder(dto);
//		try {
//			String skuId = "25350";
//			String supplierId = "2016030701799";
//			String queryJson = "{\"skuId\":\""+skuId+"\",\"supplierId\":\""+supplierId+"\"}";
//			String result  =  HttpUtil45.operateData("post","json",
//					"http://localhost:8003/supplier-sku-handle/supplier-sku",
//					defaultConfig1,null,queryJson,null,"","");
//			System.out.println(result);
//			String sku = "974864";
//			String qty = "1";
//			String supplyPrice = "104.3";
//			String requestdata = "{\"sku\":\""+sku+"\",\"qty\":\""+String.valueOf(qty)+"\",\"supplyPrice\":\""+supplyPrice+"\"}";
//			String result1  =  HttpUtil45.operateData3("post","json",
//			"https://x4ve3rz1nf.execute-api.us-east-1.amazonaws.com/dev/sales-update?sellid=11317125141131",
//			defaultConfig1,null,requestdata,null,"","");
//			JSONObject json = JSONObject.parseObject(result1);
//			System.out.println(result1);
//			if(json.get("success").equals(true)) {
//				System.out.println(11111111);
//			}else {
//				System.out.println(2222222);
//			}
//			System.out.println(result1);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
