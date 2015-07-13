package com.shangpin.iog.galiano;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.galiano.service.FetchProduct;
import com.shangpin.iog.app.AppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

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
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("galiano");
        fetchProduct.fetchProductAndSave("http://www.galianostore.com/shangpin.xml");




		System.out.println("-------fetch end---------");

	}

}
