package com.shangpin.iog.deliberti;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.deliberti.service.FetchProduct;
/**
 * 
 * @author dongjinghui
 *
 */
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
	        log.info("----拉取deliberti数据开始----");
			loadSpringContext();
	        log.info("----初始Spring成功----");
	        //拉取数据
	        FetchProduct fetchProduct =(FetchProduct)factory.getBean("deliberti");
	        fetchProduct.fetchProductAndSave();

	        log.info("----拉取deliberti数据完成----");


			System.out.println("-------fetch end---------");

		}
}
