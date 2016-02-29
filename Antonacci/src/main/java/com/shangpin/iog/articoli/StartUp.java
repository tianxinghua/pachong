package com.shangpin.iog.articoli;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.articoli.dto.Other_images;

public class StartUp {

private static Logger logInfo  = Logger.getLogger("info");
	
	private static ApplicationContext factory;

	private static void loadSpringContext()

	{

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) {
		//加载spring
		loadSpringContext();
		System.out.println("初始化Spring成功");
		logInfo.info("-----------------articoli fech start---------------------");
		FechProduct fech = (FechProduct) factory.getBean("articoli");
		fech.fechAndSave();
		System.out.println("-------------Fech over-----------------");
		logInfo.info("--------------Fech over-----------------");
		
//		String ss = "<other_images><image1></image1><image2></image2></other_images>";
//		Other_images i = XMLUtil.gsonXml2Obj(Other_images.class, ss);
//		System.out.println(i);
		
		
	}
}
