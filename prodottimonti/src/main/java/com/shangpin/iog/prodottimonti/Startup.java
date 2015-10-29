package com.shangpin.iog.prodottimonti;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.prodottimonti.service.FetchProduct;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        log.info("----拉取erminiomonti数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("erminiomonti");
//        fetchProduct.fetchProductAndSave("http://www.thelevelgroup-ftp.com/uploads/TLG_GooglePLA_lncc_GB.txt");
        fetchProduct.fetchProductAndSave("http://www.3forb.it/webserver/prodotti/");
        log.info("----拉取erminiomonti数据完成----");


        System.out.println("-------fetch end---------");
    }
}
