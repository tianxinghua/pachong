package com.shangpin.iog.notenom;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.notenom.service.FetchProduct;

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
	        log.info("----拉取notenom数据开始----");
			loadSpringContext();
	        log.info("----初始SPRING成功----");
	        //拉取数据
	        FetchProduct fetchProduct =(FetchProduct)factory.getBean("notenom");
	        fetchProduct.fetchProductAndSave();

	        log.info("----拉取notenom数据完成----");


			System.out.println("-------fetch end---------");

		}
}