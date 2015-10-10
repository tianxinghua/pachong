package com.shangpin.iog.dellogliostore;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dellogliostore.service.FetchProduct;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Startup
{
    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
	private static void loadSpringContext()

	{
        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args)
	{
        //加载spring
        log.info("----拉取dellogliostore数据开始----");
		loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("dellogliostore");
        //需要翻墙
        fetchProduct.fetchProductAndSave("http://www.dellogliostore.com/admin/temp/xi125.xml");
        //本地地址
//        fetchProduct.fetchProductAndSave("httwep://127.0.0.1:8080/xi125.xml");

        log.info("----拉取dellogliostore数据完成----");
        System.out.println("-------fetch end---------");

	}

}
