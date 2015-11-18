package com.shangpin.iog.tony.purchase.order;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.dto.*;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.ReturnOrderService;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.tony.purchase.common.Constant;
import com.shangpin.iog.tony.purchase.dto.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangyuzhi on 15/10/9.
 */
@Component("tonyOrder")
public class OrderImpl extends AbsOrderService {

    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;
    @Autowired
    SkuPriceService skuPriceService;
    @Autowired
    ReturnOrderService returnOrderService;
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    public void startWMS(){
        //通过采购单处理下单 包括下单和退单
        this.checkoutOrderFromWMS(Constant.SUPPLIER_NO, Constant.SUPPLIER_ID, true);
    }
    
    public void confirmOrder(){
        //通过采购单处理下单 包括下单和退单
        this.confirmOrder(Constant.SUPPLIER_ID);
    }
    
    /**
     * 锁库存
     */
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {
        //在线推送订单
    	orderDTO.setExcState("0");
        createOrder(Constant.PENDING,orderDTO);
        //设置异常信息
    }	
    /**
     * 推送订单
     */
    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {
        //在线推送订单
    	orderDTO.setExcState("0");
    	//createOrder(Constant.CONFIRMED,orderDTO);
        updateOrder(Constant.CONFIRMED,orderDTO);
        //设置异常信息
    }

    /**
     * 在线取消订单
     */
    @Override
    public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
    	deleteOrder.setExcState("0");
        UpdateOrderStatusDTO updateOrder = new UpdateOrderStatusDTO();
        updateOrder.setMerchantId(Constant.MERCHANT_ID);
        updateOrder.setToken(Constant.TOKEN);
        updateOrder.setShopOrderId(deleteOrder.getSpOrderId());
        updateOrder.setStatus(Constant.CANCELED);
                updateOrder.setStatusDate(getUTCTime());
        Gson gson = new Gson();
        String json = gson.toJson(updateOrder,UpdateOrderStatusDTO.class);
        System.out.println("取消订单推送的 json数据： "+json);
        logger.info("取消订单推送的 json数据："+json);
        String rtnData = null;
        try {
            rtnData = HttpUtil45.operateData("post", "json", Constant.url+"updateOrderStatus", null, null, json, "", "");
            System.out.println("取消订单返回的结果："+rtnData);
            logger.info("取消订单返回的结果："+rtnData);
            if(HttpUtil45.errorResult.equals(rtnData)){
            	return ;
            }
            ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
            if ("ko".equals(returnDataDTO.getStatus())){
                deleteOrder.setExcState("1");
                deleteOrder.setExcDesc(returnDataDTO.getMessages().toString());
            } else {
            	deleteOrder.setExcState("0");
                deleteOrder.setStatus(OrderStatus.CANCELLED);
            }
        } catch (ServiceException e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+updateOrder.getShopOrderId());
            deleteOrder.setExcState("1");
            deleteOrder.setExcDesc(e.getMessage());
        } catch (Exception e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+updateOrder.getShopOrderId());
            deleteOrder.setExcState("1");
            deleteOrder.setExcDesc(e.getMessage());
        }
    }
   
	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		
		UpdateOrderStatusDTO updateOrder = new UpdateOrderStatusDTO();
        updateOrder.setMerchantId(Constant.MERCHANT_ID);
        updateOrder.setToken(Constant.TOKEN);
        updateOrder.setShopOrderId(deleteOrder.getSpOrderId());
        updateOrder.setStatus(Constant.CANCELED);
                updateOrder.setStatusDate(getUTCTime());
        Gson gson = new Gson();
        String json = gson.toJson(updateOrder,UpdateOrderStatusDTO.class);
        System.out.println("退款订单推送的 json数据： "+json);
        logger.info("退款订单推送的 json数据："+json);
        String rtnData = null;
        try {
            rtnData = HttpUtil45.operateData("post", "json", Constant.url+"updateOrderStatus", null, null, json, "", "");
            System.out.println("退款订单返回的结果："+rtnData);
            logger.info("退款订单返回的结果："+rtnData);
            if(HttpUtil45.errorResult.equals(rtnData)){
            	return ;
            }
            ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
            if ("ko".equals(returnDataDTO.getStatus())){
                deleteOrder.setExcState("1");
                deleteOrder.setExcDesc(returnDataDTO.getMessages().toString());
            } else {
            	deleteOrder.setExcState("0");
                deleteOrder.setStatus(OrderStatus.REFUNDED);
            }
        } catch (ServiceException e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+updateOrder.getShopOrderId());
            deleteOrder.setExcState("1");
            deleteOrder.setExcDesc(e.getMessage());
        } catch (Exception e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+updateOrder.getShopOrderId());
            deleteOrder.setExcState("1");
            deleteOrder.setExcDesc(e.getMessage());
        } 
		
	}
	 
    /**
     * String:尚品skuNo
     * String:组装后的skuNo
     */
    @Override
    public void getSupplierSkuId(Map<String, String> skuMap) throws ServiceException {

    }

    private void updateOrder(String status,OrderDTO orderDTO){
    	
    	UpdateOrderStatusDTO updateOrder = new UpdateOrderStatusDTO();
        updateOrder.setMerchantId(Constant.MERCHANT_ID);
        updateOrder.setToken(Constant.TOKEN);
        updateOrder.setShopOrderId(orderDTO.getSpOrderId());
        updateOrder.setStatus(status);
        updateOrder.setStatusDate(getUTCTime());
        Gson gson = new Gson();
        String json = gson.toJson(updateOrder,UpdateOrderStatusDTO.class);
        logger.info("支付订单推送的 json数据："+json);
        String rtnData = null;
        try {
            rtnData = HttpUtil45.operateData("post", "json", Constant.url+"updateOrderStatus", null, null, json, "", "");
            System.out.println("支付订单返回的结果："+rtnData);
            logger.info("支付订单返回的结果："+rtnData);
            if(HttpUtil45.errorResult.equals(rtnData)){
            	orderDTO.setExcState("1");
            	orderDTO.setExcDesc(rtnData);
            	return ;
            }
            ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
            if ("ko".equals(returnDataDTO.getStatus())){
            	orderDTO.setExcState("0");
            	orderDTO.setExcDesc(returnDataDTO.getMessages().toString());
            	String result = setPurchaseOrderExc(orderDTO);
				if("-1".equals(result)){
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}else if("1".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
				}else if("0".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
				}
            } else {
            	orderDTO.setExcState("0");
            	orderDTO.setStatus(OrderStatus.CONFIRMED);
            }
        } catch (ServiceException e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+updateOrder.getShopOrderId());
            orderDTO.setExcState("1");
            orderDTO.setExcDesc(e.getMessage());
        } catch (Exception e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+updateOrder.getShopOrderId());
            orderDTO.setExcState("1");
            orderDTO.setExcDesc(e.getMessage());
        } 
    }
    /**
     * 在线推送订单:
     * status未支付：锁库存						
     * status已支付：推单
     */
    private void createOrder(String status,OrderDTO orderDTO){
    	
        //获取订单信息
        PushOrderDTO order = getOrder(status,orderDTO);
        Gson gson = new Gson();
        String json = gson.toJson(order,PushOrderDTO.class);
        String rtnData = null;
        logger.info("锁库存推送的数据："+json);
        System.out.println("锁库存推送的数据=="+json);
        try {
                rtnData = HttpUtil45.operateData("post", "json",  Constant.url+"createOrder", null, null, json, "", "");
                //{"error":"发生异常错误"}
                logger.info("锁库存推送"+status+"订单返回结果=="+rtnData);
                System.out.println("锁库存推送订单返回结果=="+rtnData);
                if(HttpUtil45.errorResult.equals(rtnData)){
                	orderDTO.setExcState("1");
                    orderDTO.setExcDesc(rtnData);
                	return ;
                }
                logger.info("Response ：" + rtnData + ", shopOrderId:"+order.getShopOrderId());

                ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
                if ("ko".equals(returnDataDTO.getStatus())){
                    orderDTO.setExcState("1");
                    orderDTO.setExcDesc(returnDataDTO.getMessages().toString());
                } else if (Constant.PENDING.equals(status)){
                    orderDTO.setStatus(OrderStatus.PLACED);
                } else if (Constant.CONFIRMED.equals(status)){
                    orderDTO.setStatus(OrderStatus.CONFIRMED);
                }
        } catch (ServiceException e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+order.getShopOrderId());
            orderDTO.setExcState("1");
            orderDTO.setExcDesc(e.getMessage());
        } catch (Exception e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+order.getShopOrderId());
            orderDTO.setExcState("1");
            orderDTO.setExcDesc(e.getMessage());
        } 
    }
    /**
     * 获取订单信息
     */
    private PushOrderDTO getOrder(String status,OrderDTO orderDTO){
        
    	
        String detail = orderDTO.getDetail();
        
        String[] details = detail.split(",");
		logger.info("detail数据格式:"+detail);
		int num = 0;
		String skuNo = null;
		for (int i = 0; i < details.length; i++) {
			// detail[i]数据格式==>skuId:数量
			num = Integer.parseInt(details[i].split(":")[1]);
			skuNo = details[i].split(":")[0];
		}
		String markPrice = null;
		try {
			Map tempmap = skuPriceService.getNewSkuPriceBySku(Constant.SUPPLIER_ID, skuNo);
			Map map =(Map) tempmap.get(Constant.SUPPLIER_ID);
			markPrice =(String) map.get(skuNo);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
        ItemDTO[] itemsArr = new ItemDTO[1];
        ItemDTO item = new ItemDTO();
        item.setQty(num);
        item.setSku(skuNo);
        double totalPrice = 0;
        if(!"-1".equals(markPrice)){
        	String price = markPrice.split("\\|")[0];
        	item.setPrice(price);	
        	totalPrice = (Double.parseDouble(price))*num;
        }else{
        	logger.info("没有市场价");
        }
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
        
        PushOrderDTO order = new PushOrderDTO();
        order.setMerchantId(Constant.MERCHANT_ID); 
        order.setToken(Constant.TOKEN);
        order.setShopOrderId(orderDTO.getSpOrderId());
        order.setOrderTotalPrice(totalPrice);
        order.setStatus(status);
    	order.setStatusDate(getUTCTime());
        order.setOrderDate(getUTCTime());
        order.setItems(itemsArr);
        order.setShippingInfo(shippingInfo);
        order.setBillingInfo(billingInfo);
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
	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}
}
