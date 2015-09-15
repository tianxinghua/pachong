package com.shangpin.iog.tessabit.stock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.tessabit.stock.service.FetchProducts;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by wangyuzhi on 2015/9/11.
 */
public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) throws Exception {
        //加载spring
        loadSpringContext();
        log.info("----初始SPRING成功--1--");
        //拉取数据
        log.info("----拉取tessabit数据开始----");
        FetchProducts fetchProducts =(FetchProducts)factory.getBean("tessabit");
        //fetchProducts.process();
        //单独测试数据入库处理
        fetchProducts.saveDbProcess();
        log.info("----拉取tessabit数据完成----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }

}
