package com.shangpin.iog.theclutcher;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.theclutcher.service.FetchProduct;

public class Startup {

	private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    
    private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String picpath = "";
    private static int day;
    
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        day = Integer.valueOf(bdl.getString("day"));
        picpath = bdl.getString("picpath");
        
    }
    
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
//		fetchProduct.fetchProductAndSave("https://www.theclutcher.com/en-US/home/feedShangpin",
//										"feedShangpin.zip");
		fetchProduct.handleData("sku", supplierId, day, picpath);
		System.out.println("-------fetch end---------");

	}

}
