package com.shangpin.iog.biancabianca;

import java.util.ResourceBundle;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.biancabianca.service.FetchProduct;

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
    
    private static ResourceBundle bdl=null;
    private static String supplierId = null;
    private static int day;
    private static String picpath = null;
    
    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
            supplierId = bdl.getString("supplierId");
            day = Integer.valueOf(bdl.getString("day"));
            picpath = bdl.getString("picpath");
    }

    public static void main(String[] args)
    {
        //加载spring
        log.info("----拉取biancabianca数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("biancabianca");
//        fetchProduct.fetchProductAndSave("http://www.thelevelgroup-ftp.com/uploads/TLG_GooglePLA_lncc_GB.txt");
//        fetchProduct.fetchProductAndSave();
//        fetchProduct.fetchProductAndSave("http://222.186.51.135:8080/trident/public/TLG_GooglePLA_lncc_GB.txt");
        fetchProduct.handleData("sku", supplierId, day, picpath);
        log.info("----拉取biancabianca数据完成----");


        System.out.println("-------fetch end---------");
    }
}
