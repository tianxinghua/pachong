package com.shangpin.iog.monnierfreres;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.apache.log4j.Logger;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.monnierfreres.service.FetchProduct;


/**
 * Created by zhaogenchun on 2015/8/5.
 */
public class Startup {

	private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args){
    
        //加载spring
        loadSpringContext();
        log.info("----初始SPRING成功--1--");
        //拉取数据
        log.info("----拉取数据开始----");              
        System.out.println("-------fetch start---------");
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("monnierfreres");
        fetchProduct.fetchProductAndSave();
        log.info("----拉取数据完成----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
}
