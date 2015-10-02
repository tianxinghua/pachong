package com.shangpin.iog.acanfora;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.acanfora.service.FetchProduct;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dto.ColorContrastDTO;
import com.shangpin.iog.product.service.ColorContrastFindServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class Startup
{
    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args)
	{

        //加载spring
        log.info("----拉取acanfora数据开始----");
		loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("acanfora");
//        fetchProduct.fetchProductAndSave("http://www.acanfora.it/api_ecommerce_v2.aspx");
        fetchProduct.fetchProductAndSave("http://127.0.0.1:8081/api_ecommerce_v2.xml");

        log.info("----拉取acanfora数据完成----");


		System.out.println("-------fetch end---------");

	}

}
