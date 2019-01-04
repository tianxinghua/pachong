package com.shangpin.iog.dellogliostore;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dellogliostore.service.FetchProduct;
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
	
	public static void main(String[] args){
		try {
			log.info("----拉取dellogliostore数据开始----");
			loadSpringContext();
	        log.info("----初始SPRING成功----");
	        FetchProduct fetchProduct =(FetchProduct)factory.getBean("dellogliostore");
	        fetchProduct.fetchProductAndSave();
	        log.info("----拉取dellogliostore数据完成----");
	        System.out.println("-------fetch end---------");
		} catch (Exception e) {
			log.error(e.getMessage(),e); 
		}
		System.exit(0);
        
	}

}
