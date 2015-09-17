package com.shangpin.iog.gilt.order;

import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.OrderService;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gilt.dto.OrderDTO;
import com.shangpin.iog.gilt.dto.OrderDetailDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by loyalty on 15/9/9.
 */
@Component("giltOrder")
public class OrderServiceImpl {
    @Autowired
    com.shangpin.iog.service.OrderService orderService;
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private String key = "fb8ea6839b486dba8c5cabb374c03d9d";

    public void purOrder(String supplierId,String startTime ,String endTime,List<Integer> statusList){
        OrderService orderService = new OrderService();
        try {
            //获取订单数组

            String url = "https://api-sandbox.gilt.com/global/orders/";
            Map<String,List<PurchaseOrderDetail>> orderMap =  orderService.geturchaseOrder(supplierId, startTime, endTime, statusList);
            transData( url, supplierId,orderMap);

        } catch (Exception e) {
            e.printStackTrace();
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
        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();){
            Map.Entry<String,List<PurchaseOrderDetail>> entry = itor.next();
            OrderDTO orderDTO = new OrderDTO();
            Map<String,Integer> stockMap = new HashMap<>();
            List<OrderDetailDTO> detailDTOs = new ArrayList<>();
            //获取同一产品的数量

            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){

                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo,stockMap.get(purchaseOrderDetail.SupplierSkuNo)+1);
                }else{
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo,1);
                }

            }
            List<OrderDetailDTO>list=new ArrayList<>();
            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){

               if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                   OrderDetailDTO detailDTO = new OrderDetailDTO();
                   detailDTO.setSku_id(purchaseOrderDetail.SupplierSkuNo);
                   detailDTO.setQuantity(String.valueOf(stockMap.get(purchaseOrderDetail.SupplierSkuNo)));
                   list.add(detailDTO);
                   stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
               }

            }
            orderDTO.setOrder_items(list);
            orderDTO.setId(UUIDGenerator.getUUID());
            orderDTO.setStatus("confirmed");
            String param = gson.toJson(orderDTO,new TypeToken<OrderDTO>(){}.getType());

            String result =  HttpUtil45.operateData("put", "json", url + UUIDGenerator.getUUID(), timeConfig, null, param, key, "");
            //TODO  存储
            /**
             * 日志存储，数据库存储
             */
            OrderDTO dto= getObjectByJsonString(result);
            com.shangpin.iog.dto.OrderDTO order=new com.shangpin.iog.dto.OrderDTO();
            /*order.setId();*/
            for(int i=0;i<dto.getOrder_items().size();i++){
                order.setUuId(dto.getId());
                order.setSupplierId(supplierId);
                order.setStatus(dto.getStatus());
                order.setSpOrderId(entry.getKey());
                order.setDetail(dto.getOrder_items().get(i).getSku_id()+"-"+dto.getOrder_items().get(i).getQuantity());
                order.setCreateTime(new Date());
                orderService.saveOrder(order);
            }
            logger.info("----gilt 订单存储完成----");
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
    public static void main(String[] args) throws  Exception{
        OrderServiceImpl  orderService = new OrderServiceImpl();

        Map<String,List<PurchaseOrderDetail>> orderMap =  new HashMap<>();
        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
        PurchaseOrderDetail  purchaseOrderDetail = new PurchaseOrderDetail();
        purchaseOrderDetails.add(purchaseOrderDetail);
        orderMap.put("",purchaseOrderDetails);

        orderService.transData("https://api-sandbox.gilt.com/global/orders/","",orderMap);


    }
}
