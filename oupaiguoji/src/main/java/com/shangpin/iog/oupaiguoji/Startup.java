package com.shangpin.iog.oupaiguoji;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.oupaiguoji.service.FetchProduct;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Startup
{

	private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args)
	{

        //加载spring

		loadSpringContext();
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("acanfora");
        fetchProduct.fetchProductAndSave("http://www.acanfora.it/api_ecommerce_v2.aspx");

        System.out.println("-------fetch end---------");

	}

}
