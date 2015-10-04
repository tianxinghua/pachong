package com.shangpin.iog.atelier.purchase.order;

import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.OrderService;
import com.shangpin.iog.atelier.common.MyStringUtil;
import com.shangpin.iog.atelier.common.WS_Sito_P15;
import com.shangpin.iog.atelier.purchase.dto.*;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.service.ReturnOrderService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by loyalty on 15/9/9.
 */
@Component("atelierOrder")
public class OrderServiceImpl {

    private static String  startDate=null,endDate=null;
    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";

    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String key ;
    private static String orderFile ;
    private static int period ;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
        orderFile = bdl.getString("newOrder");
        period = Integer.parseInt(bdl.getString("period"));
    }

    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;
    @Autowired
    ReturnOrderService returnOrderService;
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static String url="https://api-sandbox.gilt.com/global/orders/";

    /**
     * 下订单
     */
    public void purchaseOrder(){

        //初始化时间
        initDate("date.ini");
        //获取条形码
        WS_Sito_P15 atelier = new WS_Sito_P15();
        //atelier.getAllAvailabilityMarketplace();
        String barCodeAll = atelier.getAllAvailabilityStr();
        String barCode = "";
        CreateOrderDTO order = new CreateOrderDTO();
        order.setID_CLIENTE_WEB("45");
        order.setDESTINATIONROW1("----------------");
        order.setDESTINATIONROW2("----------------");
        order.setDESTINATIONROW3("----------------");
        order.setQTY("1");
        OrderService iceOrderService = new OrderService();
        try {
            //获取订单数组
            List<Integer> status = new ArrayList<>();
            //1=待处理
            status.add(1);
            //5=已取消
            status.add(5);
           // Map<String,List<PurchaseOrderDetail>> orderMap =  iceOrderService.getPurchaseOrder(supplierId, startDate, endDate, status);

            Map<String,List<PurchaseOrderDetail>> orderMap =  new HashMap<>();
            List<PurchaseOrderDetail> dlist = new ArrayList<>();
            PurchaseOrderDetail de = new PurchaseOrderDetail();
            de.SopPurchaseOrderDetailNo = "123";
            de.SkuPrice = "123";
            de.SupplierSkuNo = "327922";
            dlist.add(de);
            orderMap.put("1111",dlist);
            /*            //  正常下单
            String url = "https://api-sandbox.gilt.com/global/orders/";
            transData( url, supplierId,orderMap);
            //下单异常 再次下单
            handlePurchaseOrderException();*/
            System.out.println(orderMap.size());
            //defination
            //CreateOrderDTO order = getOrder();

            for (String key:orderMap.keySet()){
                System.out.println(key);
                List<PurchaseOrderDetail> list = orderMap.get(key);
                System.out.println("list.size()=="+list.size());
                for(PurchaseOrderDetail pur:list){
                    barCode = MyStringUtil.getBarcodeBySkuId(barCodeAll.substring(
                            barCodeAll.indexOf(pur.SupplierSkuNo),barCodeAll.indexOf(pur.SupplierSkuNo)+50));
                    order.setID_ORDER_WEB(pur.SopPurchaseOrderDetailNo);
                    order.setPRICE(pur.SkuPrice);
                    order.setBARCODE(barCode);
                    //取消订单
                    if (pur.DetailStatus == 5){
                        orderAmendment(order);
                    }
                    //开始下单
                    newOrder(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 在线推送订单
     */
    public void newOrder(CreateOrderDTO order){
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <NewOrder xmlns=\"http://tempuri.org/\">\n" +
                "      <ID_ORDER_WEB>" + order.getID_ORDER_WEB() + "</ID_ORDER_WEB>\n" +
                "      <ID_CLIENTE_WEB>" + order.getID_CLIENTE_WEB() + "</ID_CLIENTE_WEB>\n" +
                "      <DESTINATIONROW1>" + order.getDESTINATIONROW1() + "</DESTINATIONROW1>\n" +
                "      <DESTINATIONROW2>" + order.getDESTINATIONROW2() + "</DESTINATIONROW2>\n" +
                "      <DESTINATIONROW3>" + order.getDESTINATIONROW3() + "</DESTINATIONROW3>\n" +
                "      <BARCODE>" + order.getBARCODE() + "</BARCODE>\n" +
                "      <QTY>" + order.getQTY() + "</QTY>\n" +
                "      <PRICE>" + order.getPRICE() + "</PRICE>\n" +
                "    </NewOrder>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        System.out.println("soapRequestData=="+soapRequestData);
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=NewOrder";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/NewOrder");
        postMethod.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");

        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
/*            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File(orderFile)));
            BufferedInputStream in=new BufferedInputStream(postMethod.getResponseBodyAsStream());
            int length = 0;
            byte[] b = new byte[10240];
            while((length = in.read(b,0,10240)) != -1)
            {
                out.write(b, 0, length);
            }
            in.close();
            out.flush();
            out.close();*/
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 在线取消订单
     */
    public void orderAmendment(CreateOrderDTO order){
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <OrderAmendment xmlns=\"http://tempuri.org/\">\n" +
                "      <ID_ORDER_WEB>" + order.getID_ORDER_WEB() + "</ID_ORDER_WEB>\n" +
                "      <ID_CLIENTE_WEB>" + order.getID_CLIENTE_WEB() + "</ID_CLIENTE_WEB>\n" +
                "      <BARCODE>" + order.getBARCODE() + "</BARCODE>\n" +
                "      <QTY>0</QTY>\n" +
                "    </OrderAmendment>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        System.out.println("soapRequestData=="+soapRequestData);
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=OrderAmendment";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/OrderAmendment");
        postMethod.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");

        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
/*            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File(orderFile)));
            BufferedInputStream in=new BufferedInputStream(postMethod.getResponseBodyAsStream());
            int length = 0;
            byte[] b = new byte[10240];
            while((length = in.read(b,0,10240)) != -1)
            {
                out.write(b, 0, length);
            }
            in.close();
            out.flush();
            out.close();*/
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 支付确认
     */
    public void confirmOrder(){
        //1 、 获取已下单的订单信息
        //TODO  下单成功（尚品网支付成功） ，但支付时如果链接不上网  超过两个小时

        List<String>  uuidList= null;
        try {
            uuidList  =productOrderService.getOrderIdBySupplierIdAndOrderStatus(supplierId,"placed");
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        //2、向gilt 确认支付成功
        String param="{\"status\" : \"confirmed\"}";
        String returnStr="";
        if(null!=uuidList){
            Gson gson = new Gson();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
            for(String uuid :uuidList){
                try {
                    returnStr=HttpUtil45.operateData("patch", "json", url + uuid, timeConfig, null, param, key, "");
                    logger.info("更新gilt端订单状态返回信息："+returnStr);
                } catch (ServiceException e) {

                    e.printStackTrace();
                }
                if(HttpUtil45.errorResult.equals(returnStr)){  //链接异常
                    loggerError.error("支付下单编号 ："+uuid+" -链接异常");
                    setConnectionError(uuid);
                    continue;

                }else{
                    if(returnStr.indexOf("message")>0&&returnStr.indexOf("type")>0){//处理失败

                        loggerError.error("支付下单 失败:"+returnStr);

                        ErrorDTO errorDTO = gson.fromJson(returnStr, ErrorDTO.class);
                        Map<String,String> map = new HashMap<>();
                        map.put("excDesc",null!=errorDTO?(" 订单号 "+ uuid + " 商品 " + (null!=errorDTO.getSku_id()?errorDTO.getSku_id():" ") + ":"+errorDTO.getType()):(uuid + "确认异常"));
                        setErrorMsg(uuid, map);
                        continue;
                    }else{

                        //更新订单状态
                        Map<String,String> map = new HashMap<>();
                        map.put("status","confirmed");
                        map.put("uuid",uuid);
                        try {
                            productOrderService.updateOrderStatus(map);
                        } catch (ServiceException e) {
                            loggerError.error("订单："+uuid+" gilt支付确认成功，但更新订单状态失败时失败");
                            System.out.println("订单："+uuid+" gilt支付确认成功，但更新订单状态失败时失败");
                            e.printStackTrace();
                        }

                    }
                }
            }

        }




    }

    private void setErrorMsg(String uuid, Map<String, String> map) {
        map.put("uuid",uuid);
        map.put("excState","1");

        map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        try {
            productOrderService.updateExceptionMsg(map);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消订单
     */
    public void deleteOrder(){
        //初始化时间
        initDate("cancel.ini");
        OrderService iceOrderService = new OrderService();
        try {
            //获取订单数组
            List<Integer> status = new ArrayList<>();
            status.add(5);
            Map<String,List<PurchaseOrderDetail>> orderMap =  iceOrderService.getPurchaseOrder(supplierId, startDate, endDate, status);
            //  下单
            String url = "https://api-sandbox.gilt.com/global/orders/";
            cancelData(url, supplierId, orderMap);

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
            List<com.shangpin.iog.dto.OrderDTO> uuidList =  productOrderService.getOrderBySupplierIdAndOrderStatus(supplierId, "confirmed");
            Gson gson =new Gson();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
            Map<String,String> param =new HashMap<>();
            String uuid ="";
            String result ="";
            OrderService iceOrderService = new OrderService();
            for(com.shangpin.iog.dto.OrderDTO orderDTO:uuidList){
                uuid=orderDTO.getUuId();
                result=HttpUtil45.get(url +uuid, timeConfig, param, key, "");
                logger.info("查询是否发货："+result);
                if(HttpUtil45.errorResult.equals(result)){  //链接异常
                    loggerError.error("获取采购单商品发货状态链接异常："+uuid);
                    // 是否更新订单异常状态.
                    setConnectionError(uuid);
                    continue;
                }else {

                    OrderDTO dto = gson.fromJson(result, OrderDTO.class);

                    if (null != dto && "shipped".equals(dto.getStatus())) {
                        //通知SOP已发货
                        List<String> purchaseOrderIdList = new ArrayList<>();
                        purchaseOrderIdList.add(orderDTO.getSpOrderId());
                        try {
                            String  deliverNo=  iceOrderService.getPurchaseDeliveryOrderNo(supplierId,
                                    "","","",5,"","","", "","","",purchaseOrderIdList,0);
                            //更新海外对接库
                            Map<String, String> map = new HashMap<>();
                            map.put("status", dto.getStatus());
                            map.put("uuid", dto.getId());
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
                            map.put("uuid", dto.getId());
                            map.put("updateTime", date);
                            map.put("excState","1");
                            map.put("excDesc","订单:" + orderDTO.getUuId() + "已发货，但推送发货单信息时失败" );
                            map.put("excTime", date);
                            try {
                                productOrderService.updateOrderMsg(map);
                            } catch (ServiceException e1) {
                                loggerError.error("订单:" + orderDTO.getUuId() + " 已发货。" + " 采购单：" + orderDTO.getSpOrderId() + " 推送发货单信息失败。 保存信息失败");
                            }


                        }


                    }else{
                        loggerError.error("订单:" + uuid + "获取订单信息失败。可能被gilt删除。");
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
     * @param uuid
     */
    private void setConnectionError(String uuid) {
        Map<String,String> map = new HashMap<>();
        map.put("uuid",uuid);
        map.put("excState","1");
        map.put("excDesc","链接异常，无法下单");
        map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        try {
            productOrderService.updateExceptionMsg(map);
        } catch (ServiceException e) {
            loggerError.error("支付下单编号 ：" + uuid + " -链接异常。更新异常信息时失败");
        }
    }

    /**
     * 下单 先存入本地库 去通知gilt 更改本地库状态
     * @param url
     * @param orderMap
     * @throws ServiceException
     */
    public void transData(String url,String supplierId, Map<String, List<PurchaseOrderDetail>> orderMap) throws ServiceException {
        Gson gson = new Gson();
        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        String uuid="";
        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();){
            Map.Entry<String, List<PurchaseOrderDetail>> entry = itor.next();
            OrderDTO orderDTO = new OrderDTO();
            Map<String,Integer> stockMap = new HashMap<>();
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
            uuid=UUID.randomUUID().toString();

            //存储
            com.shangpin.iog.dto.OrderDTO spOrder =new com.shangpin.iog.dto.OrderDTO();
            spOrder.setUuId(uuid);
            spOrder.setSupplierId(supplierId);
            spOrder.setStatus("WAITING");
            spOrder.setSpOrderId(entry.getKey());
            spOrder.setDetail(buffer.toString());
            spOrder.setCreateTime(new Date());
            try {
                logger.info("采购单信息转化订单后信息："+spOrder.toString());
                System.out.println("采购单信息转化订单后信息："+spOrder.toString());
                productOrderService.saveOrder(spOrder);

                String param = gson.toJson(orderDTO,new TypeToken<OrderDTO>(){}.getType());
                logger.info("传入订单内容 ：" + param);
                System.out.println("传入订单内容 ：" + param);
                if (informOrderForGilt(url, gson, timeConfig, uuid, spOrder, param,"")) continue;


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

    private boolean informOrderForGilt(String url, Gson gson, OutTimeConfig timeConfig, String uuid, com.shangpin.iog.dto.OrderDTO spOrder, String param,String errorType) throws ServiceException {
        String result = null;
        try {
            result = HttpUtil45.operateData("put", "json", url + uuid, timeConfig, null, param, key, "");
        } catch (ServiceException e) {
            //非200返回
            Map<String,String> map = new HashMap<>();
            String reason ="";
            if("状态码:422".equals(e.getMessage())){
                reason ="sku are sold out ";
            }else{
                reason ="code = " +  e.getMessage();
            }
            map.put("uuid",uuid);
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

            this.setConnectionError(uuid);
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
                    map.put("excDesc",null!=errorDTO?(" 订单号 "+ uuid + " 商品 " + (null!=errorDTO.getSku_id()?errorDTO.getSku_id():" ") + ":"+errorDTO.getType()):(uuid + "下单异常"));
                    setErrorMsg(uuid, map);
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
                map.put("status",dto.getStatus());
                map.put("uuid",dto.getId());
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
            orderDTOList  =productOrderService.getOrderBySupplierIdAndOrderStatus(supplierId, "WAITING");
            if(null!=orderDTOList){
                String orderDetail = "",orderMsg="";
                Gson gson = new Gson();
                OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
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
     * @param url
     * @param supplierId
     * @param orderMap
     * @throws ServiceException
     */
    public void cancelData(String url,String supplierId, Map<String, List<PurchaseOrderDetail>> orderMap) throws ServiceException {
        Gson gson = new Gson();
        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        String orderDetail = "";
        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();) {
            Map.Entry<String, List<PurchaseOrderDetail>> entry = itor.next();
            OrderDTO orderDTO = new OrderDTO();
            Map<String, Integer> stockMap = new HashMap<>();
            for (PurchaseOrderDetail purchaseOrderDetail : entry.getValue()) {
                if (stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)) {
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, stockMap.get(purchaseOrderDetail.SupplierSkuNo) + 1);
                } else {
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, 1);
                }
            }
            List<OrderDetailDTO> list = new ArrayList<>();
            StringBuffer buffer = new StringBuffer();
            for (PurchaseOrderDetail purchaseOrderDetail : entry.getValue()) {
                if (stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)) {
                    buffer.append("'").append(Integer.valueOf(purchaseOrderDetail.SupplierSkuNo)).append("'").append(":").append(stockMap.get(purchaseOrderDetail.SupplierSkuNo)).append(",");
                    stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
                }
            }
            /**
             * 根据sp_order_id查询UUID
             */
            String uuid = productOrderService.getUuIdByspOrderId(entry.getKey());
            //存储
            com.shangpin.iog.dto.ReturnOrderDTO deleteOrder =new com.shangpin.iog.dto.ReturnOrderDTO();
            deleteOrder.setUuId(uuid);
            deleteOrder.setSupplierId(supplierId);
            deleteOrder.setStatus("WAITCANCEL");
            deleteOrder.setSpOrderId(entry.getKey());
            deleteOrder.setDetail(buffer.toString());
            deleteOrder.setCreateTime(new Date());
            try{
                logger.info("采购单信息转化退单后信息："+deleteOrder.toString());
                System.out.println("采购单信息转化退单后信息："+deleteOrder.toString());
                returnOrderService.saveOrder(deleteOrder);
                String param ="";


                String result =  HttpUtil45.operateData("patch", "json", url + deleteOrder.getUuId(), timeConfig, null, param, key, "");
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
                            continue;
                        }

                    } catch (Exception e) {
                        //下单失败
                        loggerError.error("采购单："+deleteOrder.getSpOrderId()+" 退单返回转化失败");
                        System.out.println("采购单：" + deleteOrder.getSpOrderId() + " 退单返回转化失败");
                        e.printStackTrace();
                    }
                }
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
