package com.shangpin.iog.monti;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.monti.service.FetchProduct;

public class StartUp {

	private static Logger log = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String picpath = null;
	
	static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        day = Integer.valueOf(bdl.getString("day"));
        picpath = bdl.getString("picpath");
    }

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) throws Exception {
        //加载spring
        log.info("----拉取monti数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("monti");
        fetchProduct.handleData("sku", supplierId, day, picpath);
        log.info("----拉取monti数据完成----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
}
