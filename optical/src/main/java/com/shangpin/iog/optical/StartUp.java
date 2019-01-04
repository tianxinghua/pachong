package com.shangpin.iog.optical;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.optical.service.FetchProduct;

public class StartUp {
	private static Logger log = Logger.getLogger("info");
    private static ApplicationContext factory;
	private static void loadSpringContext()
	{
        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args)
	{
		try {
			//加载spring
	        log.info("----拉取optical数据开始----");
			loadSpringContext();
	        log.info("----初始SPRING成功----");
	        System.out.println("----初始SPRING成功----"); 
	        //拉取数据
	        FetchProduct fetchProduct =(FetchProduct)factory.getBean("optical");
	        fetchProduct.fetchProductAndSave();
	        log.info("----拉取optical数据完成----");
			System.out.println("-------fetch end---------");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			System.exit(0); 
		}
        

	}
}
