package com.shangpin.iog.gilt.schedule;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gilt.dto.OrderDTO;
import com.shangpin.iog.gilt.order.OrderServiceImpl;
import com.shangpin.iog.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
public class Schedule {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url="https://api-sandbox.gilt.com/global/orders/";


    @Autowired
    OrderServiceImpl orderService;

    @Scheduled(cron="0 0/3 * * * ? ")
    public void setUserLevel(){

        //拉取数据
        System.out.println("-------gilt start---------");
        try {
            orderService.purchaseOrder();
            System.out.println("成功插入数据库");
            System.out.println("-------gilt end---------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Scheduled(cron="0 0/15 * * * ? ")
    public void updateStatus(){
        System.out.println("-------updatestatus start---------");
        try {
            orderService.getStatus();
            System.out.println("成功插入数据库");
            System.out.println("-------updatestatus end---------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args){
        Gson gson =new Gson();
        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        Map<String,String> param =new HashMap<>();
        String result = HttpUtil45.post(url+"2f4772df-2981-4bc3-859b-02b51870a563",param,timeConfig);
        OrderDTO dto=gson.fromJson(result,OrderDTO.class);
        System.out.println("返回结果："+result);
    }
}
