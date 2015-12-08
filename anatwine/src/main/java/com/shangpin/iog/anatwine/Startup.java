package com.shangpin.iog.anatwine;

import com.shangpin.iog.anatwine.utils.ApennineHttpUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.apache.commons.httpclient.NameValuePair;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;

import java.text.ParseException;
import java.util.List;

public class Startup {
	private static ApplicationContext factory;
	private static void loadSpringContext()
	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args)
	{

        //加载spring

		loadSpringContext();
        //拉取数据
        ApennineHttpUtil apennineService =(ApennineHttpUtil)factory.getBean("apennine");
		System.out.println("-------apennine start---------");
		try {
//			apennineService.insertApennineProducts("http://112.74.74.98:8082/api/GetProductDetails?userName=spin&userPwd=spin112233");
		} catch (Exception e) {
			e.printStackTrace();
		}

			/*String url= ApiUrl.STOCK;*/
			/*List<ApennineProductDTO>list=apennineService. getProductsDetailsByUrl("http://112.74.74.98:8082/api/GetProductDetails?userName=spin&userPwd=spin112233");
			ApennineProductDTO dto = list.get(0);*/
		/*int stock=-1;
		try {
			stock = apennineService.getHkstockByScode(url,"SAILIVORYF");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		/*System.out.println("这件商品的scode是"+dto.getScode());*/
		System.out.println("成功插入数据库");
        System.out.println("-------apennine end---------");

	}
}
