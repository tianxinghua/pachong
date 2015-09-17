package com.shangpin.iog.gilt.schedule;

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

    private static String url="https://api-sandbox.gilt.com/global/orders/";


    @Autowired
    OrderServiceImpl orderService;

    @Scheduled(cron="0 0/15 * * * ? ")
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
    public static void main(String[] args){
        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        Map<String,String> param =new HashMap<>();
        String result = HttpUtil45.post(url+"e3eb4b7d-d1bc-4d33-bfe5-4a095485b6b9",param,timeConfig);
        System.out.println("返回结果："+result);
    }
}
