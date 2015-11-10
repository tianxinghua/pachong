package com.shangpin.iog.gilt.order;

import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.OrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.gilt.dto.ErrorDTO;
import com.shangpin.iog.gilt.dto.OrderDTO;
import com.shangpin.iog.gilt.dto.OrderDetailDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.ReturnOrderService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by loyalty on 15/9/9.
 */
@Component("giltOrder")
public class OrderServiceImpl  {

    private static String  startDate=null,endDate=null;
    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";

    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String key ;
    private static String confirmTime;
    private static String url;
    private static String domain;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
        confirmTime = bdl.getString("confirmTime");
        domain =   bdl.getString("url");
        url = bdl.getString("url") + "/global/orders/";
    }

    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;
    @Autowired
    ReturnOrderService returnOrderService;
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");


    private String  placedStatus= OrderStatus.PLACED;
    private String  confirmedStatus=OrderStatus.CONFIRMED;

    private String payStatus = OrderStatus.PAYED;

    private OrderService iceOrderService = new OrderService();


    /**
     * 下订单
     */
    public void purchaseOrder(){

        //初始化时间
        initDate("date.ini");

        try {

            //下单异常 再次下单
            handlePurchaseOrderException();

            handleCancelPurchaseOrderException();
            //取消订单
            deleteOrder();
            //  正常下单
            transData();




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 支付确认
     */
    public void confirmOrder(){
        //1 、 获取已下单的订单信息

        List<com.shangpin.iog.dto.OrderDTO>  orderDTOList= null;
        try {
            orderDTOList  =productOrderService.getOrderBySupplierIdAndOrderStatus(supplierId,payStatus);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        //2、向gilt 确认支付成功
        String param="{\"status\" : \"confirmed\"}";
        String returnStr="";
        if(null!=orderDTOList){
            Gson gson = new Gson();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
            Date date = new Date();
            int  iConfirmTime = Integer.valueOf(confirmTime);
            for(com.shangpin.iog.dto.OrderDTO orderDTO :orderDTOList){

                if(DateTimeUtil.getTimeDifference(orderDTO.getCreateTime(),date)/(1000*60*iConfirmTime)>0){// 到确认时间后通知支付发货 因为gilt发货后不给退 所以延长确认支付时间
                    try {
                        returnStr=HttpUtil45.operateData("patch", "json", url + orderDTO.getUuId(), timeConfig, null, param, key, "");
                        logger.info("更新gilt端订单状态返回信息："+returnStr);
                    } catch (ServiceException e) {

                        //非200返回
                        Map<String,String> map = new HashMap<>();
                        String reason ="";
                        if("状态码:422".equals(e.getMessage())){
                            reason ="sku are sold out ";
                        }else{
                            reason ="code = " +  e.getMessage();
                        }
                        map.put("uuId",orderDTO.getUuId());
                        map.put("excDesc",reason);
                        map.put("excState","1");
                        map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));

                        if(DateTimeUtil.getTimeDifference(orderDTO.getCreateTime(),new Date())/(1000*60*60*24)>0){
                            //超过一天 不需要在做处理 订单状态改为其它状态
                            map.put("status",OrderStatus.NOHANDLE);

                        }

                        try {
                            productOrderService.updateOrderMsg(map);

                            if(OrderStatus.NOHANDLE.equals(map.get("status"))){
                                //采购异常

                                iceOrderService.cancelPurchaseOrder(orderDTO.getSpPurchaseDetailNo(),map.get("excDesc"),supplierId);
                            }

                        } catch (ServiceException e1) {
                            loggerError.error("订单 :" + orderDTO.getUuId() + " 更新 confirmed 状态失败");
                            e1.printStackTrace();
                        }
                        continue;
                    }
                    if(HttpUtil45.errorResult.equals(returnStr)){  //链接异常
                        loggerError.error("支付下单编号 ："+orderDTO.getUuId()+" -链接异常");
                        setConnectionError(orderDTO.getUuId());
                        continue;

                    }else{
                        if(returnStr.indexOf("message")>0&&returnStr.indexOf("type")>0){//处理失败

                            loggerError.error("支付下单 失败:"+returnStr);

                            ErrorDTO errorDTO = gson.fromJson(returnStr, ErrorDTO.class);
                            Map<String,String> map = new HashMap<>();
                            map.put("excDesc",null!=errorDTO?(" 订单号 "+ orderDTO.getUuId() + " 商品 " + (null!=errorDTO.getSku_id()?errorDTO.getSku_id():" ") + ":"+errorDTO.getType()):(orderDTO.getUuId() + "确认异常"));
                            setErrorMsg(orderDTO.getUuId(), map);
                            continue;
                        }else{

                            //更新订单状态
                            Map<String,String> map = new HashMap<>();
                            map.put("status",confirmedStatus);
                            map.put("uuId",orderDTO.getUuId());
                            map.put("updateTime",DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
                            try {
                                productOrderService.updateOrderStatus(map);
                            } catch (ServiceException e) {
                                loggerError.error("订单："+orderDTO.getUuId()+" gilt支付确认成功，但更新订单状态失败时失败");
                                System.out.println("订单："+orderDTO.getUuId()+" gilt支付确认成功，但更新订单状态失败时失败");
                                e.printStackTrace();
                            }

                        }
                    }

                }



            }

        }




    }

    private void setErrorMsg(String uuId, Map<String, String> map) {
        map.put("uuId",uuId);
        map.put("excState","1");

        map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        try {
            productOrderService.updateExceptionMsg(map);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void setErrorMsgForReturn(String uuId, Map<String, String> map) {
        map.put("uuId",uuId);
        map.put("excState","1");

        map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        try {
            returnOrderService.updateReturnOrderMsg(map);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消订单
     */
    public void deleteOrder(){
        //初始化时间


        try {
            //获取订单数组
            List<Integer> status = new ArrayList<>();
            status.add(5);
            Map<String,List<PurchaseOrderDetail>> orderMap =  iceOrderService.getPurchaseOrder(supplierId, startDate, endDate, status);
            //  下单
            cancelData( orderMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置发货单
     */
    public void deliveryOrder(){
        try {
            //获取已提交的产品信息
            List<com.shangpin.iog.dto.OrderDTO> uuIdList =  productOrderService.getOrderBySupplierIdAndOrderStatus(supplierId, confirmedStatus);
            Gson gson =new Gson();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*15,1000*15,1000*15);
            Map<String,String> param =new HashMap<>();
            String uuId ="";
            String result ="";

            for(com.shangpin.iog.dto.OrderDTO orderDTO:uuIdList){
                uuId=orderDTO.getUuId();
                result=HttpUtil45.get(url +uuId, timeConfig, param, key, "");
                logger.info("查询是否发货："+result);
                if(HttpUtil45.errorResult.equals(result)){  //链接异常
                    loggerError.error("获取采购单商品发货状态链接异常："+uuId);
                    // 是否更新订单异常状态.
                    setConnectionError(uuId);
                    continue;
                }else {

                    OrderDTO dto = gson.fromJson(result, OrderDTO.class);

                    if (null != dto && "shipped".equals(dto.getStatus())) {
                        //通知SOP已发货
                        String  purchaseDetailNo = orderDTO.getSpPurchaseDetailNo();
                        if(StringUtils.isBlank(purchaseDetailNo)) continue;
                        List<String> purchaseOrderIdList = new ArrayList<>();
                        String[] purchaseDetailNoArray = purchaseDetailNo.split(";");
                        if(null!=purchaseDetailNoArray){
                            for(String purchaseDetailNO:purchaseDetailNoArray){
                                purchaseOrderIdList.add(purchaseDetailNO);
                            }

                        }

                        try {
                            String  deliverNo=  iceOrderService.getPurchaseDeliveryOrderNo(supplierId,
                                    "","","",5,"","","", "","","",purchaseOrderIdList,0);
                            //更新海外对接库
                            Map<String, String> map = new HashMap<>();
                            map.put("status", dto.getStatus());
                            map.put("uuId", dto.getId());
                            map.put("updateTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
                            map.put("deliveryNo",deliverNo);
                            try {
                                productOrderService.updateDeliveryNo(map);
                            } catch (Exception e) {
                                //增加异常信息
                                loggerError.error("订单:" + orderDTO.getUuId() + " 已发货。"
                                        + " 采购单：" + orderDTO.getSpOrderId() + " 推送发货单信息成功 " + " 但保存推送信息时失败 ");
                                e.printStackTrace();
                            }
                        } catch (Exception e) {


                            loggerError.error("订单:" + orderDTO.getUuId() + " 已发货。" + " 采购单：" + orderDTO.getSpOrderId() + "推送发货单信息失败");
                            //增加异常信息

                            String date =DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH) ;
                            Map<String, String> map = new HashMap<>();
//                            map.put("status", dto.getStatus());
                            map.put("uuId", dto.getId());
                            map.put("updateTime", date);
                            map.put("excState","1");
                            map.put("excDesc","订单:" + orderDTO.getUuId() + "已发货，但推送发货单信息时失败");
                            map.put("excTime", date);
                            try {
                                productOrderService.updateOrderMsg(map);
                            } catch (ServiceException e1) {
                                loggerError.error("订单:" + orderDTO.getUuId() + " 已发货。" + " 采购单：" + orderDTO.getSpOrderId() + " 推送发货单信息失败。 保存信息失败");
                            }


                        }


                    }else{
                        loggerError.error("订单:" + uuId + "获取订单信息失败。可能被gilt删除。");
                    }

                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            loggerError.error("获得gilt采购单状态更改失败");
        }
    }

    /**
     * 设置链接异常
     * @param uuId
     */
    private void setConnectionError(String uuId) {
        Map<String,String> map = new HashMap<>();
        map.put("uuId",uuId);
        map.put("excState","1");
        map.put("excDesc","链接异常，无法下单");
        map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        try {
            productOrderService.updateExceptionMsg(map);
        } catch (ServiceException e) {
            loggerError.error("支付下单编号 ：" + uuId + " -链接异常。更新异常信息时失败");
        }
    }



    private void setConnectionErrorForReturnOrder(String uuId) {
        Map<String,String> map = new HashMap<>();
        map.put("uuId",uuId);
        map.put("excState","1");
        map.put("excDesc","链接异常，无法下单");
        map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        try {
            returnOrderService.updateReturnOrderMsg(map);
        } catch (ServiceException e) {
            loggerError.error("支付下单编号 ：" + uuId + " -链接异常。更新异常信息时失败");
        }
    }

    /**
     * 下单 先存入本地库 去通知gilt 更改本地库状态


     * @throws ServiceException
     */
    public void transData() throws ServiceException {


        //获取订单数组
        List<Integer> status = new ArrayList<>();
        status.add(1);
        Map<String,List<PurchaseOrderDetail>> orderMap = null;
        try {
            orderMap = iceOrderService.getPurchaseOrder(supplierId, startDate, endDate, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        if(null!=orderMap&&orderMap.size()==0){
            //无值 测试下状态
            try {
              String healstatus =   HttpUtil45.get( domain + "/global/healthchecks/status",timeConfig,null);
                logger.info("服务器状态:"+healstatus);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();

        String uuId="";
        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();){
            Map.Entry<String, List<PurchaseOrderDetail>> entry = itor.next();
            OrderDTO orderDTO = new OrderDTO();
            Map<String,Integer> stockMap = new HashMap<>();
            StringBuffer purchaseOrderDetailbuffer =new StringBuffer();
            //获取同一产品的数量

            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){

                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, stockMap.get(purchaseOrderDetail.SupplierSkuNo)+1);
                }else{
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, 1);

                }

            }
            List<OrderDetailDTO>list=new ArrayList<>();
            StringBuffer buffer = new StringBuffer();
            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){
                //记录采购单明细信息 以便发货
                purchaseOrderDetailbuffer.append(purchaseOrderDetail.SopPurchaseOrderDetailNo).append(";");
                //计算同一采购单的相同产品的数量
                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                    OrderDetailDTO detailDTO = new OrderDetailDTO();
                    detailDTO.setSku_id(Integer.valueOf(purchaseOrderDetail.SupplierSkuNo));
                    detailDTO.setQuantity(stockMap.get(purchaseOrderDetail.SupplierSkuNo));
                    buffer.append(detailDTO.getSku_id()).append(":").append(detailDTO.getQuantity()).append(",");
                    list.add(detailDTO);
                    stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
                }

            }

            orderDTO.setOrder_items(list);
            uuId=UUID.randomUUID().toString();

            //存储
            com.shangpin.iog.dto.OrderDTO spOrder =new com.shangpin.iog.dto.OrderDTO();
            spOrder.setUuId(uuId);
            spOrder.setSupplierId(supplierId);
            spOrder.setStatus(OrderStatus.WAITPLACED);
            spOrder.setSpOrderId(entry.getKey());
            spOrder.setSpPurchaseNo(entry.getKey());
            spOrder.setSpPurchaseDetailNo(purchaseOrderDetailbuffer.toString());
            spOrder.setDetail(buffer.toString());
            spOrder.setCreateTime(new Date());
            try {
                logger.info("采购单信息转化订单后信息："+spOrder.toString());
                System.out.println("采购单信息转化订单后信息："+spOrder.toString());
                productOrderService.saveOrder(spOrder);

                String param = gson.toJson(orderDTO,new TypeToken<OrderDTO>(){}.getType());
                logger.info("传入订单内容 ：" + param);
                System.out.println("传入订单内容 ：" + param);
                if (informOrderForGilt(url, gson, timeConfig, uuId, spOrder, param,"")) continue;


            } catch (ServiceException e) {
                loggerError.error("采购单 ："+ spOrder.getSpOrderId() + "失败,失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage() );
                System.out.println("采购单 ："+ spOrder.getSpOrderId() + "失败,失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage());
                e.printStackTrace();
            } catch (Exception e){
                loggerError.error("下单错误 " + e.getMessage());
                e.printStackTrace();
            }


            logger.info("----gilt 订单存储完成----");
        }
    }

    private boolean informOrderForGilt(String url, Gson gson, OutTimeConfig timeConfig, String uuId, com.shangpin.iog.dto.OrderDTO spOrder, String param,String errorType) throws ServiceException {
        String result = null;
        try {
            result = HttpUtil45.operateData("put", "json", url + uuId, timeConfig, null, param, key, "");
        } catch (ServiceException e) {
            //非200返回
            Map<String,String> map = new HashMap<>();
            String reason ="";
            if("状态码:422".equals(e.getMessage())){
                reason ="sku are sold out ";
            }else  if("状态码:404".equals(e.getMessage())){

                reason ="已无此商品. ";
                //处理采购异常
                try {
                    iceOrderService.cancelPurchaseOrder(spOrder.getSpPurchaseDetailNo(),reason,supplierId);
                    map.put("status", OrderStatus.NOHANDLE);
                } catch (ServiceException e1) {
                    reason = reason + e1.getMessage();
                }



            }else{
                reason ="code = " +  e.getMessage();
            }
            map.put("uuId",uuId);
            map.put("excDesc",reason);
            map.put("excState","1");
            map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
            if(StringUtils.isNotBlank(errorType)){//已发生过异常
               if(DateTimeUtil.getTimeDifference(spOrder.getCreateTime(),new Date())/(1000*60*60*24)>0){
                   //超过一天 不需要在做处理 订单状态改为其它状体
                   map.put("status","NOHANDLE");

               }
            }


            productOrderService.updateOrderMsg(map);
            return  true;

        }

        String spOrderId = spOrder.getSpOrderId();

        if(HttpUtil45.errorResult.equals(result)){  //链接异常

            this.setConnectionError(uuId);
            loggerError.error("采购单："+spOrderId+" 链接异常 无法处理");



        }else{
            logger.info("订单处理结果 ：" + result);
            System.out.println("订单处理结果 ：" + result);
            //更新      日志存储，数据库更新

            try {
                if(result.indexOf("message")>0&&result.indexOf("type")>0){ //  失败
                    loggerError.error("采购单："+spOrderId+" gilt 处理时有异常。异常信息 ：" + result);
                    System.out.println("采购单：" + spOrderId + " gilt 处理时有异常。异常信息 ：" + result);

                    loggerError.error("支付下单 失败:"+result);

                    ErrorDTO errorDTO = gson.fromJson(result, ErrorDTO.class);
                    Map<String,String> map = new HashMap<>();
                    map.put("excDesc",null!=errorDTO?(" 订单号 "+ uuId + " 商品 " + (null!=errorDTO.getSku_id()?errorDTO.getSku_id():" ") + ":"+errorDTO.getType()):(uuId + "下单异常"));
                    setErrorMsg(uuId, map);
                    return true;
                }
                OrderDTO dto= getObjectByJsonString(result);
                if(null==dto){
                    loggerError.error("采购单："+spOrderId+" 下单返回转化失败");
                    System.out.println("采购单：" + spOrderId + " 下单返回转化失败");
                    return true;
                }

                //
                //更新订单状态
                Map<String,String> map = new HashMap<>();
                map.put("status",OrderStatus.PAYED);
                map.put("uuId",dto.getId());
                if(StringUtils.isNotBlank(errorType)){
                    map.put("excState","0");
                }
                try {

                    productOrderService.updateOrderMsg(map);
                } catch (ServiceException e) {
                    loggerError.error("采购单："+spOrderId+" 下单成功。但更新订单状态失败");
                    System.out.println("采购单：" + spOrderId + " 下单成功。但更新订单状态失败");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                //下单失败
                loggerError.error("采购单："+spOrderId+" 下单返回转化失败");
                System.out.println("采购单：" + spOrderId + " 下单返回转化失败");
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 处理异常订单
     */

    private void handlePurchaseOrderException(){
        //拉取采购单存入本地库，产生订单 但通知gilt时 失败
        List<com.shangpin.iog.dto.OrderDTO>  orderDTOList= null;
        try {
            orderDTOList  =productOrderService.getOrderBySupplierIdAndOrderStatus(supplierId, OrderStatus.WAITPLACED);
            if(null!=orderDTOList&&orderDTOList.size()>0){
                String orderDetail = "",orderMsg="";
                Gson gson = new Gson();
                OutTimeConfig timeConfig = new OutTimeConfig(1000*60,1000*60,1000*60);
                for(com.shangpin.iog.dto.OrderDTO orderDTO:orderDTOList){
                    orderDetail = orderDTO.getDetail().substring(0,orderDTO.getDetail().length()-1);
                    String[] orderDetailArray = orderDetail.split(",");
                    if(null!=orderDetailArray){
                        StringBuffer buffer = new StringBuffer("{\"order_items\":[");
                        int i =0;
                        for(String detail:orderDetailArray){
                            i++;
                           if(i==orderDetailArray.length) {
                               buffer.append("{\"sku_id\":").append(detail.substring(0,detail.indexOf(":"))).append(",\"quantity\":")
                                       .append(detail.substring(detail.indexOf(":")+1)).append("}");
                           }else{
                               buffer.append("{\"sku_id\":").append(detail.substring(0,detail.indexOf(":"))).append(",\"quantity\":")
                                       .append(detail.substring(detail.indexOf(":")+1)).append("},");
                           }


                        }
                        buffer.append("]}");


                        orderMsg = buffer.toString();
                        //异常订单通知下单
                        informOrderForGilt(url, gson, timeConfig, orderDTO.getUuId(), orderDTO, orderMsg,"exception");

                    }



                }
            }

        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }



    /**
     * 取消订单

     * @param orderMap
     * @throws ServiceException
     */
    public void cancelData( Map<String, List<PurchaseOrderDetail>> orderMap) throws ServiceException {




        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();) {
            Map.Entry<String, List<PurchaseOrderDetail>> entry = itor.next();
            Map<String, Integer> stockMap = new HashMap<>();
            for (PurchaseOrderDetail purchaseOrderDetail : entry.getValue()) {
                if (stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)) {
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, stockMap.get(purchaseOrderDetail.SupplierSkuNo) + 1);
                } else {
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, 1);
                }
            }
            StringBuffer buffer = new StringBuffer();
            for (PurchaseOrderDetail purchaseOrderDetail : entry.getValue()) {
                if (stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)) {
                    buffer.append(purchaseOrderDetail.SupplierSkuNo).append(":").append(stockMap.get(purchaseOrderDetail.SupplierSkuNo)).append(",");
                    stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
                }
            }

            com.shangpin.iog.dto.OrderDTO orderDTO =   productOrderService.getOrderByPurchaseNo(entry.getKey());
            //存储
            if(null== orderDTO){//采购单已到退款状态  未有已支付状态
                continue;
            }
            String uuId =  orderDTO.getUuId();
            com.shangpin.iog.dto.ReturnOrderDTO deleteOrder =new com.shangpin.iog.dto.ReturnOrderDTO();
            deleteOrder.setUuId(uuId);
            deleteOrder.setSupplierId(supplierId);
            deleteOrder.setStatus(OrderStatus.WAITCANCEL);
            deleteOrder.setSpOrderId(entry.getKey());
            deleteOrder.setDetail(buffer.toString());
            deleteOrder.setCreateTime(new Date());
            try{
                logger.info("采购单信息转化退单后信息："+deleteOrder.toString());
                System.out.println("采购单信息转化退单后信息："+deleteOrder.toString());
                returnOrderService.saveOrder(deleteOrder);
                if (confirmCancelOrder(  orderDTO, deleteOrder)) continue;

            } catch (ServiceException e) {
                loggerError.error("采购单 ："+ deleteOrder.getSpOrderId() + "失败,失败信息 " + deleteOrder.toString()+" 原因 ：" + e.getMessage() );
                System.out.println("采购单 ："+ deleteOrder.getSpOrderId() + "失败,失败信息 " + deleteOrder.toString()+" 原因 ：" + e.getMessage());
                e.printStackTrace();
            } catch (Exception e){
                loggerError.error("下单错误 " + e.getMessage());
                e.printStackTrace();
            }


        }
    }

    /**
     * 通知供货商取消订单
      * @param orderDTO
     * @param deleteOrder
     * @return
     */
    private boolean confirmCancelOrder(  com.shangpin.iog.dto.OrderDTO orderDTO, ReturnOrderDTO deleteOrder) {
        String errorMsg;
        String operateTime;
        String result = "";
        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        try {

            OrderDTO dto = null;
            try {
                //查询订单状态
                result = HttpUtil45.operateData("get", "",  url + orderDTO.getUuId(), timeConfig, null, "", key, "");
            } catch (ServiceException e) {
                updateReturnOrderMsg(orderDTO.getUuId(), e);
                return true;
            }

            if(HttpUtil45.errorResult.equals(result)){  //链接异常
                loggerError.error("退单："+orderDTO.getUuId()+" 链接异常 无法处理");
                setConnectionErrorForReturnOrder(orderDTO.getUuId());

            }else{

                dto= getObjectByJsonString(result);
                if(null==dto){
                    loggerError.error("退单："+orderDTO.getUuId()+" 查询订单状态转化失败");
                    System.out.println("退单：" + orderDTO.getUuId() + " 查询订单状态转化失败");
                    return true;

                }
                //更新状态
                if("shipped".equals(dto.getStatus())){//已发货 不能取消订单
                    //修改退单
                    Map<String,String> returnMap = new HashMap<>();
                    returnMap.put("uuId",orderDTO.getUuId());
                    returnMap.put("excState","1");
                    returnMap.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
                    //处理采购异常

                    try {
                        iceOrderService.cancelPurchaseOrder(orderDTO.getSpPurchaseDetailNo(), "已发货不能取消订单", supplierId);
                        returnMap.put("status",OrderStatus.SHIPPED);
                        returnMap.put("excDesc","已发货不能取消订单");

                        errorMsg = " 通知SOP取消采购单成功";

                    } catch (ServiceException e) {
                        returnMap.put("excDesc","已发货,通知SOP取消采购单失败。");

                        errorMsg = " 通知SOP取消采购单失败";
                    }


                    try {
                        returnOrderService.updateReturnOrderMsg(returnMap);
                    } catch (ServiceException e) {
                        loggerError.error("uuId :" + orderDTO.getUuId() + errorMsg + " 更新退单信息失败");
                    }


                    return true;
                }else{
                    String param ="{\"status\" : \"cancelled\"}";
                    try {
                        result = HttpUtil45.operateData("patch", "json",url + deleteOrder.getUuId(), timeConfig, null, param, key, "");
                    } catch (ServiceException e) {
                        updateReturnOrderMsg(orderDTO.getUuId(), e);

                        return true;
                    }
                    if(HttpUtil45.errorResult.equals(result)){  //链接异常
                        loggerError.error("采购单："+deleteOrder.getSpOrderId()+" 链接异常 无法处理");


                    }else{
                        logger.info("退单处理结果 ：" + result);
                        System.out.println("退单处理结果 ：" + result);
                        //更新      日志存储，数据库更新

                        try {
                            if(result.indexOf("message")>0&&result.indexOf("type")>0){ //  失败
                                loggerError.error("采购单："+deleteOrder.getSpOrderId()+" 退单返回转化失败");
                                System.out.println("采购单：" + deleteOrder.getSpOrderId() + " 退单返回转化失败");
                                Gson gson = new Gson();
                                ErrorDTO errorDTO = gson.fromJson(result, ErrorDTO.class);
                                Map<String,String> map = new HashMap<>();
                                map.put("excDesc",null!=errorDTO?(" 订单号 "+ orderDTO.getUuId() + " 商品 " + (null!=errorDTO.getSku_id()?errorDTO.getSku_id():" ") + ":"+errorDTO.getType()):(orderDTO.getUuId() + "下单异常"));
                                setErrorMsgForReturn(orderDTO.getUuId(), map);


                            }else{
                                //更改退单状态

                                operateTime = DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH);
                                try {
                                    Map<String,String> map = new HashMap<>();
                                    map.put("status","cancelled");
                                    map.put("uuId",orderDTO.getUuId());
                                    map.put("updateTime",operateTime);
                                    returnOrderService.updateReturnOrderMsg(map);
                                } catch (ServiceException e) {
                                    loggerError.error("订单："+orderDTO.getUuId()+" 退款成功。但更新订单状态失败");
                                    System.out.println("订单："+orderDTO.getUuId()+" 退款成功。但更新订单状态失败");
                                    e.printStackTrace();
                                }

                                //更改订单状态

                                try {
                                    Map<String,String> map = new HashMap<>();
                                    map.put("status", OrderStatus.CANCELLED);
                                    map.put("uuId",orderDTO.getUuId());
                                    map.put("updateTime",operateTime);

                                    productOrderService.updateOrderMsg(map);
                                } catch (ServiceException e) {
                                    loggerError.error("订单："+orderDTO.getUuId()+" 退款成功。但更新订单状态失败");
                                    System.out.println("订单："+orderDTO.getUuId()+" 退款成功。但更新订单状态失败");
                                    e.printStackTrace();
                                }
                            }

                        } catch (Exception e) {
                            //下单失败
                            loggerError.error("采购单："+deleteOrder.getSpOrderId()+" 退单返回转化失败");
                            System.out.println("采购单：" + deleteOrder.getSpOrderId() + " 退单返回转化失败");
                            e.printStackTrace();
                        }
                    }

                }

            }



        } catch (ServiceException e) {

            e.printStackTrace();
        }
        return false;
    }


    /**
     * 处理尚未有退单操作记录的异常

     */
    private void handleCancelPurchaseOrderException(){
        //拉取采购单存入本地库
        List<ReturnOrderDTO>  orderDTOList= null;
        try {
            orderDTOList  =returnOrderService.getReturnOrderBySupplierIdAndOrderStatus(supplierId, OrderStatus.WAITCANCEL);
            if(null!=orderDTOList){

                for(ReturnOrderDTO deleteOrder:orderDTOList){

                    try {
                        //处理取消订单
                        com.shangpin.iog.dto.OrderDTO orderDTO =   productOrderService.getOrderByUuId(deleteOrder.getUuId());
                        confirmCancelOrder(orderDTO,deleteOrder);

                    } catch (Exception e) {
                        e.printStackTrace();
                        loggerError.error("退单处理失败。失败信息 " + deleteOrder.toString()+" 原因 ：" + e.getMessage() );

                    }
                }
            }

        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    private void updateReturnOrderMsg(String uuId, ServiceException e) throws ServiceException {
        //非200 201 202 返回
        Map<String,String> map = new HashMap<>();
        String reason ="";
        if("状态码:422".equals(e.getMessage())){
            reason ="sku are sold out ";
        }else{
            reason ="code = " +  e.getMessage();
        }
        map.put("uuId",uuId);
        map.put("excDesc",reason);
        map.put("excState","1");
        map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        loggerError.error("订单 ：" + uuId + " 异常。原因 "+e.getMessage());

        returnOrderService.updateReturnOrderMsg(map);
    }

    private static List<OrderDTO> getObjectsByJsonString(String jsonStr){
        Gson gson = new Gson();
        List<OrderDTO> objs = new ArrayList<OrderDTO>();
        try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<OrderDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            //logger.info("get List<ApennineProductDTO> fail :"+e);
        }
        return objs;
    }
    private static OrderDTO getObjectByJsonString(String jsonStr){
        OrderDTO obj=null;
        Gson gson = new Gson();
        try {
            obj=gson.fromJson(jsonStr, OrderDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    private  static void initDate(String  fileName) {
        Date tempDate = new Date();

        endDate = DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH);

        String lastDate=getLastGrapDate(fileName);
        startDate= StringUtils.isNotEmpty(lastDate) ? lastDate: DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH);



        writeGrapDate(endDate,fileName);


    }

    private static File getConfFile(String fileName) throws IOException {
        String realPath = OrderServiceImpl.class.getClassLoader().getResource("").getFile();
        realPath= URLDecoder.decode(realPath, "utf-8");
        File df = new File(realPath+fileName);//"date.ini"
        if(!df.exists()){
            df.createNewFile();
        }
        return df;
    }
    private static String getLastGrapDate(String fileName){
        File df;
        String dstr=null;
        try {
            df = getConfFile(fileName);
            try(BufferedReader br = new BufferedReader(new FileReader(df))){
                dstr=br.readLine();
            }
        } catch (IOException e) {
            logger.error("读取日期配置文件错误");
        }
        return dstr;
    }

    private static void writeGrapDate(String date,String fileName){
        File df;
        try {
            df = getConfFile(fileName);

            try(BufferedWriter bw = new BufferedWriter(new FileWriter(df))){
                bw.write(date);
            }
        } catch (IOException e) {
            logger.error("写入日期配置文件错误");
        }
    }


    public static void main(String[] args) throws  Exception{
        OrderServiceImpl  orderService = new OrderServiceImpl();

        orderService.purchaseOrder();

//        Map<String,List<PurchaseOrderDetail>> orderMap =  new HashMap<>();
//        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
//        PurchaseOrderDetail  purchaseOrderDetail = new PurchaseOrderDetail();
//        purchaseOrderDetails.add(purchaseOrderDetail);
//        orderMap.put("",purchaseOrderDetails);
//
//        orderService.transData("https://api-sandbox.gilt.com/global/orders/","",orderMap);


    }
}
