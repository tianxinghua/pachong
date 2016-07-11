package com.shangpin.iog.filippo.schedule;
import com.shangpin.iog.filippo.order.LogisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.filippo.order.FilippoOrderServiceImpl;

import java.util.*;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
@PropertySource("classpath:conf.properties")
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
    FilippoOrderServiceImpl orderService;

    @Autowired
    LogisticsService logisticsService;



    //下单 退单 异常
    @Scheduled(cron="0 0/2 * * * ? ")
    public void start(){
    	orderService.checkoutOrderFromWMS(supplierNo, supplierId, true);
    }

    //确认支付
    @Scheduled(cron="0 0/3 * * * ? ")
    public  void confirmOrder(){
        try {
            orderService.confirmOrder(supplierId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "${shipOrderSchedule}")
    public void handleShippedOrder(){
        try {
            logisticsService.handleShippedOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //	@Scheduled(cron = "${invoiceSchedule}")
    public void handleInvoice(){
        try {
            logisticsService.handleInvoice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
