package com.shangpin.ep.order.module.orderapiservice.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.baseblu.OrderResult;
import com.shangpin.ep.order.util.axis.Orders_v1_0Stub;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lizhongren on 2017/2/8.
 */
@Component("baseBluServiceImpl")
@Slf4j
public class BaseBluServiceImpl implements IOrderService {
    @Autowired
    LogCommon logCommon;
    @Autowired
    SupplierProperties supplierProperties;

    private String createOrderUrl ;
    private String stockQueryUrl;
    private String sKey;

    @PostConstruct
    public void init(){
        createOrderUrl = supplierProperties.getBaseBlu().getOrderCreateUrl();
        stockQueryUrl = supplierProperties.getBaseBlu().getStockUrl();
        sKey = supplierProperties.getBaseBlu().getSKey();

    }

    OutTimeConfig outTimeConf =  new OutTimeConfig(1000*3,1000*60,1000*60);

    ObjectMapper mapper = new ObjectMapper();
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {
        orderDTO.setLockStockTime(new Date());
        orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
        orderDTO.setLogContent("------锁库结束-------");
        logCommon.loggerOrder(orderDTO, LogLeve.INFO);
    }

    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {
         //查询库存
        String  skuNo = orderDTO.getDetail().split(",")[0].split(":")[0];
        String  num =  orderDTO.getDetail().split(",")[0].split(":")[1];
        if(!isHasStock(skuNo)){
            //无库存
            orderDTO.setConfirmTime(new Date());
            orderDTO.setPushStatus(PushStatus.NO_STOCK);
            logCommon.recordLog("供货商："+ orderDTO.getSupplierId() +" skuNo：" + skuNo +" 无库存");
        }else{
            //有库存
            String queryJson = this.getOrderJson(orderDTO.getPurchaseNo(),skuNo,num);
            try {
                String result  =  HttpUtil45.operateData("post","json",
                        createOrderUrl+"?sKey="+sKey,
                        outTimeConf,null,queryJson,null,"","");

                orderDTO.setLogContent("返回结果 ========"+result+ " 推送参数====" + queryJson);
                logCommon.loggerOrder(orderDTO, LogLeve.INFO);


                if(HttpUtil45.errorResult.equals(result)){
                    orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
                    orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                    orderDTO.setDescription("网络连接错误");
                    return;
                }else{
                    try {
                        result = result.replaceAll("\\\\","");
                        result = result.substring(1,result.length()-1);
                        OrderResult orderResult =  mapper.readValue(result, OrderResult.class);
                        if(0==orderResult.getCodMsg()){
                            orderDTO.setConfirmTime(new Date());
                            orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
                        }else{
                            //推送订单失败
                            orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                            orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
                            orderDTO.setDescription("push order to supplier error：" + orderResult.getMsg());
                        }
                    } catch (IOException e) {
                        orderDTO.setErrorType(ErrorStatus.API_ERROR);
                        orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                        orderDTO.setDescription("push order to supplier error：" + e.getMessage());
                    }
                }

            } catch (Exception e) {
                orderDTO.setErrorType(ErrorStatus.API_ERROR);
                orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
                orderDTO.setLogContent(" confirm order error:"+e.getMessage());
                logCommon.loggerOrder(orderDTO, LogLeve.ERROR);
            }
            orderDTO.setUpdateTime(new Date());
        }


    }




    /**
     * 如果库存接口有问题  可继续执行
     * @param skuNo
     * @return
     */
    private boolean isHasStock(String  skuNo){
        String stockUrl = supplierProperties.getBaseBlu().getStockUrl()+"?sKey="+
                supplierProperties.getBaseBlu().getSKey() +"&ean13="+skuNo;
        LogCommon.recordLog(stockUrl,LogLeve.DEBUG);
        String stockResult  = HttpUtil45.get(stockUrl,outTimeConf,null);
        LogCommon.recordLog(stockResult,LogLeve.DEBUG);
        if(stockResult.indexOf("Stock")>=0){
            stockResult = stockResult.replaceAll("\\\\","");
            stockResult = stockResult.substring(1,stockResult.length()-1);

            JSONObject obj = JSON.parseObject(stockResult);;

            int num = obj.getIntValue("Stock");
            if(num > 0 ){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

    private String getOrderJson(String orderNo,String skuNo,String  quantity){
        String jsonFormat = "{\n" +
                "\"order_id\": \"" + orderNo + "\",\n" +
                "\"customer\": {\n" +
                "\"name\": \"Shangping\",\n" +
                "\"surname\": \"Shangping\",\n" +
                "\"address\": \"VIA GIACOMO LEOPARDI 27 22075 LURATE CACCIVIO IT Italy\",\n" +
                "\"zip\": \"22075\",\n" +
                "\"city\": \"LURATE CACCIVIO\",\n" +
                "\"state\": \"italy\",\n" +
                "\"country\": \"italy\",\n" +
                "\"phone\": \"\",\n" +
                "\"mobile\": \"\",\n" +
                "\"email\": \"\"\n" +
                "},\n" +
                "\"products\": [{\n" +
                "\"barcode\": \"" + skuNo + "\",\n" +
                "\"quantity\": \""+ quantity +"\"\n" +
                "}]" +
                "}";
        return  jsonFormat;
    }


    @Override
    public void handleCancelOrder(OrderDTO deleteOrder) {
        deleteOrder.setCancelTime(new Date());
        deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API);
        logCommon.loggerOrder(deleteOrder, LogLeve.INFO);
    }

    @Override
    public void handleRefundlOrder(OrderDTO deleteOrder) {
        deleteOrder.setCancelTime(new Date());
        deleteOrder.setPushStatus(PushStatus.NO_REFUNDED_API);
        logCommon.loggerOrder(deleteOrder, LogLeve.INFO);

    }
}
