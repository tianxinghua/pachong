package com.shangpin.iog.prestashop;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.prestashop.service.FetchProduct;

/**
 * 
 * @author lubaijiang 2016/07/29
 *
 */
public class StartUp {
	private static Logger log = Logger.getLogger("info");
	private static ApplicationContext factory;

	private static void loadSpringContext()

	{

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) {
		log.info("----拉取Atelier-template数据开始----");
		loadSpringContext();
		
		// 拉取数据
		FetchProduct fetchProduct = (FetchProduct) factory
				.getBean("prestashop-template");
		fetchProduct.sendAndSaveProduct();
		log.info("----拉取Atelier-template数据完成----");
		System.out.println("-------fetch end---------");

	}
}
