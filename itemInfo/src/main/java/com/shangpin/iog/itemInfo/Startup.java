package com.shangpin.iog.itemInfo;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;




import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.itemInfo.service.FetchProduct;
import com.shangpin.iog.itemInfo.service.Test;

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
        //拉取数据
		//从网站拉取数据，保存到文件
		Test t= new Test();
		t.getResAsStream(
				"http://service.alducadaosta.com/EcSrv/Services.asmx?op=GetItem4Platform",
				"http://service.alducadaosta.com/EcSrv/GetItem4Platform",
				"text/xml; charset=UTF-8");
		//获取文件内容，解析xml，并且将信息保存入库
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("itemInfo");
        fetchProduct.fetchProductAndSave("E:\\testBuffer.xml");

		System.out.println("-------fetch end---------");

	}

}
