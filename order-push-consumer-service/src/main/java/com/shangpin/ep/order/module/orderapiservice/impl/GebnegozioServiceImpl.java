package com.shangpin.ep.order.module.orderapiservice.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.dto.SizeDto;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.response.HubResponse;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio.*;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa.Errors;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa.FailResult;
import com.shangpin.ep.order.module.orderapiservice.impl.util.HttpClientUtil;
import com.shangpin.ep.order.module.orderapiservice.impl.util.HttpRequestMethedEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhaowenjun on 2018/9/12.
 */
@Component("gebnegozioOrderImpl")
public class GebnegozioServiceImpl implements IOrderService {
    Gson gson = new Gson();
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logger = Logger.getLogger("info");
    @Autowired
    LogCommon logCommon;
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    @Autowired
    private RestTemplate restTemplate;

    String url = null;
    String tokenUrl = null;
    String supplierId = null;

    @PostConstruct
    public void init(){
        url = supplierProperties.getGebnegozio().getUrl();
        tokenUrl = supplierProperties.getGebnegozio().getTokenUrl();
        supplierId = supplierProperties.getGebnegozio().getSupplierId();
    }

    /**
     * 锁库存
     */
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {
        orderDTO.setLockStockTime(new Date());
        orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
        orderDTO.setLogContent("------锁库结束-------");
        logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
    }

    /**
     * 推送订单
     */
    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {
        String skuId =  null;
        int qty = 0;
        String detail = orderDTO.getDetail();
        skuId = detail.split(":")[0];
        qty = Integer.parseInt(detail.split(":")[1]);
        String reqQuoteUrl = url + "carts/mine";
        String skuRequId = "";
        String delCartProUrl = "";
        String delCartProJson = "";
        String addToCartUrl = url + "carts/mine/items";
        // 第一步：获取token
        String token = selToken();
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", "Bearer " + token);
        try{
            String stockData = selStock( skuId, token );
            //如果库存大于0,则下单
            //if( Integer.valueOf(stockData)>0 ){
                //第二步：获取购物车ID quoteId
                String quoteId = postHttp(HttpRequestMethedEnum.HttpPost, token, reqQuoteUrl, null, null);
                if(null != quoteId && !quoteId.equals("")){
                    quoteId = quoteId.substring( 1, quoteId.length()-1 );//取到的值有引号，去掉引号
                    logger.info("获取购物车ID：" + quoteId);
                    //第三步：添加商品到购物车 区分 simple 和 configurable
                    //3.1 ： 根据sku 查询产品是 simple 还是 configurable
                    String queryTypeIdUrl = url + "products/"+skuId+"?fields=sku,type_id,custom_attributes";
                    String typeIdJson = selMessage(token , queryTypeIdUrl);
                    logger.info("查询产品是简单产品还是可配置产品：" + typeIdJson);
                    ProDetail proDetail = gson.fromJson( typeIdJson, ProDetail.class);
                    String typeId = proDetail.getTypeId();
                    List<CustomAttributes> customAttributesList = proDetail.getCustomAttributesList();
                    Map<String, Object> customAttrMap = new HashMap<String, Object>();
                    String modello = "";
                    String queryConfUrl = "";
                    if( null != customAttributesList && customAttributesList.size()>0 ){
                        // key = attributeCode, value - value
                        customAttrMap = customAttributesList.stream().collect(Collectors.toMap(CustomAttributes::getAttributeCode,CustomAttributes::getValue));
                    }
                    if( customAttrMap.containsKey("modello") ){
                        modello = String.valueOf(customAttrMap.get("modello"));
                    }
                    logger.info("Product TypeId：" + typeId);
                    logger.info("Product Modello：" + modello);
                    modello = URLEncoder.encode( modello );
                    if( typeId.equals("simple") ){
                        queryConfUrl = url +  "products/?searchCriteria[currentPage]=1&searchCriteria[pageSize]=5&searchCriteria[filter_groups][0][filters][0][field]=modello&searchCriteria[filter_groups][0][filters][0][value]="+modello+"&searchCriteria[filter_groups][0][filters][0][condition_type]=eq&searchCriteria[filter_groups][1][filters][0][field]=type_id&searchCriteria[filter_groups][1][filters][0][value]=configurable&searchCriteria[filter_groups][1][filters][0][condition_type]=eq&fields=items[sku]";
                        String queryConfJson = postHttp(HttpRequestMethedEnum.HttpGet , token, queryConfUrl , null, null);
                        if(null != queryConfJson && !queryConfJson.equals("")) {
                            ResponseDTO responseDTO = gson.fromJson(queryConfJson, ResponseDTO.class);
                            skuRequId = responseDTO.getItems().get(0).getSku();
                        }
                        logger.info("当它是简单产品时，应该返回其父类的sku：" + skuRequId);
                    }else {
                        skuRequId = skuId;
                        logger.info("当它是可配置产品时，应该自身的sku：" + skuRequId);
                    }

                    //根据sku查尺码--------------------------------------------------
                    String usrSelectSize = getSizeFromEphub( supplierId, skuId );
                    Long supplierSpuId = getSupplierSpuIdFromEphub( supplierId, skuId );
                    //根据sku查颜色--------------------------------------------------
                    String usrSelectColor = getSupplierSpuColorFromEphub( supplierSpuId );
                    logger.info("简单产品sku的颜色：" + usrSelectColor);
                    logger.info("简单产品sku的尺码：" + usrSelectSize);
                    //封装请求参数
                    List<String> sizeOptList = selSizeAndColorOpt(usrSelectSize,"size",token);
                    List<String> colorOptList = selSizeAndColorOpt(usrSelectColor,"color",token);
                    CartItem cartItem = new CartItem();//可配置产品
                    ProductOption productOption = new ProductOption();
                    ExtensionAttributes extensionAttributes = new ExtensionAttributes();
                    List<ConfigurableItemOptions> configurableItemOptions = new ArrayList<ConfigurableItemOptions>();
                    ConfigurableItemOptions configurableItemOptions1 = new ConfigurableItemOptions();
                    ConfigurableItemOptions configurableItemOptions2 = new ConfigurableItemOptions();

                    cartItem.setSku( skuRequId );//如果是简单产品，查询父类的sku
                    cartItem.setQty(qty);
                    cartItem.setQuoteId(quoteId);

                    configurableItemOptions1.setOptionId(sizeOptList.get(0));
                    configurableItemOptions1.setOptionValue(sizeOptList.get(1));
                    configurableItemOptions2.setOptionId(colorOptList.get(0));
                    configurableItemOptions2.setOptionValue(colorOptList.get(1));
                    configurableItemOptions.add(configurableItemOptions1);
                    configurableItemOptions.add(configurableItemOptions2);
                    extensionAttributes.setConfigurableItemOptions(configurableItemOptions);
                    productOption.setExtensionAttributes(extensionAttributes);
                    cartItem.setProductOption(productOption);
                    CartRequ cartRequ = new CartRequ();
                    cartRequ.setCartItem(cartItem);
                    String cartJson = gson.toJson(cartRequ);
                    logger.info("添加到购物车的请求参数（JSON类型）：" + cartJson);
                    //添加商品到购物车，发送请求
                    delCartProJson = postHttp(HttpRequestMethedEnum.HttpPost , token, addToCartUrl , null, cartJson);
                    if(null != delCartProJson && !delCartProJson.equals("")) {
                        CartItem cartItem1 = gson.fromJson(delCartProJson, CartItem.class);
                        String itemId = cartItem1.getItemId();
                        //第四步：提供地址，估计运费
                        String estimateCostsUrl = url + "carts/mine/estimate-shipping-methods";
                        AddressRequ addressRequ = new AddressRequ();
                        addressRequ.setAddress(getAddredd(quoteId));
                        String addressJson = gson.toJson(addressRequ);
                        String carrierJson = postHttp(HttpRequestMethedEnum.HttpPost , token, estimateCostsUrl , null, addressJson);
                        List<CarrierDTO> carrierList = new ArrayList<CarrierDTO>();
                        if(null != carrierJson && !carrierJson.equals("") && !carrierJson.equals("[]")) {
                            //返回运输方式和运费
                            carrierList = gson.fromJson(carrierJson, new TypeToken<List<CarrierDTO>>() {
                            }.getType());
                            //第五步：设置发货和账单信息
                            String shopInfoUrl = url + "carts/mine/shipping-information";
                            AddressInfoRequ addressInfoRequ = new AddressInfoRequ();
                            addressInfoRequ.setAddressInformation(getAddressInfo(quoteId, carrierList));
                            String addressInfoJson = gson.toJson(addressInfoRequ);
                            String paymentJson = postHttp(HttpRequestMethedEnum.HttpPost, token, shopInfoUrl, null, addressInfoJson);
                            if (null != paymentJson && !paymentJson.equals("")) {
                                PaymentDTO paymentDTO = gson.fromJson(paymentJson, PaymentDTO.class);
                                //第六步：发送支付信息
                                String payInfoUrl = url + "carts/mine/payment-information";
                                ReqOrder reqOrder = new ReqOrder();
                                reqOrder.setBillingAddress(getAddredd(quoteId));
                                reqOrder.setPaymentMethod(getPayMethod());
                                String reqOrderJson = gson.toJson(reqOrder);
                                HashMap<String, String> httpResponse5 = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost, payInfoUrl, null, header, reqOrderJson);
                                if (httpResponse5.get("code").equals("200")) {
                                    String orderId = httpResponse5.get("resBody");
                                    if (null != orderId && !orderId.equals("")) {
                                        orderId = orderId.substring(1, orderId.length() - 1);
                                    }
                                    System.out.println("订单号：" + orderId);
                                    logger.info("订单号：" + orderId);
                                    //第七步：确认订单-------------------------没权限呐、
                                    /*String reqUrl = url + "orders/" + orderId;
                                    HashMap<String, String> httpResponse = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet, reqUrl, null, header, null);
                                    String resBody = httpResponse.get("resBody");
                                    int code = Integer.valueOf(httpResponse.get("code"));
                                    String message = httpResponse.get("message");
                                    logger.info("responseCode：" + code);
                                    logger.info("responseMessage：" + message);
                                    logger.info("responseBody：" + resBody);

                                    ResponseObject obj = new Gson().fromJson(resBody, ResponseObject.class);*/
                                    orderDTO.setConfirmTime(new Date());
                                    orderDTO.setSupplierOrderNo(orderId);

                                    String resBody = httpResponse5.get("resBody");
                                    int code = Integer.valueOf(httpResponse5.get("code"));
                                    String message = httpResponse5.get("message");
                                    logger.info("responseCode：" + code);
                                    logger.info("responseMessage：" + message);
                                    logger.info("responseBody：" + resBody);

                                    if (code == 200) {
                                        if (null != resBody && !resBody.equals("")) {
                                            // if ("processing".equals(obj.getStatus().toLowerCase())) {
                                            orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
                                            //}
                                        }
                                    } else if (code == 400) {
                                        FailResult fail = new Gson().fromJson(message, FailResult.class);
                                        Errors error = fail.getMessages().getError().get(0);
                                        orderDTO.setDescription(error.getMessage());
                                        orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                                        orderDTO.setErrorType(ErrorStatus.API_ERROR);
                                    } else if (code == 500) {
                                        FailResult fail = new Gson().fromJson(message, FailResult.class);
                                        Errors error = fail.getMessages().getError().get(0);
                                        orderDTO.setDescription(error.getMessage());
                                        orderDTO.setErrorType(ErrorStatus.API_ERROR);
                                        orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                                    } else {
                                        orderDTO.setErrorType(ErrorStatus.API_ERROR);
                                        orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                                    }
                                } else {
                                    logCommon.recordLog(skuId+" 设置发货和账单信息异常："+  httpResponse5.get("message") + "详细信息：" + httpResponse5.get("resBody"));
                                    logger.info("这里需要清空购物车----------");
                                    delCartProUrl = url + "carts/mine/items/" + itemId;
                                    HashMap<String, String> delCartProResp = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpDelete, delCartProUrl, null, header, null);
                                    if (delCartProResp.get("code").equals("200")) {
                                        String delflag = delCartProResp.get("resBody");
                                        if (delflag.equals("true")) {
                                            logger.info("购物车内容已经清空。");
                                        } else {
                                            logger.info("购物车内容清空失败！");
                                        }
                                    }
                               }
                            }
                        }
                    }
                }
            /*}else{
                orderDTO.setConfirmTime(new Date());
                orderDTO.setPushStatus(PushStatus.NO_STOCK);
            }*/
        }catch(Exception ex){
            //可能把产品加到购物车以后的步骤异常，需要清空购物车
            HashMap<String, String> httpResponse = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet, addToCartUrl, null, header, null);
            String resBody = httpResponse.get("resBody");
            //查询购物车有数据，则清空
            if (null != httpResponse && !httpResponse.equals("[]")) {
                if (null != resBody && !resBody.equals("") ){
                    List<CartItem> cartItems = gson.fromJson( resBody, new TypeToken<List<CartItem>>(){}.getType() );
                    if (null != cartItems && cartItems.size() >0 ) {
                        for (CartItem cartItem : cartItems) {
                            delCartProUrl = url + "carts/mine/items/" + cartItem.getItemId();
                            HashMap<String, String> delCartProResp = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpDelete, delCartProUrl, null, header, null);
                            if (delCartProResp.get("code").equals("200")) {
                                String delflag = delCartProResp.get("resBody");
                                if (delflag.equals("true")) {
                                    logger.info("购物车内容已经清空。");
                                } else {
                                    logger.info("购物车内容清空失败！");
                                }
                            }
                        }
                    }
                }
            }
            orderDTO.setDescription(ex.getMessage());
            orderDTO.setErrorType(ErrorStatus.API_ERROR);
            orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
        }
    }
    /**
     * 解除库存锁
     */
    @Override
    public void handleCancelOrder(OrderDTO deleteOrder) {
        deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED);
    }
    /**
     * 退款
     */
    @Override
    public void handleRefundlOrder(OrderDTO deleteOrder) {

        String skuId =  null;
        int qty = 0;
        String detail = deleteOrder.getDetail();
        skuId = detail.split(":")[0];
        qty = Integer.parseInt(detail.split(":")[1]);
        String json = "{\"purchase_no\":\""+deleteOrder.getPurchaseNo()+"\",\"order_no\":\""+deleteOrder.getSpOrderId()+"\",\"order_items\":[{\"sku_id\":\""+skuId+"\",\"quantity\":"+qty+"}]}";
        System.out.println("推送的数据:"+json);
        logger.info("推送的数据:"+json);

        String reqUrl = url + "orders/"+deleteOrder.getSupplierOrderNo();
        String token = selToken();
        // 存储相关的header值
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", "Bearer " + token);
        //发送请求
        HashMap<String,String> httpResponse = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet ,reqUrl, null, header, null);

        String resBody = httpResponse.get("resBody");
        String message = httpResponse.get("message");
        int code1 = Integer.valueOf( httpResponse.get("code") );
        logger.info("这是退款前获取订单信息返回信息--------------------");
        logger.info( "responseCode：" + code1 );
        logger.info( "responseMessage：" + message );
        logger.info( "responseBody：" + resBody );

        ResponseObject obj = new Gson().fromJson(resBody, ResponseObject.class);

        String orderItemId =  queryOrderItemId( obj , skuId );
        //封装请求对象
        RefundReqParam refundReqParam = packReqObject( orderItemId, qty );
        String refundReqJson =  gson.toJson( refundReqParam );
        String refundUrl = url + "order/" + deleteOrder.getSupplierOrderNo() + "/refund";
        HashMap<String,String> refundRes = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,refundUrl, null, header, refundReqJson);

        logger.info("这是退款返回信息-------------------------");
        logger.info( "responseCode：" + refundRes.get("code") );
        logger.info( "responseMessage：" + refundRes.get("message") );
        logger.info( "responseBody：" + refundRes.get("resBody") );

        int code2 = Integer.valueOf( refundRes.get("code") );
        if(code1 == 200 && code2 == 200 ){
            if(obj!=null){
                if("canceled".equals(obj.getStatus().toLowerCase())){
                    deleteOrder.setPushStatus(PushStatus.REFUNDED);
                }
            }
        }else if(code2==400){
            FailResult fail = new Gson().fromJson(message, FailResult.class);
            Errors error = fail.getMessages().getError().get(0);
            deleteOrder.setDescription(error.getMessage());
            deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
        }else if(code2==500){
            deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
        }else{
            deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
        }
    }
    //封装请求对象
    private RefundReqParam packReqObject( String orderItemId, int qty ) {
        List<Integer> retStoc = new ArrayList<Integer>();
        retStoc.add( Integer.valueOf( orderItemId ) );
        ExtAttr extAttr = new ExtAttr();
        extAttr.setReturnToStockItems( retStoc );
        GebArguments gebArguments = new GebArguments();
        gebArguments.setExtensionAttributes( extAttr );
        gebArguments.setAdjustmentNegative(0);
        gebArguments.setShippingAmount(0);
        gebArguments.setAdjustmentPositive(0);
        Itemss itemss = new Itemss();
        itemss.setOrderItemId( Integer.valueOf(orderItemId) );
        itemss.setQty( qty );

        RefundReqParam refundReqParam = new RefundReqParam();
        refundReqParam.setItems( itemss );
        refundReqParam.setNotify( true );
        refundReqParam.setArguments( gebArguments );
        return refundReqParam;
    }

    //根据要退款商品的 sku 查 对应订单的 order_item_id
    private String queryOrderItemId( ResponseObject obj, String sku ) {
        String orderItemId = null;
        if (null != obj){
            List<GebItems> gebItemsList = obj.getItems();
            if( null != gebItemsList && !gebItemsList.isEmpty() ){
                for ( GebItems gebItems : gebItemsList ) {
                    if( gebItems.getName().equals( sku ) ){
                        orderItemId = gebItems.getItemId();
                        break;
                    }
                }
            }
        }
        return orderItemId;
    }

    /**
     *  获取token
     */
    int i = 0;
    public String selToken(){
        String token = "";
        // 存储相关的header值
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        // 请求正文内容
        String json = "{\"username\":\"ming.liu@shangpin.com\",\"password\":\"Ex7n4AQ5\"}";
        //返回值响应对象
        HashMap<String,String> httpResponse = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,tokenUrl, null, header, json);
        String response = httpResponse.get("resBody");
        String code = httpResponse.get("code");
        if(null != response && !response.equals("") && code.equals("200")){
            token = response.substring( 1, response.length()-1 );
            return token;
        }else {
            for(;i<4;i++){
                logger.info("获取token异常，重新获取，错误超过5次将不再获取：" + httpResponse.get("message"));
                i++;
                token = selToken();
            }
            return token;
        }
    }
    /**
     *  填充地址
     */
    public Address getAddredd(String quoteId){
        Address address = new Address();
        List<String> street = new ArrayList<String>();
        street.add("Sanjianfang East Road, Chaoyang District, Beijing");

        address.setCountryId("CN");
        address.setStreet(street);
        address.setPostcode("100020");
        address.setCity("BeiJing");
        address.setFirstname("wenjun");
        address.setLastname("Zhao");
        address.setCustomerId(446);
        address.setEmail("wenjun.zhao@shangpin.com");
        address.setTelephone("(512)555-1111");
        address.setSameAsBilling(1);
        return address;
    }
    /**
     *  填充发货地址和账单信息
     */
    public AddressInformation getAddressInfo( String quoteId, List<CarrierDTO> carrierDTOList ){
        AddressInformation addressInformation = new AddressInformation();
        if ( null != carrierDTOList && carrierDTOList.size() > 0 ){
            addressInformation.setShippingAddress( getAddredd(quoteId) );
            addressInformation.setBillingAddress( getAddredd(quoteId) );
            addressInformation.setShippingMethodCode( carrierDTOList.get(0).getMethodCode() );
            addressInformation.setShippingCarrierCode( carrierDTOList.get(0).getCarrierCode() );
        }
        return addressInformation;
    }

    /**
     * 设置支付方式
     *
        {
         "code": "banktransfer",  //银行转帐
        "title": "Bank Transfer Payment"
        }
     */
    public PaymentMethod getPayMethod(){

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethod( "banktransfer" ); //这里先写成银行转账

        return paymentMethod;
    }

    /**
     * 查库存
     * @return
     */
    public String selStock( String sku , String token ){
        String qty = "0";
        if ( null != sku && !sku.equals("") ){
            try {
                if(sku.contains("\\\\")) {
                    sku = sku.replaceAll("\\\\\\\\", "\\\\");
                }
                String urlStr = URLEncoder.encode( sku , "UTF-8");
                String stockUrl = url + "stockStatuses/" + urlStr;
                String stockJson = selMessage(token , stockUrl);
                if ( null != stockJson && !stockJson.equals("") ){
                    StockDTO stockDTO = gson.fromJson( stockJson , StockDTO.class);
                    qty = stockDTO.getStockItem().getQty();
                    if(null == qty || qty.equals("")){
                        qty = "0";
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return  qty;
    }
    /**
     * 携带token获取内容、get公用方法
     */
    public String selMessage(String token , String url){
        String respValue = "";
        try{
            // 存储相关的header值
            Map<String,String> header = new HashMap<String, String>();
            header.put("Content-Type", "application/json");
            header.put("Authorization", "Bearer " + token);

            HashMap<String,String> response = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet ,url  ,null, header,null);
            if ( null != response && !response.equals("") ){
                if( response.get("code").equals("200") ){
                    respValue = response.get("resBody");
                }else {
                    logger.info("错误信息：" + response.get("message"));
                }
            }else {
                token = selToken();
                selMessage(token, url);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return respValue;
    }

    /**
     *  查产品的尺码、颜色的 ID 和 value
     * @param usrSelectOpt
     * @param opt
     * @param token
     * @return
     */
    public List<String> selSizeAndColorOpt(String usrSelectOpt,String opt,String token){
        String value = "";
        List<String> optResp = new ArrayList<String>();
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", "Bearer " + token);

        String selOptUrl = url + "products/attributes/"+opt;
        Map<String,String> optRes = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet ,selOptUrl, null, header, null);
        String optResBody = optRes.get("resBody");
        OptionsConf optRespList = gson.fromJson(optResBody,OptionsConf.class);
        String attributeId = optRespList.getAttributeId();//取到的size_Id
        List<Options> attributeOptions = optRespList.getOptions();//根据用户选择的lable取对应的value
        for ( Options options : attributeOptions ) {
            if( null != options && !options.equals("") ){
                if(options.getLabel().equals(usrSelectOpt)){
                    value = options.getValue();
                    break;
                }
            }
        }
        optResp.add( attributeId );
        optResp.add( value );
        return optResp;
    }

    /**
     *  POST请求的公用方法
     * @param token
     * @param url
     * @param params
     * @param entity
     *
     * @return
     */
    int j = 0;
    public String postHttp(HttpRequestMethedEnum requestMethod, String token,String url, Map<String, Object> params, String entity){
        String respValue = "";
        try {
            // 存储相关的header值
            Map<String, String> header = new HashMap<String, String>();
            header.put("Content-Type", "application/json");
            header.put("Authorization", "Bearer " + token);
            HashMap<String, String> respMap = HttpClientUtil.sendHttp(requestMethod, url, params, header, entity);
            String resBody = respMap.get("resBody");
            String code = respMap.get("code");
            String message = respMap.get("message");
            if (null != resBody && !resBody.equals("")) {
                if (code.equals("200")){
                    respValue = resBody;
                }else {
                    logCommon.recordLog("请求异常信息：" + resBody +"----------请求的url为：" + url);
                    System.out.println("request error info: " + resBody +"----------request url :" + url);
                }
            } else {
                logger.error("请求异常code：" + code );
                logger.error("请求返回为空，重新获取一次：" + message );
                token = selToken();
                for(;j<1;j++) {
                    j++;
                    postHttp(requestMethod, token, url, params, entity);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("请求异常,url为：" + url );
        }
        return respValue;
    }

    /**
     *  从ephub 获取供应商 size
     * @param supplierId
     * @param supplierSkuNo
     * @return
     */
    public String getSizeFromEphub(String supplierId, String supplierSkuNo){
        SizeDto sizeDto = new SizeDto();
        sizeDto.setSupplierId(supplierId);
        sizeDto.setSupplierSkuNo(supplierSkuNo);
        String getSizeFromEphubUrl = supplierProperties.getSupplier().getGetSizeFromEphubUrl();
        HubResponse<String> response = restTemplate.postForObject(getSizeFromEphubUrl, sizeDto, HubResponse.class);
        if("0".equals(response.getCode())){
            return response.getContent();
        }else{
            return "";
        }
    }
    /**
     *  从ephub 获取 supplier_spu_id
     * @param supplierId
     * @param supplierSkuNo
     * @return
     */
    public Long getSupplierSpuIdFromEphub(String supplierId, String supplierSkuNo){
        SizeDto sizeDto = new SizeDto();
        sizeDto.setSupplierId(supplierId);
        sizeDto.setSupplierSkuNo(supplierSkuNo);
        String getSupplierSpuIdFromEphubUrl = supplierProperties.getSupplier().getGetSupplierSpuIdFromEphubUrl();
        HubResponse<Integer> response = restTemplate.postForObject(getSupplierSpuIdFromEphubUrl, sizeDto, HubResponse.class);
        if("0".equals(response.getCode())){
            return Long.valueOf(response.getContent());
        }else{
            return null;
        }
    }
    /**
     *  从ephub 获取供应商原始颜色
     * @param supplierSpuId
     * @return
     */
    public String getSupplierSpuColorFromEphub(Long supplierSpuId){
        SizeDto sizeDto = new SizeDto();
        sizeDto.setSupplierSpuId(supplierSpuId);
        String getSupplierSpuColorFromEphubUrl = supplierProperties.getSupplier().getGetSupplierSpuColorFromEphubUrl();
        HubResponse<String> response = restTemplate.postForObject(getSupplierSpuColorFromEphubUrl, sizeDto, HubResponse.class);
        if("0".equals(response.getCode())){
            return response.getContent();
        }else{
            return "";
        }
    }
}
