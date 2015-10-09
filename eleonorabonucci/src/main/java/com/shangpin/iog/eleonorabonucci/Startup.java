package com.shangpin.iog.eleonorabonucci;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.eleonorabonucci.service.FetchProduct;
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
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("eleonorabonucci");
        //需要翻墙
//        fetchProduct.fetchProductAndSave("http://www.eleonorabonucci.com/rss/demo.aspx");
        fetchProduct.fetchProductAndSave("http://127.0.0.1:8080/demo.aspx");

        log.info("----拉取dante5数据完成----");
		System.out.println("-------fetch end---------");

	}

}
