package com.shangpin.iog.galiano;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.galiano.service.FetchProduct;
import com.shangpin.iog.app.AppContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class Startup
{
	private static Logger log = Logger.getLogger("info");
	private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args)
	{

        //加载spring

		loadSpringContext();
		log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("galiano");
		log.info("----拉取galiano数据开始----");
        fetchProduct.fetchProductAndSave("http://www.galianostore.com/shangpin.xml");


		log.info("----拉取galiano数据结束----");

		System.out.println("-------fetch end---------");

	}

}
