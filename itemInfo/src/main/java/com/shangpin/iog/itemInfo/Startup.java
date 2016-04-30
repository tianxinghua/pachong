package com.shangpin.iog.itemInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;








import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.itemInfo.service.FetchProduct;
import com.shangpin.iog.itemInfo.service.Test;

public class Startup {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl=null;
//	private static String biaoshi = "0";

	private static ApplicationContext factory;

//	private static String localPath = "";
	
	private static String supplierId = "";
	private static int day;
	private static String picpath = null;;

	static {
		if(null==bdl)
			bdl=ResourceBundle.getBundle("conf");
//		biaoshi = bdl.getString("biaoshi");
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
		logger.info("============初始化Spring成功===============");

//		try {
//			localPath = URLDecoder.decode((Startup.class.getClassLoader().getResource("").getFile()), "utf-8");
//			localPath = localPath+"testBuffer.xml";
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		logger.info("localPath============"+localPath);
//		logger.info("biaoshi==============="+biaoshi);
//		//拉取数据
//		//从网站拉取数据，保存到文件
//		if("0".equals(biaoshi)){
//			Test t= new Test();
//			t.getResAsStream(
//					"http://service.alducadaosta.com/EcSrv/Services.asmx?op=GetSku4Platform",
//					"http://service.alducadaosta.com/EcSrv/GetSku4Platform",
//					"text/xml; charset=UTF-8",
//					localPath);
//		}
//		t.getResAsStream(
//				"http://service.alducadaosta.com/EcSrv/Services.asmx?op=GetItem4Platform",
//				"http://service.alducadaosta.com/EcSrv/GetItem4Platform",
//				"text/xml; charset=UTF-8",
//				localPath);
		//获取文件内容，解析xml，并且将信息保存入库
//		System.out.println(localPath);
		FetchProduct fetchProduct =(FetchProduct)factory.getBean("itemInfo");
		fetchProduct.handleData("sku", supplierId, day, picpath);

		logger.info("-------fetch end---------");
		System.out.println("-------fetch end---------");

	}

}
