package com.shangpin.iog.tony.purchase.order;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.dto.*;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.ReturnOrderService;
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
        createOrder(Constant.CONFIRMED,orderDTO);
        //设置异常信息
    }

    /**
     * 在线取消订单
     */
    @Override
    public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
        UpdateOrderStatusDTO updateOrder = new UpdateOrderStatusDTO();
        updateOrder.setMerchantId(Constant.MERCHANT_ID);
        updateOrder.setToken(Constant.TOKEN);
        updateOrder.setShopOrderId(deleteOrder.getSpOrderId());
        updateOrder.setStatus(Constant.CANCELED);
                updateOrder.setStatusDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
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
        } catch (ServiceException e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+updateOrder.getShopOrderId());
            deleteOrder.setExcState("1");
            deleteOrder.setExcDesc(e.getMessage());
        } catch (Exception e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+updateOrder.getShopOrderId());
            deleteOrder.setExcState("1");
            deleteOrder.setExcDesc(e.getMessage());
        } finally {
            ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
            if ("ko".equals(returnDataDTO.getStatus())){
                deleteOrder.setExcState("1");
                deleteOrder.setExcDesc(returnDataDTO.getMessages().toString());
            } else {
                deleteOrder.setStatus(OrderStatus.CANCELLED);
            }
        }
    }
   
    /**
     * String:尚品skuNo
     * String:组装后的skuNo
     */
    @Override
    public void getSupplierSkuId(Map<String, String> skuMap) throws ServiceException {

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
		
        ItemDTO[] itemsArr = new ItemDTO[1];
        ItemDTO item = new ItemDTO();
        item.setQty(num);
        item.setSku(skuNo);
        item.setPrice(11.00);
        item.setCur(1);
        itemsArr[0] = item;
        double totalPrice = 0;
        if(orderDTO.getPurchasePriceDetail()!=null){
        	  double price = Double.parseDouble(orderDTO.getPurchasePriceDetail());
        	  totalPrice = price*num;
        }
      
        ShippingInfoDTO shippingInfo = new ShippingInfoDTO();
        //运费需要得到
        double fees = 0;
        shippingInfo.setFees(String.valueOf(fees));
        //地址写死就行
        AddressDTO shippingAddress = new AddressDTO();
        shippingAddress.setFirstname("Filippo ");
        shippingAddress.setLastname("Troina");
        shippingAddress.setCompanyname("Genertec Italia S.r.l.");
        shippingAddress.setStreet("VIA G.LEOPARDI 27");	
        shippingAddress.setHn("22075 ");
        shippingAddress.setZip("22075");
        shippingAddress.setCity("LURATE CACCIVIO ");
        shippingAddress.setProvince("como");
        shippingAddress.setState("Italy");
        shippingInfo.setAddress(shippingAddress);
        
        
        BillingInfoDTO billingInfo = new BillingInfoDTO();
        //fees and the orderTotalPrice
        billingInfo.setTotal(totalPrice+fees);
        //1:PayPal,2:postal order,3:bank check,4:Visa / Mastercard credit card,5:American Express credit card,6:cash on delivery,7:bank transfer
        billingInfo.setPaymentMethod(7);
        AddressDTO billingAddress = new AddressDTO();
        billingAddress.setFirstname("Filippo");
        billingAddress.setLastname("Troina");
        billingAddress.setCompanyname("Genertec Italia S.r.l.");
        billingAddress.setStreet("VIA G.LEOPARDI 27");
        billingAddress.setHn("11 ");
        billingAddress.setZip("22075 ");
        billingAddress.setCity("LURATE CACCIVIO");
        billingAddress.setProvince("como");
        billingAddress.setState("Italy");
        billingInfo.setAddress(billingAddress);
        
        
        
        PushOrderDTO order = new PushOrderDTO();
        order.setMerchantId(Constant.MERCHANT_ID); 
        order.setToken(Constant.TOKEN);
        order.setShopOrderId(orderDTO.getSpOrderId());
        order.setOrderTotalPrice(totalPrice);
        order.setStatus(status);
        order.setStatusDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        order.setOrderDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        order.setItems(itemsArr);
        order.setShippingInfo(shippingInfo);
        order.setBillingInfo(billingInfo);
        return  order;
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
        System.out.println("request json == "+json);
        String rtnData = null;
        logger.info("推送的数据："+json);
        System.out.println("rtnData=="+json);
        try {
                rtnData = HttpUtil45.operateData("post", "json", Constant.url+"createOrder", null, null, json, "", "");
                //{"error":"发生异常错误"}
                logger.info("推送"+status+"订单返回结果=="+rtnData);
                System.out.println("推送订单返回结果=="+rtnData);
                if(HttpUtil45.errorResult.equals(rtnData)){
                	return ;
                }
                logger.info("Response ：" + rtnData + ", shopOrderId:"+order.getShopOrderId());
        } catch (ServiceException e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+order.getShopOrderId());
            orderDTO.setExcState("1");
            orderDTO.setExcDesc(e.getMessage());
        } catch (Exception e) {
            loggerError.error("Failed Response ：" + e.getMessage() + ", shopOrderId:"+order.getShopOrderId());
            orderDTO.setExcState("1");
            orderDTO.setExcDesc(e.getMessage());
        } finally {
            ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
            if ("ko".equals(returnDataDTO.getStatus())){
                orderDTO.setExcState("1");
                orderDTO.setExcDesc(returnDataDTO.getMessages().toString());
            } else if (Constant.PENDING.equals(status)){
                orderDTO.setStatus(OrderStatus.PLACED);
            } else if (Constant.CONFIRMED.equals(status)){
                orderDTO.setStatus(OrderStatus.CONFIRMED);
            }
        }
    }
    public static void main(String[] args){
        OrderImpl  orderService = new OrderImpl();

//        orderService.test(new ReturnOrderDTO());

//        Map<String,List<PurchaseOrderDetail>> orderMap =  new HashMap<>();
//        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
//        PurchaseOrderDetail  purchaseOrderDetail = new PurchaseOrderDetail();
//        purchaseOrderDetails.add(purchaseOrderDetail);
//        orderMap.put("",purchaseOrderDetails);
//
//        orderService.transData("https://api-sandbox.gilt.com/global/orders/","",null);


    }

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}
	 
	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}
}
