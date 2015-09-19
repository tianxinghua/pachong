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
import com.shangpin.iog.gilt.dto.OrderDTO;
import com.shangpin.iog.gilt.dto.OrderDetailDTO;
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
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
    }

    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;
    @Autowired
    ReturnOrderService returnOrderService;
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static String url="https://api-sandbox.gilt.com/global/orders/";

    public void purchaseOrder(){

        //初始化时间
        initDate();
        OrderService iceOrderService = new OrderService();
        try {
            //获取订单数组
            List<Integer> status = new ArrayList<>();
            status.add(1);
            Map<String,List<PurchaseOrderDetail>> orderMap =  iceOrderService.geturchaseOrder(supplierId, startDate, endDate, status);
           //  下单
            String url = "https://api-sandbox.gilt.com/global/orders/";
            transData( url, supplierId,orderMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void deleteOrder(){
        //初始化时间
        initDate();
        OrderService iceOrderService = new OrderService();
        try {
            //获取订单数组
            List<Integer> status = new ArrayList<>();
            status.add(5);
            Map<String,List<PurchaseOrderDetail>> orderMap =  iceOrderService.geturchaseOrder(supplierId, startDate, endDate, status);
            //  下单
            String url = "https://api-sandbox.gilt.com/global/orders/";
            cancelData(url, supplierId, orderMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新订单商品的状态
     */
    public void updateStatus(){
        try {
            //获取已提交的产品信息
            List<String> uuidList =  productOrderService.getOrderIdBySupplierIdAndOrderStatus(supplierId, "confirmed");
            List<String> uuidlist =  productOrderService.getOrderIdBySupplierIdAndOrderStatus(supplierId, "NOWAIT");
            uuidList.addAll(uuidlist);
            Gson gson =new Gson();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
            Map<String,String> param =new HashMap<>();
            String UID ="";
            String result ="";
            for(String uuid:uuidList){
                result=HttpUtil45.get(url +uuid, timeConfig, param, key, "");
                if(HttpUtil45.errorResult.equals(result)){  //链接异常
                    loggerError.error("获取采购单商品发货状态链接异常："+uuid);
                }else {
                    OrderDTO dto = gson.fromJson(result, OrderDTO.class);
                    if (/*!"confirmed".equals(dto.getStatus())||*/null!=dto&&"shipped".equals(dto.getStatus())) {
                        Map<String,String> map = new HashMap<>();
                        map.put("status",dto.getStatus());
                        map.put("uuid",dto.getId());
                        productOrderService.updateOrderStatus(map);
                    }
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            loggerError.error("获得gilt采购单状态更改失败");
        }
    }

    /**
     * 传输库存
     * @param url
     * @param orderMap
     * @throws ServiceException
     */
    public void transData(String url,String supplierId, Map<String, List<PurchaseOrderDetail>> orderMap) throws ServiceException {
        Gson gson = new Gson();
        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        String orderDetail = "",uuid="";
        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();){
            Map.Entry<String,List<PurchaseOrderDetail>> entry = itor.next();
            OrderDTO orderDTO = new OrderDTO();
            Map<String,Integer> stockMap = new HashMap<>();
            //获取同一产品的数量

            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){

                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo,stockMap.get(purchaseOrderDetail.SupplierSkuNo)+1);
                }else{
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo,1);
                }

            }
            List<OrderDetailDTO>list=new ArrayList<>();
            StringBuffer buffer = new StringBuffer();
            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){

               if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                   OrderDetailDTO detailDTO = new OrderDetailDTO();
                   detailDTO.setSku_id(Integer.valueOf(purchaseOrderDetail.SupplierSkuNo));
                   detailDTO.setQuantity(stockMap.get(purchaseOrderDetail.SupplierSkuNo));
                   buffer.append("'").append(detailDTO.getSku_id()).append("'").append(":").append(detailDTO.getQuantity()).append(",");
                   list.add(detailDTO);
                   stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
               }

            }

            orderDTO.setOrder_items(list);
            uuid=UUID.randomUUID().toString();
            /*orderDTO.setId(UUID.randomUUID().toString());
            orderDTO.setStatus("confirmed");*/





            //存储
            com.shangpin.iog.dto.OrderDTO spOrder =new com.shangpin.iog.dto.OrderDTO();
            spOrder.setUuId(uuid);
            spOrder.setSupplierId(supplierId);
            spOrder.setStatus("NOWAIT");
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
                String result =  HttpUtil45.operateData("put", "json", url + uuid, timeConfig, null, param, key, "");
                if(HttpUtil45.errorResult.equals(result)){  //链接异常
                    loggerError.error("采购单："+spOrder.getSpOrderId()+" 链接异常 无法处理");
                }else{
                    logger.info("订单处理结果 ：" + result);
                    System.out.println("订单处理结果 ：" + result);
                    //更新      日志存储，数据库更新

                    try {
                        if(result.indexOf("message")>0&&result.indexOf("type")>0){ //  失败
                            loggerError.error("采购单："+spOrder.getSpOrderId()+" 下单返回转化失败");
                            System.out.println("采购单：" + spOrder.getSpOrderId() + " 下单返回转化失败");
                            continue;
                        }
                        OrderDTO dto= getObjectByJsonString(result);
                        if(null==dto){
                            loggerError.error("采购单："+spOrder.getSpOrderId()+" 下单返回转化失败");
                            System.out.println("采购单：" + spOrder.getSpOrderId() + " 下单返回转化失败");
                            continue;
                        }
                        //
                        //更新订单状态
                        Map<String,String> map = new HashMap<>();
                        map.put("status",dto.getStatus());
                        map.put("uuid",dto.getId());
                        try {
                            //
                            if("placed".equals(dto.getStatus())){
                                map.put("status","confirmed");
                            }
                            String returnStr=HttpUtil45.operateData("patch", "json", url + uuid, timeConfig, map, param, key, "");
                            logger.info("更新gilt端订单状态："+returnStr);
                            productOrderService.updateOrderStatus(map);
                        } catch (ServiceException e) {
                            loggerError.error("采购单："+spOrder.getSpOrderId()+" 下单成功。但更新订单状态失败");
                            System.out.println("采购单：" + spOrder.getSpOrderId() + " 下单成功。但更新订单状态失败");
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        //下单失败
                        loggerError.error("采购单："+spOrder.getSpOrderId()+" 下单返回转化失败");
                        System.out.println("采购单：" + spOrder.getSpOrderId() + " 下单返回转化失败");
                        e.printStackTrace();
                    }
                }



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
                Map<String,String>pramap=new HashMap<>();
                String param ="";
                pramap.put("status","cancelled");
                logger.info("传入退单内容 ：" + param);
                System.out.println("传入退单内容 ：" + param);
                String result =  HttpUtil45.operateData("delete", "json", url + deleteOrder.getUuId(), timeConfig, pramap, param, key, "");
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
                       /* OrderDTO dto= getObjectByJsonString(result);
                        if(null==dto){
                            loggerError.error("采购单："+deleteOrder.getSpOrderId()+" 退单返回转化失败");
                            System.out.println("采购单：" + deleteOrder.getSpOrderId() + " 退单返回转化失败");
                            continue;
                        }
                        //
                        //更新退单状态
                        Map<String,String> map = new HashMap<>();
                        map.put("status",dto.getStatus());
                        map.put("uuid",dto.getId());
                        try {
                            productOrderService.updateOrderStatus(map);
                        } catch (ServiceException e) {
                            loggerError.error("采购单："+deleteOrder.getSpOrderId()+" 退单成功。但更新退单状态失败");
                            System.out.println("采购单：" + deleteOrder.getSpOrderId() + " 退单成功。但更新退单状态失败");
                            e.printStackTrace();
                        }*/
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

    private  static void initDate() {
        Date tempDate = new Date();

        endDate = DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH);

        String lastDate=getLastGrapDate();
        startDate= StringUtils.isNotEmpty(lastDate) ? lastDate: DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH);



        writeGrapDate(endDate);


    }

    private static File getConfFile() throws IOException {
        String realPath = OrderServiceImpl.class.getClassLoader().getResource("").getFile();
        realPath= URLDecoder.decode(realPath, "utf-8");
        File df = new File(realPath+"date.ini");
        if(!df.exists()){
            df.createNewFile();
        }
        return df;
    }
    private static String getLastGrapDate(){
        File df;
        String dstr=null;
        try {
            df = getConfFile();
            try(BufferedReader br = new BufferedReader(new FileReader(df))){
                dstr=br.readLine();
            }
        } catch (IOException e) {
            logger.error("读取日期配置文件错误");
        }
        return dstr;
    }

    private static void writeGrapDate(String date){
        File df;
        try {
            df = getConfFile();

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
