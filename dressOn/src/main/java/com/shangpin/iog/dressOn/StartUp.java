package com.shangpin.iog.dressOn;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dressOn.service.FetchProduct;

public class StartUp {
	 private static Logger log = Logger.getLogger("info");

	    private static ApplicationContext factory;
		private static void loadSpringContext()

		{

	        factory = new AnnotationConfigApplicationContext(AppContext.class);
		}
		
		public static void main(String[] args)
		{

	        //加载spring
	        log.info("----拉取dressOn数据开始----");
			loadSpringContext();
			System.out.println("初始化Spring成功");
	        //拉取数据
	        FetchProduct fetchProduct =(FetchProduct)factory.getBean("dressOn");
	        fetchProduct.fechProduct();
	        log.info("成功插入数据库");
			System.out.println("-------fetch end---------");
			System.exit(0);

		}
}
