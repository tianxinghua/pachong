package com.shangpin.iog.gilt.schedule;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gilt.dto.OrderDTO;
import com.shangpin.iog.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
public class Schedule {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url="https://api-sandbox.gilt.com/global/orders/";
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    @Autowired
    OrderService orderService;






    @Scheduled(cron="0 0/15 * * * ? ")
    public void setUserLevel(){
        try {
            //获取已提交的产品信息
            List<String> uuidList =  orderService.getOrderIdBySupplierIdAndOrderStatus(supplierId, "confirmed");
            Gson gson =new Gson();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
            Map<String,String> param =new HashMap<>();
            String UID ="";
            String result ="";
            for(String uuid:uuidList){
                result = HttpUtil45.post(url+uuid,param,timeConfig);
                OrderDTO dto=gson.fromJson(result,OrderDTO.class);
                if(/*!"confirmed".equals(dto.getStatus())||*/"shipped".equals(dto.getStatus())){

                    Map<String,String >map=new HashMap<>();
                    map.put(uuid,"shipped");
                    orderService.updateOrderStatus(map);
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();

        }
    }
    public static void main(String[] args){
        Gson gson =new Gson();
        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        Map<String,String> param =new HashMap<>();
        String result = HttpUtil45.post(url+"e3eb4b7d-d1bc-4d33-bfe5-4a095485b6b9",param,timeConfig);
        OrderDTO dto=gson.fromJson(result,OrderDTO.class);
        System.out.println("返回结果："+result);
    }
}
