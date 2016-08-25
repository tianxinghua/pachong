package com.shangpin.iog.picture;

import java.util.ResourceBundle;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.picture.service.FtpDownPicService;
import com.shangpin.iog.picture.service.PictureDownService;

import org.apache.commons.lang.StringUtils;
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
		
		if(args.length==0 || StringUtils.isBlank(args[0])){
			System.out.println("请传入一个参数conf或者ftp");
			loggerInfo.info("请传入一个参数conf或者ftp"); 
			return;
		}
		loadSpringContext();
		loggerInfo.info("初始化成功，开始同步");
		if("conf".equals(args[0])){
			PictureDownService pictureDownService = (PictureDownService)factory.getBean("pictureDownService");
			pictureDownService.downPic();
			HttpUtil45.closePool();
		}else if("ftp".equals(args[0])){
			FtpDownPicService ftpDownPicService = (FtpDownPicService)factory.getBean("ftpDownPicService");
			ftpDownPicService.downPic(); 
		}
					
	}
}
