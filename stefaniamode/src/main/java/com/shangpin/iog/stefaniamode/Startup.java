package com.shangpin.iog.stefaniamode;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.stefaniamode.service.StefanFrameFetchProduct;

public class Startup {
	private static Logger log = Logger.getLogger("info");

	private static ApplicationContext factory;

	private static ResourceBundle bdl = null;

	public static String supplierId;

	public static String zipUrl;

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) {

		// 加载spring
		log.info("----拉取stefaniamode数据开始----");
		loadSpringContext();
		log.info("----初始SPRING成功----");
		// 拉取数据
		StefanFrameFetchProduct fetchProduct = (StefanFrameFetchProduct) factory.getBean("framestefaniamode");
		fetchProduct.sendAndSaveProduct();

		log.info("----拉取stefaniamode数据完成----");

		System.out.println("-------fetch end---------");

	}

}
