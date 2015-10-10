package com.shangpin.iog.monnierfreres;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.monnierfreres.service.FetchProduct;
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
        log.info("----拉取monnierfreres数据开始----");
        System.out.println("----拉取monnierfreres数据开始----");
        loadSpringContext();
        log.info("----初始monnierfreres成功----");
        System.out.println("----初始monnierfreres成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("monnierfreres");
        fetchProduct.fetchProductAndSave("http://prod.monnierfreres.com/media/lengow/default/lengow_feed.csv");
        log.info("----拉取monnierfreres数据完成----");
        System.out.println("----拉取monnierfreres数据完成----");

	}

}
