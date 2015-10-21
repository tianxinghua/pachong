package com.shangpin.iog.railso;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.railso.dao.Rss;
import com.shangpin.iog.theclutcher.utils.DownloadFileFromNet;
import com.shangpin.iog.theclutcher.utils.XMLUtil;

public class Startup {

	private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args){
		try {
			//加载spring
			loadSpringContext();
			System.out.println("初始化Spring成功");
			//抓取信息并存库
			FetchProduct fet = (FetchProduct) factory.getBean("railso");
			fet.fetchProductAndSave("E:\\下载\\railso-com.xml.html");
			System.out.println("-------fetch end---------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
