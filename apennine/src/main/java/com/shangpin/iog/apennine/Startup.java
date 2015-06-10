package com.shangpin.iog.apennine;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.apennine.utils.ApennineHttpUtil;
import com.shangpin.iog.app.AppContext;

public class Startup {
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
		System.out.print("ooooo");
        ApennineHttpUtil apennineService =(ApennineHttpUtil)factory.getBean("apennine");
        try {
        	 System.out.println("-------apennine start---------");
			apennineService.insertApennineProducts("http://112.74.74.98:8082/api/GetProductDetails?userName=spin&userPwd=spin112233");
		} catch (ServiceException e) {
			e.printStackTrace();
		}

        System.out.println("-------apennine end---------");

	}
}
