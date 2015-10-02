package com.shangpin.iog.giglio;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.giglio.service.FetchProduct;
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
        log.info("----拉取giglio数据开始----");
        System.out.println("----拉取giglio数据开始----");
        loadSpringContext();
        log.info("----初始giglio成功----");
        System.out.println("----初始giglio成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("giglio");
        fetchProduct.fetchProductAndSave("http://www.giglio.com/feeds/shangpin.csv");
        log.info("----拉取giglio数据完成----");
        System.out.println("----拉取giglio数据完成----");

	}

}
