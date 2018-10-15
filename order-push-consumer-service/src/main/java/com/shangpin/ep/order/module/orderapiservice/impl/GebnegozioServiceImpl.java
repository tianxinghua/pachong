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
import com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio.*;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa.Errors;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa.FailResult;
import com.shangpin.ep.order.module.orderapiservice.impl.util.HttpClientUtil;
import com.shangpin.ep.order.module.orderapiservice.impl.util.HttpRequestMethedEnum;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

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
    private HubSupplierSkuGateWay hubSupplierSkuGateWay;
    @Autowired
    private HubSupplierSpuGateWay hubSupplierSpuGateWay;
   String url = null;
    String tokenUrl = null;

    @PostConstruct
    public void init(){
        url = supplierProperties.getGebnegozio().getUrl();
        tokenUrl = supplierProperties.getGebnegozio().getTokenUrl();
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

        try{
            // 第一步：获取token
            String token = selToken();
            Map<String,String> header = new HashMap<String, String>();
            header.put("Content-Type", "application/json");
            header.put("Authorization", "Bearer " + token);

            //第二步：获取购物车ID quoteId
            HashMap<String,String> httpResponse1 = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,reqQuoteUrl, null, header, null);
            String quoteId = httpResponse1.get("resBody");

            String stockData = selStock( skuId, token );
            //如果库存大于0,则下单
            if( Integer.valueOf(stockData)>0 ){
                //第三步：添加商品到购物车 先不区分 simple 和 configurable
                String addToCartUrl = url + "carts/mine/items";
                //处理颜色和尺码
                //根据sku查尺码--------------------------------------------------
                HubSupplierSkuCriteriaDto skuCriteriaDto = new HubSupplierSkuCriteriaDto();
                HubSupplierSkuCriteriaDto.Criteria skuCriteria = skuCriteriaDto.createCriteria();
                if(null != skuId && StringUtils.isNotBlank(skuId)){
                    skuCriteria.andSupplierSkuNoEqualTo(skuId).andSupplierIdEqualTo("2018061101883");
                }
                List<HubSupplierSkuDto> hubSupplierSkuDtos =  hubSupplierSkuGateWay.selectByCriteria(skuCriteriaDto);
                String usrSelectSize = hubSupplierSkuDtos.get(0).getSupplierSkuSize();//用户选择的产品的尺码，根据sku去库里查对应的颜色
                Long supplierSpuId = hubSupplierSkuDtos.get(0).getSupplierSpuId();
                //根据sku查颜色--------------------------------------------------
                HubSupplierSpuCriteriaDto spuCriteriaDto = new HubSupplierSpuCriteriaDto();
                HubSupplierSpuCriteriaDto.Criteria spuCriteria = spuCriteriaDto.createCriteria();
                if(null != skuId && StringUtils.isNotBlank(skuId)){
                    spuCriteria.andSupplierSpuIdEqualTo(supplierSpuId).andSupplierIdEqualTo("2018061101883");
                }
                List<HubSupplierSpuDto> hubSupplierSpuDtos = hubSupplierSpuGateWay.selectByCriteria(spuCriteriaDto);
                String usrSelectColor = hubSupplierSpuDtos.get(0).getSupplierSpuColor();//用户选择的产品的颜色，根据sku去库里查对应的颜色
                //封装请求参数
                List<String> sizeOptList = selSizeAndColorOpt(usrSelectSize,"size",token);
                List<String> colorOptList = selSizeAndColorOpt(usrSelectColor,"color",token);
                CartItem cartItem = new CartItem();//可配置产品
                ProductOption productOption = new ProductOption();
                ExtensionAttributes extensionAttributes = new ExtensionAttributes();
                List<ConfigurableItemOptions> configurableItemOptions = new ArrayList<ConfigurableItemOptions>();
                ConfigurableItemOptions configurableItemOptions1 = new ConfigurableItemOptions();
                ConfigurableItemOptions configurableItemOptions2 = new ConfigurableItemOptions();

                cartItem.setSku(skuId);
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
                String cartJson = gson.toJson(CartItem.class);
                //发送请求
                HashMap<String,String> httpResponse2 = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,addToCartUrl, null, header, cartJson);

                //第四步：提供地址，估计运费
                String estimateCostsUrl = url + "carts/mine/estimate-shipping-methods";
                String addressJson = gson.toJson(getAddredd(quoteId));
                HashMap<String,String> httpResponse3 = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,estimateCostsUrl, null, header, addressJson);

                //返回运输方式和运费
                String carrierJson = httpResponse3.get("resBody");
                List<CarrierDTO> carrierList = gson.fromJson(carrierJson, new TypeToken<List<CarrierDTO>>() {}.getType());

                //第五步：设置发货和账单信息
                String shopInfoUrl = url + "carts/mine/shipping-information";
                String addressInfoJson = gson.toJson( getAddressInfo( quoteId, carrierList ) );
                HashMap<String,String> httpResponse4 = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,estimateCostsUrl, null, header, addressJson);
                String paymentJson = httpResponse4.get("resBody");
                PaymentDTO paymentDTO = gson.fromJson( paymentJson,PaymentDTO.class );

                //第六步：发送支付信息
                String payInfoUrl = url + "carts/mine/payment-information";
                ReqOrder reqOrder = new ReqOrder();
                reqOrder.setBillingAddress( getAddredd( quoteId ) );
                reqOrder.setPaymentMethod( getPayMethod() );
                String reqOrderJson = gson.toJson( reqOrder );
                HashMap<String,String> httpResponse5 = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,payInfoUrl, null, header, reqOrderJson);
                String orderId = httpResponse5.get("resBody");

                //第七步：确认订单
                String reqUrl = url+"orders/" + orderId;
                HashMap<String,String> httpResponse = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet ,reqUrl, null, header, null);

                String resBody = httpResponse.get("resBody");
                int code = Integer.valueOf( httpResponse.get("code") );
                String message = httpResponse.get("message");
                logger.info( "responseCode：" + code );
                logger.info( "responseMessage：" + message );
                logger.info( "responseBody：" + resBody );

                ResponseObject obj = new Gson().fromJson(resBody, ResponseObject.class);
                orderDTO.setConfirmTime(new Date());
                orderDTO.setSupplierOrderNo( orderId );
                if(code==200){
                    if(obj!=null){
                        if("processing".equals(obj.getStatus().toLowerCase())){
                            orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
                        }
                    }
                }else if(code==400){
                    FailResult fail = new Gson().fromJson(message, FailResult.class);
                    Errors error = fail.getMessages().getError().get(0);
                    orderDTO.setDescription(error.getMessage());
                    orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                    orderDTO.setErrorType(ErrorStatus.API_ERROR);
                }else if(code==500){
                    FailResult fail = new Gson().fromJson(message, FailResult.class);
                    Errors error = fail.getMessages().getError().get(0);
                    orderDTO.setDescription(error.getMessage());
                    orderDTO.setErrorType(ErrorStatus.API_ERROR);
                    orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                }else{
                    orderDTO.setErrorType(ErrorStatus.API_ERROR);
                    orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                }
            }else{
                orderDTO.setConfirmTime(new Date());
                orderDTO.setPushStatus(PushStatus.NO_STOCK);
            }
        }catch(Exception ex){
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
    public String selToken(){
        // 存储相关的header值
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        // 请求正文内容
        String json = "{\"username\":\"ming.liu@shangpin.com\",\"password\":\"Ex7n4AQ5\"}";
        //返回值响应对象
        HashMap<String,String> httpResponse = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,tokenUrl, null, header, json);
        String response = httpResponse.get("resBody");
        String token = response.substring( 1, response.length()-1 );
        return token;
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
        addressInformation.setShippingAddress( getAddredd(quoteId) );
        addressInformation.setBillingAddress( getAddredd(quoteId) );
        addressInformation.setShippingMethodCode( carrierDTOList.get(0).getMethodCode() );
        addressInformation.setShippingCarrierCode( carrierDTOList.get(0).getCarrierCode() );
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
                String urlStr = URLEncoder.encode( sku , "UTF-8");
                String stockUrl = url + "stockStatuses/" + urlStr;
                HashMap<String,String> stockJson = selMessage(token , stockUrl);
                if ( null != stockJson && !stockJson.equals("") ){
                    StockDTO stockDTO = gson.fromJson( stockJson.get("resBody") , StockDTO.class);
                    qty = stockDTO.getStockItem().getQty();
                    if(null == qty && qty.equals("")){
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
    public HashMap<String,String> selMessage(String token , String url){
        // 存储相关的header值
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", "Bearer " + token);

        HashMap<String,String> response = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet ,url  ,null, header,null);
        return response;
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


    //测试-------------------------
    public static void main(String[] args) {
        GebnegozioServiceImpl gebnegozioService = new GebnegozioServiceImpl();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setSpOrderId("181011");
        orderDTO.setDetail("1\\\\4\\\\3\\\\3586\\\\382465\\\\0:1");
        orderDTO.setSupplierId("2018061101883");
        gebnegozioService.handleConfirmOrder(orderDTO);
    }


}
