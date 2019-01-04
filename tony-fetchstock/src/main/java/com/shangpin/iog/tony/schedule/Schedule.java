package com.shangpin.iog.tony.schedule;



import com.shangpin.iog.tony.service.TonyStockImp;
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
    TonyStockImp tonyStockImp;

    //获取库存
    @Scheduled(cron="0/20 * * * * ? ")
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








    public static void main(String[] args){

    }
}
