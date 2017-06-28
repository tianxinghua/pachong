package com.shangpin.ep.order.module.orderapiservice.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri.BillingAddress;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri.Item;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri.OrderParam;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri.OrderResponse;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri.ShippingAddress;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri.Shopper;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
@Component("forzieriServiceImpl")
public class ForzieriServiceImpl implements IOrderService {

	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    
	public static void main(String[] args) {
		ForzieriServiceImpl orderService = new ForzieriServiceImpl();
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setSpOrderId("201609264074169");
    	orderDTO.setDetail("6137974-2015343123475:1,");
    	orderDTO.setSupplierSkuNo("az30277-002-001");
    	orderDTO.setSpMasterOrderNo("2017062716361");
    	orderDTO.setPurchasePriceDetail("685.24");
    	orderDTO.setSupplierOrderNo("8798917");
//    	orderService.handleSupplierOrder(orderDTO);
    	orderService.handleConfirmOrder(orderDTO);
	}
    
    /**
     * 给对方推送数据
     * @param url
     * @param param
     * @param outTimeConf
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public String forzieriPost(String url,String method,String jsonValue,OrderDTO order) throws Exception{
    	Map<String,String> headerMap = new HashMap<String,String>();
    	headerMap.put("Authorization","Bearer b8abbb17240e6b95ed2552bea4bdcb856d58e615");
    	return HttpUtil45.operateData(method, "json", url, new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1), null, jsonValue, headerMap, null,null);
    }
    
    public String handleException(String url, Map<String,String> param, OutTimeConfig outTimeConf, String userName, String password,OrderDTO order,Throwable e){
    	handleException.handleException(order, e); 
		return null;
    }
    
   
    @Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
    	String placeOrderUrl = "https://api.forzieri.com/test/orders";
    	
		Gson gson = new Gson();
		String jsonValue = gson.toJson(getOrderParam(orderDTO));
		try {
			String s = forzieriPost(placeOrderUrl, "post", jsonValue, orderDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
    	String confirmOrderUrl = "https://api.forzieri.com/test/orders/"+orderDTO.getSupplierOrderNo();
    	String returnData = null;
		try {
			JSONObject jsonValue = new JSONObject();
			jsonValue.put("status","approved");
			returnData = forzieriPost(confirmOrderUrl, "post", jsonValue.toJSONString(), orderDTO);
			Gson gson = new Gson();
			OrderResponse response = gson.fromJson(returnData, OrderResponse.class);
			if(response!=null&&"success".equals(response.getStatus())){
				orderDTO.setConfirmTime(new Date()); 
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
			}
			//{"status":"success","data":{"message":"Order status updated"}}
			
		} catch (Exception e) {
			
			orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);	
			orderDTO.setDescription("查询对方库存接口失败,对方返回的信息是："+returnData);
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单返回结果： "+returnData);
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
		try {
			String spOrderId = deleteOrder.getSpOrderId();
			if(spOrderId.contains("-")){
				spOrderId = spOrderId.substring(0, spOrderId.indexOf("-")); 
			}
			String returnData = null;
//			returnData = setStatusOrderMarketplace(spOrderId,"CANCELED",deleteOrder);
			if(returnData.contains("OK")){
				deleteOrder.setRefundTime(new Date());
				deleteOrder.setPushStatus(PushStatus.REFUNDED);
			}else{
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
				deleteOrder.setDescription(returnData);
			}				
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			handleException.handleException(deleteOrder, e); 
			deleteOrder.setLogContent("退款发生异常============"+e.getMessage());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);			
		}
		
	}
	
	 private OrderParam getOrderParam(OrderDTO orderDTO){
	    	OrderParam order = new OrderParam();
	    	BillingAddress billing_address = new BillingAddress();
	    	billing_address.setAddress_line1("1");
	    	billing_address.setAddress_line2("2");
	    	billing_address.setCountry("US");
	    	billing_address.setFull_name("John Doe");
	    	billing_address.setLocality("Anytown");
	    	billing_address.setPhone_number("123-456-7890");
	    	billing_address.setPostal_code("90210");
	    	billing_address.setRegion("California");
	    	order.setBilling_address(billing_address);
	    	String merchant_reference = orderDTO.getSpMasterOrderNo();
	    	order.setMerchant_reference(merchant_reference);
	    	ShippingAddress shipping_address = new ShippingAddress();
	    	shipping_address.setAddress_line1("1");
	    	shipping_address.setAddress_line2("2");
	    	shipping_address.setCountry("US");
	    	shipping_address.setFull_name("John Doe");
	    	shipping_address.setLocality("Anytown");
	    	shipping_address.setPhone_number("123-456-7890");
	    	shipping_address.setPostal_code("90210");
	    	shipping_address.setRegion("California");
	    	order.setShipping_address(shipping_address);
	    	Shopper shopper = new Shopper();
	    	shopper.setEmail("username@example.com");
	    	order.setShopper(shopper);
	    	List<Item> items = new ArrayList<Item>();
	    	Item item = new Item();
	    	item.setMerchant_sku("12222");
	    	item.setQuantity("1");
	    	item.setSku(orderDTO.getSupplierSkuNo());
	    	items.add(item);
	    	order.setItems(items);
	    	
	    	return order;
	    }

	 //读取本地文件并转为字符串
    private static String getJson(String fileName) {
		String fullFileName = "E:/" + fileName + ".json";
		File file = new File(fullFileName);
		Scanner scanner = null;
		StringBuilder buffer = new StringBuilder();
		try {
			scanner = new Scanner(file, "utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
			}
		} catch (Exception e) {

		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		System.out.println(buffer.toString());
		return buffer.toString();
	}
	
}
