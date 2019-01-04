package com.shangpin.iog.studio69;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.studio69.service.FetchProduct;

public class Startup
{
    private static Logger log = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args){
		try {
			//加载spring
	        log.info("----拉取studio69数据开始----");
			loadSpringContext();
	        log.info("----初始SPRING成功----");
	        //拉取数据
	        FetchProduct fetchProduct =(FetchProduct)factory.getBean("studio69");
	        try {
	            fetchProduct.fetchProductAndSave();
	        } catch (Exception e) {
	        	e.printStackTrace();
	           loggerError.error("拉取失败。" + e.getMessage());
	        }
	        log.info("----拉取studio69数据完成----");
			System.out.println("-------fetch end---------");
		} catch (Exception e) {
			// TODO: handle exception
		}
        System.exit(0);
	}

}
