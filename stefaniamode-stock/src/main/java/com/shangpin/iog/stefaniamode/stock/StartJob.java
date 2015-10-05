package com.shangpin.iog.stefaniamode.stock;

import com.shangpin.iog.app.AppContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by lizhongren on 2015/8/29.
 */
public class StartJob {
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) throws Exception {
        //加载spring
        loadSpringContext();
        logger.info("----初始SPRING成功----");

        GrabStockImp grabStockImp =(GrabStockImp)factory.getBean("stefaniamodeStock");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("VELA更新数据库开始");
        grabStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("VELA更新数据库结束");


        System.exit(0);
    }
}
