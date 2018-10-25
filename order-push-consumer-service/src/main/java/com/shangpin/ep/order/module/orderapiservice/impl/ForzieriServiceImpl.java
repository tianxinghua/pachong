package com.shangpin.ep.order.module.orderapiservice.impl;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

import com.shangpin.ep.order.module.order.bean.NewAccessToken;
import com.shangpin.ep.order.module.order.bean.RealStock;
import com.shangpin.ep.order.module.order.bean.TokenDTO;
import com.shangpin.ep.order.module.order.mapper.TokenMapper;
import com.shangpin.ep.order.module.order.service.TokenService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
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
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
@Component("forzieriServiceImpl")
public class ForzieriServiceImpl implements IOrderService {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    LogCommon logCommon;
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    @Autowired
    TokenService tokenService;


    public static void main(String[] args) {
        ForzieriServiceImpl orderService = new ForzieriServiceImpl();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setSpOrderId("201609264074169");
        orderDTO.setDetail("6137974-2015343123475:1,");
        orderDTO.setSupplierSkuNo("az30277-002-001");
        orderDTO.setSpMasterOrderNo("2017062716361");
        orderDTO.setPurchasePriceDetail("685.24");
        orderDTO.setSupplierId("2015103001637");
        orderDTO.setSupplierOrderNo("8798917");
        //orderService.handleSupplierOrder(orderDTO);
        orderService.handleConfirmOrder(orderDTO);
    }


    public String handleException(String url, Map<String,String> param, OutTimeConfig outTimeConf, String userName, String password,OrderDTO order,Throwable e){
        handleException.handleException(order, e);
        return null;
    }


    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {

        String placeOrderUrl = "https://api.forzieri.com/v3/orders";

        Gson gson = new Gson();
        String jsonValue = gson.toJson(getOrderParam(orderDTO));
        System.out.println("调用getOrderParam方法得到的order"+jsonValue);
        String s="";
        try {
            s = forzieriPost(placeOrderUrl, "post", jsonValue, orderDTO);
            Gson gson1 = new Gson();
            OrderResponse response1 = gson1.fromJson(s, OrderResponse.class);
            if(s!=null&&"success".equals(response1.getStatus())){
                orderDTO.setLockStockTime(new Date());
                orderDTO.setPushStatus(PushStatus.LOCK_PLACED);
                orderDTO.setSupplierOrderNo(response1.getData().getOrder_id());
                orderDTO.setLogContent("------锁库结束-------");
                logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
            }
        } catch (Exception e) {
            orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
            orderDTO.setDescription("查询对方接口失败,对方返回的信息是："+s);
            orderDTO.setPushStatus(PushStatus.LOCK_PLACED_ERROR);
            handleException.handleException(orderDTO,e);
            orderDTO.setLogContent("推送订单返回结果："+s);
            logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
        }

    }

    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {

        String confirmOrderUrl = "https://api.forzieri.com/v3/orders/"+orderDTO.getSupplierOrderNo();
        String returnData = null;
        try {
            JSONObject jsonValue = new JSONObject();
            jsonValue.put("status","approved");
            returnData = confirmPost(confirmOrderUrl, "post", jsonValue.toJSONString(), orderDTO);
            System.out.println("=============================="+returnData);
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
       /* deleteOrder.setCancelTime(new Date());
        deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API);*/
        String confirmOrderUrl = "https://api.forzieri.com/v3/orders/"+deleteOrder.getSupplierOrderNo();
        String returnData = null;
        try {
            JSONObject jsonValue = new JSONObject();
            jsonValue.put("status","cancelled");
            returnData = confirmPost(confirmOrderUrl, "post", jsonValue.toJSONString(), deleteOrder);
            System.out.println("=============================="+returnData);
            Gson gson = new Gson();
            OrderResponse response = gson.fromJson(returnData, OrderResponse.class);
            if(response!=null&&"success".equals(response.getStatus())){
                deleteOrder.setCancelTime(new Date());
                deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED);
            }
            //{"status":"success","data":{"message":"Order status updated"}}

        } catch (Exception e) {
            deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED_ERROR);
            deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
            deleteOrder.setDescription(deleteOrder.getLogContent());
            handleException.handleException(deleteOrder,e);
            deleteOrder.setLogContent("推送订单返回结果： "+returnData);
            logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_CANCELLED_LOG);
        }
    }

    @Override
    public void handleRefundlOrder(OrderDTO deleteOrder) {
        String confirmOrderUrl = "https://api.forzieri.com/v3/orders/"+deleteOrder.getSupplierOrderNo();
        String returnData = null;
        try {
            JSONObject jsonValue = new JSONObject();
            jsonValue.put("status", "cancelled");
            returnData = confirmPost(confirmOrderUrl, "post", jsonValue.toJSONString(), deleteOrder);
            System.out.println("==============================" + returnData);
            if(HttpUtil45.errorResult.equals(returnData)){
                deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
                deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
                deleteOrder.setDescription(deleteOrder.getLogContent());
                return ;
            }
            Gson gson = new Gson();
            OrderResponse returnDataDTO = gson.fromJson(returnData, OrderResponse.class);
            if ("success".equals(returnDataDTO.getStatus())){
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
        /*String merchant_reference = orderDTO.getSpMasterOrderNo();
        order.setMerchant_reference(merchant_reference);*/
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
        List<Items> items1 = new ArrayList<Items>();
        Items item = new Items();
        item.setMerchant_sku("");
        item.setQuantity("1");
        String skuId  = "";
        String detail = orderDTO.getDetail();
        if (detail != null) {
            skuId = detail.split(":")[0];
        }
        item.setSku(skuId);
        items1.add(item);
        order.setItems(items1);
        return order;
    }





    public  String forzieriPost(String url,String method,String jsonValue,OrderDTO order) throws Exception {
        TokenDTO tokenDTO = tokenService.findToken("2015103001637");
        String accessToken = tokenDTO.getAccessToken();
        Gson gson = new Gson();
        String s = "";
        String skuId = "";
        String detail = order.getDetail();
        if (detail != null) {
            skuId = detail.split(":")[0];
        }
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("https://api.forzieri.com/v3/products/" + skuId);
        getMethod.setRequestHeader("Authorization", "Bearer " + accessToken);

        int httpCode = httpClient.executeMethod(getMethod);
        //判断httpCode，404商品未找到...401 accessToken过期,200得到数据
        if (httpCode == 200) {
            String data = getMethod.getResponseBodyAsString();
            RealStock realStock = gson.fromJson(data, RealStock.class);
            int qty = realStock.getData().getQty();
            if (qty > 0) {
                Map<String, String> headerMap1 = new HashMap<String, String>();
                headerMap1.put("Authorization", "Bearer " + accessToken + "");
                s = HttpUtil45.operateData(method, "json", url, new OutTimeConfig(1000 * 60 * 1, 1000 * 60 * 1, 1000 * 60 * 1), null, jsonValue, headerMap1, null, null);
                System.out.println(s);
                OrderResponse orderResponse = gson.fromJson(s, OrderResponse.class);
            }

        }
        return s;
    }
        public String confirmPost (String url, String method, String jsonValue, OrderDTO order) throws Exception {
            TokenDTO tokenDTO1 = tokenService.findToken("2015103001637");
            String accessToken1 = tokenDTO1.getAccessToken();
            Map<String, String> headerMap1 = new HashMap<String, String>();
            headerMap1.put("Authorization", "Bearer " + accessToken1 + "");
            String s1 = HttpUtil45.operateData(method, "json", url, new OutTimeConfig(1000 * 60 * 1, 1000 * 60 * 1, 1000 * 60 * 1), null, jsonValue, headerMap1, null, null);
            return s1;
        }
    }
