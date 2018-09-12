package com.shangpin.iog.moncler.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * Created by wanner on 2018/6/30.
 *
 * redigroup-memo brands 爬虫品牌：MONCLER 库存价格推送程序 ，注意推送的是 昨天的库存数据
 */
public class Startup {

    private static   Logger logger = LoggerFactory.getLogger(Startup.class);
    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

	public static void main(String[] args)
    {
        //加载spring
    	System.out.println("start");
        loadSpringContext();
        logger.info(" schedule start  ");
    }
}
