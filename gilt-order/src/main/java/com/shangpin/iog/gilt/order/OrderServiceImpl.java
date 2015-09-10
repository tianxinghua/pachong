package com.shangpin.iog.gilt.order;

import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.ice.ice.OrderService;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gilt.dto.GiltSkuDTO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/9/9.
 */
@Component("giltOrder")
public class OrderServiceImpl {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private String key = "fb8ea6839b486dba8c5cabb374c03d9d";

    public void purOrder(String supplierId,String startTime ,String endTime,List<Integer> statusList){
        OrderService orderService = new OrderService();
        try {
            //获取订单数组
            Gson gson = new Gson();
            String url = "https://api-sandbox.gilt.com/global/orders/";
            Map<String,List<PurchaseOrderDetail>> orderMap =  orderService.geturchaseOrder(supplierId, startTime, endTime, statusList);
            OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
            for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();){
                Map.Entry<String,List<PurchaseOrderDetail>> entry = itor.next();
                List<PurchaseOrderDetail> orderDetailList = entry.getValue();
                //
                String param = gson.toJson(orderDetailList,new TypeToken<List<PurchaseOrderDetail>>(){}.getType());

                String result =  HttpUtil45.operateData("put","json",url+ UUIDGenerator.getUUID(),timeConfig,null,param,key,"");
                //TODO  存储


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws  Exception{
        OrderServiceImpl  orderService = new OrderServiceImpl();


    }
}
