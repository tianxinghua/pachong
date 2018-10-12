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

    /**
     * 给对方推送数据

     * @return
     * @throws Exception
     */


/*    public  String forzieriPost(String url,String method,String jsonValue,OrderDTO order) throws Exception{
    	Map<String,String> headerMap = new HashMap<String,String>();

        TokenDTO tokenDTO = tokenService.findToken("2015103001637");
        String accessToken = tokenDTO.getAccessToken();
        System.out.println("accessToken的值是："+accessToken);
    	headerMap.put("Authorization","Bearer "+accessToken+"");
    //    headerMap.put("Authorization","Bearer ec3b6ab7302531268294a000f17968dc8e438b9f");
        return HttpUtil45.operateData(method, "json", url, new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1), null, jsonValue, headerMap, null,null);
    }
    */
    public String handleException(String url, Map<String,String> param, OutTimeConfig outTimeConf, String userName, String password,OrderDTO order,Throwable e){
        handleException.handleException(order, e);
        return null;
    }


    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {

        String placeOrderUrl = " https://api.forzieri.com/v3/orders";

        Gson gson = new Gson();
        String jsonValue = gson.toJson(getOrderParam(orderDTO));
        System.out.println("调用getOrderParam方法得到的order"+jsonValue);
        String s="";
        try {
            s = forzieriPost(placeOrderUrl, "post", jsonValue, orderDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderDTO.setLockStockTime(new Date());
        orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
        orderDTO.setLogContent("------锁库结束-------");
        orderDTO.setSpOrderId(s);
        System.out.println("s的值是："+s);
        logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
    }

    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {

        String confirmOrderUrl = "https://api.forzieri.com/v3/orders/"+orderDTO.getSupplierOrderNo();
        String returnData = null;
        try {
            JSONObject jsonValue = new JSONObject();
            jsonValue.put("status","approved");
            returnData = forzieriPost(confirmOrderUrl, "post", jsonValue.toJSONString(), orderDTO);
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



    public  String forzieriPost(String url,String method,String jsonValue,OrderDTO order) throws Exception{
        TokenDTO tokenDTO = tokenService.findToken("2015103001637");
        String skuId  = order.getSpSkuNo();
        String accessToken = tokenDTO.getAccessToken();
        String refreshToken = tokenDTO.getRefreshToken();

        HttpClient httpClient = new HttpClient();
        Gson gson = new Gson();
        String  s="";
        GetMethod getMethod = new GetMethod("https://api.forzieri.com/v3/products/"+skuId);
        getMethod.setRequestHeader("Authorization", "Bearer "+accessToken);

        int httpCode = httpClient.executeMethod(getMethod);
        //判断httpCode，404商品未找到...401 accessToken过期,200得到数据
        if (httpCode==200) {
            String realSku = getMethod.getResponseBodyAsString();
            RealStock realStock = gson.fromJson(realSku, RealStock.class);
            int  qty= realStock.getData().getQty() ;
            if(qty>0){
                Map<String,String> headerMap = new HashMap<String,String>();
                headerMap.put("Authorization","Bearer "+accessToken+"");
                s= HttpUtil45.operateData(method, "json", url, new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1), null, jsonValue, headerMap, null,null);
                System.out.println(s);
            }
        }else if (httpCode==404){
            // 产品未找到
            logger.info(skuId+"产品未找到");

        }else if (httpCode==401) {
            //access_token过期
            //刷新Token,更改刷新后的数据库,
            // 存入map
            logger.info("accessToken过期");
//				PostMethod postMethod = new PostMethod("https://api.forzieri.com/test/oauth/token");//测试
            String   tokenurl="https://api.forzieri.com/v2/oauth/token?grant_type=refresh_token&client_id=NTY0MjBmOWZiZjI3OTc5&client_secret=9470b9341606430e3b36871541732865e0f51979&refresh_token="+refreshToken+"";
            GetMethod tokenMethod = new GetMethod(tokenurl);
            logger.info("refreshToken的值是"+refreshToken);
            int tokenCode = httpClient.executeMethod(tokenMethod);
            logger.info("executeMethod的值是"+tokenCode);
            if (tokenCode==200) {

                NewAccessToken newAccessToken = gson.fromJson(tokenMethod.getResponseBodyAsString(), NewAccessToken.class);
                String  accessToken1 = newAccessToken.getAccess_token();
                String refreshToken1 = newAccessToken.getRefresh_token();
                tokenDTO.setAccessToken(accessToken1);
                tokenDTO.setRefreshToken(refreshToken1);
                tokenDTO.setCreateDate(new Date());
                tokenDTO.setExpireTime(newAccessToken.getExpires_in());
                tokenService.refreshToken(tokenDTO);
                TokenDTO TokenDTO2 = tokenService.findToken("2015103001637");
                String accessToken2 =TokenDTO2.getAccessToken();
                Map<String,String> headerMap = new HashMap<String,String>();
                headerMap.put("Authorization","Bearer "+accessToken2+"");
                s= HttpUtil45.operateData(method, "json", url, new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1), null, jsonValue, headerMap, null,null);
                System.out.println(s);
                if (httpCode==200) {
                    String realSku2 = getMethod.getResponseBodyAsString();
                    RealStock realStock2 = gson.fromJson(realSku2, RealStock.class);
                }else if (httpCode==404){
                    // 产品未找到
                    logger.info(skuId+"产品未找到");
                }else{
                    //服务器错误
                    loggerError.error(skuId+"服务器错误");
                    System.out.println(skuId+"服务器错误"+httpCode);
                }
            }else{
                loggerError.error(skuId+"刷新token错误"+getMethod.getResponseBodyAsString());
                System.out.println(skuId+"刷新token错误"+getMethod+getMethod.getResponseBodyAsString());
            }
        }else{
            //服务器错误
            loggerError.error(skuId+"服务器错误");
            System.out.println(skuId+"服务器错误"+httpCode);
        }
        //  }
        return s;
    }
 /*  public String forzieriPost(String url,String method,String jsonValue,OrderDTO order) throws Exception{
        Map<String,String> headerMap = new HashMap<String,String>();
        headerMap.put("Authorization","Bearer 38ca9706b29a098c209c995e5390822e0c3a1432");
        return HttpUtil45.operateData(method, "json", url, new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1), null, jsonValue, headerMap, null,null);
    }
*/
}
