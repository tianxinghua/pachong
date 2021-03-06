package com.shangpin.iog.dante5;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dante5.service.FetchProduct;
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
        log.info("----拉取dante5数据开始----");
		loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("dante5");
        fetchProduct.fetchProductAndSave("https://www.dante5.com/en-US/home/feedShangpin");

        log.info("----拉取dante5数据完成----");
		System.out.println("-------fetch end---------");

	}

}
