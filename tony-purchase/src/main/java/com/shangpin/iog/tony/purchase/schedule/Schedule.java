package com.shangpin.iog.tony.purchase.schedule;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.tony.purchase.order.OrderImpl;
import com.shangpin.iog.tony.purchase.order.OrderSopImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
public class Schedule {

//    @Autowired
//    OrderImpl orderService;
    
    //已支付订单推送
//    @Scheduled(cron="0 0/2 * * * ? ")
//    public void checkoutOrderFromWMS(){
//        try {
//            orderService.startWMS();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    
    @Autowired
    OrderSopImpl orderSopService;
    //已支付订单推送
    @Scheduled(cron="0 0/2 * * * ? ")
    public void checkoutOrderFromSOP(){
        try {
        	orderSopService.startSOP();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //已支付订单推送
    @Scheduled(cron="0 0/2 * * * ? ")
    public void confirmOrder(){
        try {
        	orderSopService.confirmOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
