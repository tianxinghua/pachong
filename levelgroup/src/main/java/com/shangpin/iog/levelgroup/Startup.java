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
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("levelgroup");
        fetchProduct.fetchProductAndSave("TLG_GooglePLA_lncc_IT.txt");
//        fetchProduct.fetchProductAndSave("http://www.thelevelgroup-ftp.com/uploads/TLG_GooglePLA_lncc_GB.txt");

        //分开处理 放到项目biancabianca里了 这里不处理
//        FetchProduct2 fetchProduct2 =(FetchProduct2)factory.getBean("levelgroup2");
//        fetchProduct2.fetchProductAndSave();

        log.info("----拉取levelgroup数据完成----");


        System.out.println("-------fetch end---------");
    }
}
