package com.shangpin.ep.order.module.orderapiservice.impl.prestashop;

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
import com.shangpin.ep.order.module.orderapiservice.impl.Creative99ServiceImpl;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.CommonService;

import com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri.OrderResponse;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri.Shopper;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.vietti2.*;
import com.shangpin.ep.order.module.sku.mapper.HubSkuMapper;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import com.shangpin.ep.order.util.utils.OAuth;
import com.shangpin.ep.order.util.utils.SkuDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import java.util.*;

@Component("vietti2ServiceImpl")
public class Vietti2ServiceImpl  implements IOrderService {

    @Autowired
    LogCommon logCommon;
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;

    @Autowired
    private CommonService commonService;

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
    public String creative99Post(String url, Map<String,String> param, OutTimeConfig outTimeConf, String userName, String password, OrderDTO order) throws Exception{
        return HttpUtil45.postAuth(url, param, outTimeConf, userName, password);
    }

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
               String data="";
               String  sku= orderDTO.getSupplierSkuNo();
               String   URL=    String.format("https://www.viettishop.com/api/rest/marketplaces/stock_check/%s", sku);
                try {
                    data = OAuth.getData ("GET",URL,"de924d99f10d11f0f881ee77cbdf254e","85f2923d3f8fdc376cda2633f10ee204","6637e3905e90dedb3cc177f930eba2dc","1.0","HMAC-SHA1");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                Gson gson = new Gson();
                SkuDTO skuDTO = gson.fromJson(data, SkuDTO.class);
                int  qty1=   skuDTO.getQty();
                    //如果库存大于0,则下单
                  if(qty1 > 0){
                        String placeOrderUrl = "https://www.viettishop.com/api/rest/marketplaces/place_order";
                        String jsonValue = gson.toJson(getOrderParam(orderDTO));
                            String returnData = null;
                            try {
                            returnData = forzieriPost(placeOrderUrl, "get", jsonValue, orderDTO);
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
            if(spOrderId.contains("-")){
                spOrderId = spOrderId.substring(0, spOrderId.indexOf("-"));
            }
            String returnData = setStatusOrderMarketplace(spOrderId,"CANCELED",deleteOrder);
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



    /**
     * 设置订单状态
     * @param code 订单号
     * @param status 可取值为:NEW, PROCESSING, SHIPPED, CANCELED
     */
    @SuppressWarnings("static-access")
    private String setStatusOrderMarketplace(String code, String status,OrderDTO orderDTO) throws Exception {
        Map<String,String> param = new HashMap<String,String>();
        param.put("CODE", code);
        param.put("STATUS", status);//NEW PROCESSING SHIPPED CANCELED (for delete ORDER)
        orderDTO.setLogContent("设置订单参数======="+param.toString());
        logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
        String returnData = creative99Post(supplierProperties.getCreative99().getUrl()+supplierProperties.getCreative99().getSetStatusInterface(), param, new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2),supplierProperties.getCreative99().getUser(),supplierProperties.getCreative99().getPassword(),orderDTO);
        orderDTO.setLogContent("设置订单状态返回结果======="+returnData);
        logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
        return returnData;
    }




    public static void main(String[] args) {


        String data="";
        String  sku="6697571_44";
        String   URL=    String.format("https://www.viettishop.com/api/rest/marketplaces/stock_check/%s", sku);
        try {
            data = OAuth.getData ("GET",URL,"de924d99f10d11f0f881ee77cbdf254e","85f2923d3f8fdc376cda2633f10ee204","6637e3905e90dedb3cc177f930eba2dc","1.0","HMAC-SHA1");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Gson gson = new Gson();
        SkuDTO skuDTO = gson.fromJson(data, SkuDTO.class);
        int  qty1=   skuDTO.getQty();
        System.out.println(qty1);


    }


    private OrderParam getOrderParam(OrderDTO orderDTO){
        OrderParam order = new OrderParam();
        BillingAddress billing_address = new BillingAddress();
        billing_address.setFirstname("First Name");
        billing_address.setLastname("Last Name");
        billing_address.setEmail("username@domain.com");
        billing_address.setStreet("Street");
        billing_address.setCity("City");
        billing_address.setCountry_id("Country Code ISO 3166-1\n" +
                "    alpha-2");
        billing_address.setRegion("Region");
        billing_address.setPostcode("Postcode");
        billing_address.setTelephone("Telephone");
        billing_address.setVat_id("VAT ID");
        order.setBilling_address(billing_address);
   /*     String merchant_reference = orderDTO.getSpMasterOrderNo();
        order.setMerchant_reference(merchant_reference);*/
        ShippingAddress shipping_address = new ShippingAddress();
        shipping_address.setFirstname("First Name");
        shipping_address.setLastname("Last Name");
        shipping_address.setStreet("Street");
        shipping_address.setCity("City");
        shipping_address.setCountry_id("Country Code ISO 3166-1 \n" +
                "alpha-2");
        shipping_address.setRegion("Region");
        shipping_address.setPostcode("Postcode");
        shipping_address.setTelephone("Telephone");
        order.setShipping_address(shipping_address);
        Map<String,String> map = new HashMap<>();
        map.put(orderDTO.getDetail().split(":")[0],"1");
        order.setProducts(map);
//        shopper.setSKU_SIZE(orderDTO.getDetail().split(":")[0]);
//        shopper.setQty(Integer.parseInt(orderDTO.getDetail().split(":")[1]));
//        order.setProducts(shopper);
        return order;
    }

    /**
     * 给对方推送数据
     * @return
     * @throws Exception
     */
    public String forzieriPost(String url,String method,String jsonValue,OrderDTO order) throws Exception{
        return HttpUtil45.
                operateData(method, "json", url, new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1), null, jsonValue, null, null,null);
    }
}
