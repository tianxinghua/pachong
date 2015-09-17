package com.shangpin.iog.gilt.schedule;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
public class Schedule {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static ResourceBundle bdl=null;
    private static String supplierId;

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
        } catch (ServiceException e) {
            e.printStackTrace();

        }
    }
}
