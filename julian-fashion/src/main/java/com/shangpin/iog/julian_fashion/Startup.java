package com.shangpin.iog.julian_fashion;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.julian_fashion.service.FetchProduct;

public class Startup {
	private static Logger log = Logger.getLogger("info");

	private static ApplicationContext factory;

	private static ResourceBundle bdl = null;

	public static String supplierId;

	private static String[] brandArray = { "Alexander Mcqueen".toUpperCase(), "BOTTEGA VENETA".toUpperCase(),
			"CHARLOTTE OLYMPIA".toUpperCase(), "DOLCE & GABBANA".toUpperCase(), "GIVENCHY".toUpperCase(), "KENZO".toUpperCase(),
			"MICHAEL KORS".toUpperCase(), "MIU MIU".toUpperCase(), "PRADA".toUpperCase(), "MOSCHINO".toUpperCase(), "STELLA".toUpperCase(),
			"Lavin".toUpperCase(), "Tory burch".toUpperCase(), "bottega veneta".toUpperCase(), "michael kors".toUpperCase(),
			"miu miu".toUpperCase(), "prada".toUpperCase() , "stella".toUpperCase() };

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) {

		// 加载spring
		log.info("----拉取julian_fashion数据开始----");
		loadSpringContext();
		log.info("----初始SPRING成功----");
		// 拉取数据
		FetchProduct fetchProduct = (FetchProduct) factory.getBean("vinicco");
		if (args.length == 0) {
			fetchProduct.fetchProductAndSave(supplierId, brandArray);
		} else {
			fetchProduct.persist2DB(supplierId, brandArray);
		}

		log.info("----拉取julian_fashion数据完成----");

		System.out.println("-------fetch end---------");
		System.exit(0);
	}

}
