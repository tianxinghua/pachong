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
 * Created by loyalty on 15/9/9.
 */
@Component("tonyOrder")
public class OrderServiceImpl extends AbsOrderService {

    private OrderDTO orderDTO;
    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;
    @Autowired
    ReturnOrderService returnOrderService;
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
/**
 * main
 * */
    public void start(){
        //通过采购单处理下单 包括下单和退单
        this.checkoutOrderFromWMS(Constant.SUPPLIER_ID,"",true);
    }

    /**
     * 在线推送订单：未支付
     */
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {
        //在线推送订单
        createOrder("PENDING");
        //设置异常信息
        orderDTO = this.orderDTO;
    }
    /**
     * 在线推送订单：已支付
     */
    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
        //订单支付确认
        confirmOrder(Constant.SUPPLIER_ID);
        //在线推送订单
        createOrder("CONFIRMED");
        //设置异常信息
        orderDTO = this.orderDTO;
    }

    /**
     * 在线取消订单
     */
    @Override
    public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
        UpdateOrderStatusDTO updateOrder = new UpdateOrderStatusDTO();
        updateOrder.setMerchantId(Constant.MERCHANT_ID);
        updateOrder.setToken(Constant.TOKEN);
/*        updateOrder.setShopOrderId(deleteOrder.getSupplierOrderNo());*/
        updateOrder.setStatus("CANCELED");
        updateOrder.setStatusDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        Gson gson = new Gson();
/*        String json = gson.toJson(updateOrder,UpdateOrderStatusDTO.class);*/
        String json = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\"," +
                "\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"," +
                "\"shopOrderId\":\"aaa\"," +
                "\"status\":\"CANCELED\"," +
                "\"statusDate\":\"2015/01/31 09:01:00\"}";
        System.out.println("request json == "+json);
        String rtnData = null;
        try {
            rtnData = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/updateOrderStatus", null, null, json, "", "");
            System.out.println("rtnData=="+rtnData);
            ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
            System.out.println("------------"+returnDataDTO.getStatus());
            logger.info("Response ：" + rtnData + ", shopOrderId:"+updateOrder.getShopOrderId());
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
                orderDTO.setStatus(OrderStatus.CANCELLED);
            }
            System.out.println("------------"+returnDataDTO.getStatus());
        }
    }
    public void test(ReturnOrderDTO deleteOrder) {
        Gson gson = new Gson();
/*        String json = gson.toJson(updateOrder,UpdateOrderStatusDTO.class);*/
        String json = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\"," +
                "\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"," +
                "\"shopOrderId\":\"aaa\"," +
                "\"status\":\"CANCELED\"," +
                "\"statusDate\":\"2015/01/31 09:01:00\"}";
        System.out.println("request json == "+json);
        String rtnData = null;
        try {
            rtnData = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/updateOrderStatus", null, null, json, "", "");
            System.out.println("rtnData=="+rtnData);
         } catch (ServiceException e) {
           deleteOrder.setExcState("1");
            deleteOrder.setExcDesc(e.getMessage());
        } catch (Exception e) {
            deleteOrder.setExcState("1");
            deleteOrder.setExcDesc(e.getMessage());
        } finally {
            ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
            System.out.println("------------"+returnDataDTO.getStatus());
            System.out.println("------------"+returnDataDTO.getMessages());
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
    private CreateOrderDTO getOrder(String status){
        ShippingInfoDTO shippingInfo = new ShippingInfoDTO();
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
        shippingInfo.setFees("0");
        BillingInfoDTO billingInfo = new BillingInfoDTO();
        billingInfo.setTotal(99);
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
        ItemDTO[] itemsArr = new ItemDTO[1];
        ItemDTO item = new ItemDTO();
        item.setQty(1);
        item.setSku("test");
        item.setPrice(11.00);
        item.setCur(12);
        itemsArr[0] = item;
        CreateOrderDTO order = new CreateOrderDTO();
        order.setMerchantId(Constant.MERCHANT_ID);
        order.setToken(Constant.TOKEN);
        order.setShopOrderId("test1");
        order.setOrderTotalPrice("test2");
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
    private void createOrder(String status){
        //获取订单信息
        CreateOrderDTO order = getOrder(status);
        order.getBillingInfo().setTotal(orderDTO.getDetail());
        order.setShopOrderId(orderDTO.getSupplierOrderNo());
        order.setOrderTotalPrice(orderDTO.getDetail());
        order.getItems()[0].setSku(orderDTO.getSupplierOrderNo());
        order.getItems()[0].setPrice(orderDTO.getSupplierOrderNo());
        order.getItems()[0].setCur(orderDTO.getSupplierOrderNo());
        Gson gson = new Gson();
        String json = gson.toJson(order,CreateOrderDTO.class);
/*        String json = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\"," +
                "\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"," +
                "\"shopOrderId\":\"aaa\"," +
                "\"status\":\"CONFIRMED\"," +
                "\"statusDate\":\"2015/01/31 09:01:00\"," +
                "\"orderDate\":\"2015/01/31 09:01:00\"," +
                "\"items\":[{ \"sku\":\"123\" , \"qty\":1 ," + "\"price\":1 ,\"cur\":135 }]," +
                "\"orderTotalPrice\":555.00," +
                "\"shippingInfo\":{ \"fees\":1 , " +
                "\"address\":{\"firstname\":\"2\", \"lastname\":\"2\"," + " \"companyname\":\"2\"," +
                " \"street\":\"2\", \"hn\":\"2\", \"zip\":\"2\", \"city\":\"2\"," + " \"province\":\"2\" ," +
                "\"state\":\"2\" }}," +
                "\"billingInfo\":{ \"total\":1.00 ," +
                "\"paymentMethod\":1 ," +
                "\"address\":{ \"firstname\":\"2\", \"lastname\":\"2\"," + " \"companyname\":\"2\", " +
                "\"street\":\"2\", \"hn\":\"2\", \"zip\":\"2\", \"city\":\"2\", " +
                "\"province\":\"2\" ,\"state\":\"2\" }}}";*/
        System.out.println("request json == "+json);
        String rtnData = null;
        int qty = Integer.parseInt(orderDTO.getDetail().split(":")[1]);
        try {
            for(int i = 0; i < qty; i++){
                rtnData = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/createOrder", null, null, json, "", "");
                System.out.println("rtnData=="+rtnData);
                logger.info("Response ：" + rtnData + ", shopOrderId:"+order.getShopOrderId());
            }
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
            } else if ("PENDING".equals(status)){
                orderDTO.setStatus(OrderStatus.PLACED);
            } else if ("CONFIRMED".equals(status)){
                orderDTO.setStatus(OrderStatus.PAYED);
            }
            System.out.println("------------"+returnDataDTO.getStatus());
        }
    }
    public static void main(String[] args){
        OrderServiceImpl  orderService = new OrderServiceImpl();

        orderService.test(new ReturnOrderDTO());

//        Map<String,List<PurchaseOrderDetail>> orderMap =  new HashMap<>();
//        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
//        PurchaseOrderDetail  purchaseOrderDetail = new PurchaseOrderDetail();
//        purchaseOrderDetails.add(purchaseOrderDetail);
//        orderMap.put("",purchaseOrderDetails);
//
//        orderService.transData("https://api-sandbox.gilt.com/global/orders/","",orderMap);


    }
}
