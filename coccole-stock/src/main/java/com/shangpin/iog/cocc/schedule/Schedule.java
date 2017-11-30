package com.shangpin.iog.cocc.schedule;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.cocc.service.TonyStockImp;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
@PropertySource("classpath:conf.properties")
public class Schedule {
    @Autowired
    TonyStockImp tonyStockImp;
    //获取库存
    @Scheduled(cron="${jobsSchedule}")
    public void fetchStock(){

        //拉取数据
        System.out.println("-------tony start---------");
        try {
            tonyStockImp.fetchStock();

            System.out.println("-------tony end---------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
