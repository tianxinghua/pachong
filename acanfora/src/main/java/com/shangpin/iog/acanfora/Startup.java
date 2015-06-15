package com.shangpin.iog.acanfora;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.acanfora.service.FetchProduct;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dto.ColorContrastDTO;
import com.shangpin.iog.product.service.ColorContrastFindServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class Startup
{

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
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("acanfora");
        fetchProduct.fetchProductAndSave("http://www.acanfora.it/api_ecommerce_v2.aspx");

//		List<ColorContrastDTO> list = new ArrayList<>();
//		ColorContrastFindServiceImpl colorContrastFindService = (ColorContrastFindServiceImpl)factory.getBean("ColorContrastFindService");
//		try {
//			list = colorContrastFindService.findAll();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//		System.out.println(list.size());


		System.out.println("-------fetch end---------");

	}

}
