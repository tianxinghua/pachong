package com.shangpin.iog.della;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.della.service.FetchProduct;

public class StartUp {
	
		private static Logger logInfo  = Logger.getLogger("info");
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

		public static void main(String[] args) {
			//加载spring
			loadSpringContext();
			System.out.println("初始化Spring成功");
			logInfo.info("数据拉取开始---------------------");
			FetchProduct fech = (FetchProduct) factory.getBean("della");
			fech.handleData("sku", supplierId, day, picpath);
			System.out.println("------------拉取完成 Fech over-----------------");
			logInfo.info("------------拉取完成 Fech over-----------------");
		}
	
}
