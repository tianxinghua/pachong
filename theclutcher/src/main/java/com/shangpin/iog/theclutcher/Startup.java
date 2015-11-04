package com.shangpin.iog.theclutcher;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.theclutcher.service.FetchProduct;

public class Startup {

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
		System.out.println("初始化Spring成功");
		
        //1.拉取数据
		//2.获取文件内容，解析xml，并且将信息保存入库
		FetchProduct fetchProduct = (FetchProduct)factory.getBean("theclutcher");
		fetchProduct.fetchProductAndSave("https://www.theclutcher.com/en-US/home/feedShangpin",
										"feedShangpin.zip");
		System.out.println("-------fetch end---------");

	}

}
