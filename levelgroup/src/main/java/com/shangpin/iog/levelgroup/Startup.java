package com.shangpin.iog.levelgroup;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.levelgroup.service.FetchProduct;
import com.shangpin.iog.levelgroup.service.FetchProduct2;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Created by sunny on 2015/8/17.
 */
public class Startup {


    private static Logger log = Logger.getLogger("info");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args)
    {
        //加载spring
        log.info("----拉取levelgroup数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
       // FetchProduct fetchProduct =(FetchProduct)factory.getBean("levelgroup");
        FetchProduct2 fetchProduct2 =(FetchProduct2)factory.getBean("levelgroup2");
//        fetchProduct.fetchProductAndSave("http://www.thelevelgroup-ftp.com/uploads/TLG_GooglePLA_lncc_GB.txt");
        //fetchProduct.fetchProductAndSave("TLG_GooglePLA_lncc_IT.txt");
        fetchProduct2.fetchProductAndSave();
//        fetchProduct.fetchProductAndSave("http://222.186.51.135:8080/trident/public/TLG_GooglePLA_lncc_GB.txt");

        log.info("----拉取levelgroup数据完成----");


        System.out.println("-------fetch end---------");
    }
}
