package com.shangpin.iog.ostore.schedule;

import com.shangpin.iog.ostore.order.OrderServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {
    Logger logger = LoggerFactory.getLogger(this.getClass());
   
    @Autowired
    OrderServiceImpl orderService;

    //下单 退单 异常
    @Scheduled(cron="${jobSchedule}")
    public void start(){
    	orderService.loopExecute();
    }

    //确认支付
    @Scheduled(cron="${jobConfirmSchedule}")
    public  void confirmOrder(){
    	orderService.confirmOrder();
    }
    
}
