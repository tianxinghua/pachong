package com.shangpin.iog.EMonti;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.EMonti.service.FetchProduct;
import com.shangpin.iog.app.AppContext;

import java.util.ResourceBundle;

public class StartUp {
	 private static Logger log = Logger.getLogger("info");
	private static ResourceBundle bdl=null;
	private static int day;
	private static String supplierId = "";
	private static String picpath = "";
	static {
		if(null==bdl)
			bdl=ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		picpath = bdl.getString("picpath");
	}

	    private static ApplicationContext factory;
		private static void loadSpringContext()

		{

	        factory = new AnnotationConfigApplicationContext(AppContext.class);
		}
		
		public static void main(String[] args)
		{

	        //加载spring
	        log.info("----拉取E-Monti数据开始----");
			loadSpringContext();
	        log.info("----初始SPRING成功----");
	        //拉取数据
	        FetchProduct fetchProduct =(FetchProduct)factory.getBean("E-Monti");
			fetchProduct.handleData("spu", supplierId, day, picpath);

	        log.info("----拉取E-Monti数据完成----");


			System.out.println("-------fetch end---------");

		}
}
