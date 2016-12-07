package com.shangpin.iog.tony;
import java.util.ResourceBundle;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.tony.service.FetchProduct;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StartSkuJob {

	private static Logger logInfo  = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String picpath = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		picpath = bdl.getString("picpath");
	}
	
	private static ApplicationContext factory;

	private static void loadSpringContext()

	{

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) {
		//加载spring
		loadSpringContext();
		System.out.println("-------------start Spring successful------------");
		logInfo.info("-------------start Spring successful------------");
		FetchProduct fech = (FetchProduct) factory.getBean("tony");
		fech.handleData("sku", supplierId, day, picpath);
		System.out.println("------------ Fech over-----------------");
		logInfo.info("------------ Fech over-----------------");
	}

}
