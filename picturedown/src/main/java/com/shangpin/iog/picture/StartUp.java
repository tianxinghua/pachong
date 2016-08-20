package com.shangpin.iog.picture;

import java.util.ResourceBundle;

import com.shangpin.iog.picture.service.PictureDownService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;


public class StartUp {

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
			.getLogger("info");

	private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	public static void main(String[] args) {
		loadSpringContext();
		loggerInfo.info("初始化成功，开始同步");

		PictureDownService pictureDownService = (PictureDownService)factory.getBean("pictureDownService");

		pictureDownService.downPic();
		
		loggerInfo.info("===========下载图片完成========");
	}
}
