package com.shangpin.ep.order.module.orderapiservice.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.AdditionalInfo;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.AddressDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.BillingInfoDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.Data;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.ItemDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.ObjectOrder;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.PushOrderDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.ReturnDataDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.ReturnObject;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.ShippingInfoDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.tony.UpdateOrderStatusDTO;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;

/**
 * Created by wangyuzhi on 15/10/9.
 */
@Component("tonyOrderImpl")
public class TonyOrderImpl implements IOrderService {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    private  String merchantId = null;
    private  String token = null;
    private  String url = null;
    
    @PostConstruct
    public void init(){
    	url = supplierProperties.getTonyConf().getUrl();
    	token = supplierProperties.getTonyConf().getToken();
    	merchantId = supplierProperties.getTonyConf().getMerchantId();
    }
    
    public static String CANCELED = "CANCELED";
    public static String PENDING = "PENDING";
    public static String CONFIRMED = "CONFIRMED";
    /**
     * 锁库存
     */
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {
        //在线推送订单
        createOrder(PENDING,orderDTO);
        //设置异常信息
    }	
    
    /**
     * 推送订单
     */
    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {
        //在线推送订单
    	//createOrder(CONFIRMED,orderDTO);
        updateOrder(CONFIRMED,orderDTO);
        //设置异常信息
    }

    /**
     * 在线取消订单
     */
    @SuppressWarnings("static-access")
	@Override
    public void handleCancelOrder(OrderDTO orderDTO) {
        UpdateOrderStatusDTO updateOrder = new UpdateOrderStatusDTO();
        updateOrder.setMerchantId(merchantId);
        updateOrder.setToken(token);
        updateOrder.setShopOrderId(orderDTO.getSpOrderId());
        updateOrder.setStatus(CANCELED);
                updateOrder.setStatusDate(getUTCTime());
        Gson gson = new Gson();
        String json = gson.toJson(updateOrder,UpdateOrderStatusDTO.class);
        String rtnData = null;
        try {
        	
        	 rtnData = tonyPushOrder(orderDTO,url+"updateOrderStatus", json);
         	 orderDTO.setLogContent("取消订单推送返回结果=="+rtnData+"，取消订单推送的数据："+json);
         	 logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
        	
            if(HttpUtil45.errorResult.equals(rtnData)){
            	orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED_ERROR);
            	orderDTO.setErrorType(ErrorStatus.API_ERROR);
            	orderDTO.setDescription(orderDTO.getLogContent());
            	return ;
            }
            ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
            if ("ok".equals(returnDataDTO.getStatus())){
            	orderDTO.setCancelTime(new Date());
            	orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED);
            } else{
            	 orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED_ERROR);
               	 orderDTO.setErrorType(ErrorStatus.API_ERROR);
               	 orderDTO.setDescription(orderDTO.getLogContent());
            }
        } catch (Exception e) {
        	orderDTO.setPushStatus(PushStatus.LOCK_CANCELLED_ERROR);
        	orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
        	orderDTO.setDescription(e.getMessage());
        	orderDTO.setLogContent(e.getMessage());
        } 
    }
   
	@SuppressWarnings("static-access")
	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		
		UpdateOrderStatusDTO updateOrder = new UpdateOrderStatusDTO();
        updateOrder.setMerchantId(merchantId);
        updateOrder.setToken(token);
        updateOrder.setShopOrderId(deleteOrder.getSpOrderId());
        updateOrder.setStatus(CANCELED);
                updateOrder.setStatusDate(getUTCTime());
        Gson gson = new Gson();
        String json = gson.toJson(updateOrder,UpdateOrderStatusDTO.class);
        String rtnData = null;
        try {
        	
       	 rtnData = tonyPushOrder(deleteOrder,url+"updateOrderStatus", json);
       	 deleteOrder.setLogContent("退款返回结果=="+rtnData+"，退款推送的数据："+json);
     	 logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_LOG);
        	
            if(HttpUtil45.errorResult.equals(rtnData)){
            	deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
            	deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
                deleteOrder.setDescription(deleteOrder.getLogContent());
            	return ;
            }
            ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
            if ("ok".equals(returnDataDTO.getStatus())){
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
            deleteOrder.setDescription(e.getMessage());
            deleteOrder.setLogContent(e.getMessage());
        }
		
	}
	 
	private String tonyPushOrder(OrderDTO orderDTO, String url, String json)  throws Exception{
		
		return HttpUtil45.operateData("post", "json", url, null, null, json,null, "", "");
	}
	public String handleException(OrderDTO orderDTO, String url, String json,Throwable e){		
		handleException.handleException(orderDTO, e); 
		return null;
	}
    @SuppressWarnings("static-access")
	private void updateOrder(String status, OrderDTO orderDTO){
    	
    	UpdateOrderStatusDTO updateOrder = new UpdateOrderStatusDTO();
        updateOrder.setMerchantId(merchantId);
        updateOrder.setToken(token);
        updateOrder.setShopOrderId(orderDTO.getSpOrderId());
        updateOrder.setStatus(status);
        updateOrder.setStatusDate(getUTCTime());
        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setPurchase_id(orderDTO.getPurchaseNo());
        updateOrder.setAdditionalInfo(additionalInfo);
        Gson gson = new Gson();
        String json = gson.toJson(updateOrder,UpdateOrderStatusDTO.class);
        String rtnData = null;
        try {
            rtnData = tonyPushOrder(orderDTO,url+"updateOrderStatus", json);
        	orderDTO.setLogContent("支付订单推送返回结果=="+rtnData+"，支付订单推送的数据："+json);
        	logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
            
            if(HttpUtil45.errorResult.equals(rtnData)){
            	orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
            	orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
            	orderDTO.setLogContent(rtnData);
            	return ;
            }
            
            ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
            if ("ok".equals(returnDataDTO.getStatus())){
            	 orderDTO.setConfirmTime(new Date());
             	 orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
            } else {
            	if("[Quota exceeded]".equals(returnDataDTO.getMessages().toString())){
            		orderDTO.setDescription(orderDTO.getLogContent());
            		orderDTO.setErrorType(ErrorStatus.API_ERROR);
                	orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
            	}else if("Shop order id - Shop order id - Order not found".equals(returnDataDTO.getMessages().toString())){
            		orderDTO.setErrorType(ErrorStatus.API_ERROR);
                	orderDTO.setDescription(orderDTO.getLogContent());
                	orderDTO.setPushStatus(PushStatus.NO_STOCK);
            	}else{
                	orderDTO.setDescription(orderDTO.getLogContent());
                	orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
            	}
            }
        } catch (Exception ex) {
        	orderDTO.setDescription(ex.getMessage());
        	orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
        	orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
        	orderDTO.setLogContent(ex.getMessage());
        } 
    }
    
    /**
     * 在线推送订单:
     * status未支付：锁库存						
     * status已支付：推单
     */
    @SuppressWarnings("all")
	private void createOrder(String status, OrderDTO orderDTO){
    	
        //获取订单信息
        PushOrderDTO order = getOrder(status,orderDTO);
        Gson gson = new Gson();
        String json = gson.toJson(order,PushOrderDTO.class);
        String rtnData = null;
        try {
        	rtnData = tonyPushOrder(orderDTO,url+"createOrder", json);
        	orderDTO.setLogContent("锁库存推送返回结果=="+rtnData+"，锁库存推送的数据："+json);
        	logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
	    	  if(HttpUtil45.errorResult.equals(rtnData)){
	    		  orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
	    		  orderDTO.setPushStatus(PushStatus.LOCK_PLACED_ERROR);
	    		  return;
	          }
	    	 
	          ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
	          if ("ok".equals(returnDataDTO.getStatus())){
	        	  	orderDTO.setLockStockTime(new Date());
	            	orderDTO.setPushStatus(PushStatus.LOCK_PLACED);
	            }else {
	            	if("[Quota exceeded]".equals(returnDataDTO.getMessages().toString())){
	            		orderDTO.setDescription(orderDTO.getLogContent());
	            		orderDTO.setErrorType(ErrorStatus.API_ERROR);
	                	orderDTO.setPushStatus(PushStatus.LOCK_PLACED_ERROR);
	            	}else if(returnDataDTO.getMessages().toString().endsWith("quantity not available in the stock")){
	            		orderDTO.setDescription(orderDTO.getLogContent());
	                	orderDTO.setPushStatus(PushStatus.NO_STOCK);
	            		
	            	}else{
	            		orderDTO.setErrorType(ErrorStatus.API_ERROR);
	            		orderDTO.setDescription(orderDTO.getLogContent());
	                	orderDTO.setPushStatus(PushStatus.LOCK_PLACED_ERROR);
	            	}
	            }
        } catch (Exception e) {
            orderDTO.setPushStatus(PushStatus.LOCK_PLACED_ERROR);
            orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
            orderDTO.setLogContent(e.getMessage());
        } 
    }
    /**
     * 获取订单信息
     */
    private PushOrderDTO getOrder(String status, OrderDTO orderDTO){
    	
    	PushOrderDTO order = null;
		try {
		     String detail = orderDTO.getDetail();
			logger.info("detail数据格式:"+detail);
			int num = 0;
			String skuNo = null;
			num = Integer.parseInt(detail.split(":")[1]);
			skuNo = detail.split(":")[0];
			
			ItemDTO[] itemsArr = new ItemDTO[1];
	        ItemDTO item = new ItemDTO();
	        item.setQty(num);
	        item.setSku(skuNo);
	        double totalPrice = 0;
        	String price = orderDTO.getPurchasePriceDetail();
        	item.setPrice(price);	
        	totalPrice = (Double.parseDouble(price))*num;
	        item.setCur(1);
	        itemsArr[0] = item;
	        
	        ShippingInfoDTO shippingInfo = new ShippingInfoDTO();
	        //运费需要得到
	        double fees = 0;
	        shippingInfo.setFees(String.valueOf(fees));
	        //地址写死就行
	        AddressDTO address = new AddressDTO();
	        address.setFirstname("Filippo ");
	        address.setLastname("Troina");
	        address.setCompanyname("Genertec Italia S.r.l.");
	        address.setStreet("VIA G.LEOPARDI 27");	
	        address.setHn("22075 ");
	        address.setZip("22075");
	        address.setCity("LURATE CACCIVIO ");
	        address.setProvince("como");
	        address.setState("Italy");
	        shippingInfo.setAddress(address);
	        
	        BillingInfoDTO billingInfo = new BillingInfoDTO();
	        //fees and the orderTotalPrice
	        billingInfo.setTotal(totalPrice+fees);
	        //1:PayPal,2:postal order,3:bank check,4:Visa / Mastercard credit card,5:American Express credit card,6:cash on delivery,7:bank transfer
	        billingInfo.setPaymentMethod(7);
	        billingInfo.setAddress(address);
	        
	        order = new PushOrderDTO();
	        order.setMerchantId(merchantId); 
	        order.setToken(token);
	        order.setShopOrderId(orderDTO.getSpOrderId());
	        order.setOrderTotalPrice(totalPrice);
	        order.setStatus(status);
	    	order.setStatusDate(getUTCTime());
	        order.setOrderDate(getUTCTime());
	        order.setItems(itemsArr);
	        order.setShippingInfo(shippingInfo);
	        order.setBillingInfo(billingInfo);
		} catch (Exception e) {
			loggerError.info(e);
			e.printStackTrace();
		}
        return  order;
    }

    private static String getUTCTime(){
    	// 1、取得本地时间：  
        Calendar cal = Calendar.getInstance() ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
        // 2、取得时间偏移量：  
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
        return sdf.format(cal.getTime());
    }
	private  String getReturnOrder(String orderId){
		
		String str = null;
		ObjectOrder obj = new ObjectOrder();
		obj.setMerchantId(merchantId);
		String[] arr = {orderId};
		obj.setShopOrderIds(arr);
		obj.setToken(token);
		Gson gson = new Gson();
		String json = gson.toJson(obj, ObjectOrder.class);
		try {
            String rtnData = HttpUtil45.operateData("post", "json", url +"getOrders", null, null, json, null,"", "");
            System.out.println("rtnData=="+rtnData);
            if(HttpUtil45.errorResult.equals(rtnData)){
            	str = "no";
            }else{
            	ReturnObject returnObj =gson.fromJson(rtnData, ReturnObject.class);
            	if("ok".equals(returnObj.getStatus())){
            		List<Data> list = returnObj.getData();
            		if(list!=null){
            			if(list.size()>0){
            				str = list.get(0).getOrder_status();
            			}
            		}
            	}else{
            		str = "none";
            	}
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
		return str;
	}
}
