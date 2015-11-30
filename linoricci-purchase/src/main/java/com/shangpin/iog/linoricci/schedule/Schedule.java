package com.shangpin.iog.linoricci.schedule;

import com.shangpin.iog.linoricci.order.LinoricciOrderServiceImpl;

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
    private static String supplierNo ;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
    }

    @Autowired
    LinoricciOrderServiceImpl orderService;

    //下单 退单 异常
    @Scheduled(cron="0 0/2 * * * ? ")
    public void start(){
    	orderService.checkoutOrderFromWMS(supplierNo, supplierId, true);
    }

    //确认支付
    @Scheduled(cron="0 0/3 * * * ? ")
    public  void confirmOrder(){
        try {
            orderService.confirmOrder(supplierId);;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
