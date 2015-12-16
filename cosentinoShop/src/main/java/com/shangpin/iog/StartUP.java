package com.shangpin.iog;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.service.PullSku;
import com.shangpin.iog.utils.XMLUtil;

public class StartUP {

	
private static ApplicationContext factory;
    
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	public static void main(String[] args){
		
		loadSpringContext();
		System.out.println("初始化Spring成功");
		PullSku pull = (PullSku)factory.getBean("PullSku");
		pull.pullAndSave();
		System.out.println("-------cosentinoShop拉取成功-------------");
	}
}
