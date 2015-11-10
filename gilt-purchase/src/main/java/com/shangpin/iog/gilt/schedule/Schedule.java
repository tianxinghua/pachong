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
    private static String key ;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
    }

    @Autowired
    OrderServiceImpl orderService;

    //下单 退单 异常
    @Scheduled(cron="0 0/3 * * * ? ")
    public void putOrder(){

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

    //确认支付
    @Scheduled(cron="0 0/5 * * * ? ")
    public  void confirmOrder(){
        try {
            orderService.confirmOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //确认发货
//   @Scheduled(cron="0 0/15 * * * ? ")
    public void deliveryOrder(){
        System.out.println("-------deliveryOrder start---------");
        try {
            orderService.deliveryOrder();
            System.out.println("-------deliveryOrder end---------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






    public static void main(String[] args){

    }
}
