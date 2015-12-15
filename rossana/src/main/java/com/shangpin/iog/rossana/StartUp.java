package com.shangpin.iog.rossana;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.rossana.dto.Item;

public class StartUp {

	private static Logger logInfo  = Logger.getLogger("info");
	
	private static ApplicationContext factory;

	private static void loadSpringContext()

	{

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	public  static void main(String[] args){

		//加载spring
		loadSpringContext();
		System.out.println("初始化Spring成功");
		logInfo.info("数据拉取开始---------------------");
		PullDown pull = (PullDown)factory.getBean("PullDown");
		pull.fechProduct();
		System.out.println("------------拉取完成 Fech over-----------------");
		logInfo.info("------------拉取完成 Fech over-----------------");
	}
}
