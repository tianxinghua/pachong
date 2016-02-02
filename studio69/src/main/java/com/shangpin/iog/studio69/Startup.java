package com.shangpin.iog.studio69;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dto.ColorContrastDTO;
import com.shangpin.iog.product.service.ColorContrastFindServiceImpl;
import com.shangpin.iog.studio69.service.FetchProduct;

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
    private static Logger loggerError = Logger.getLogger("error");
    private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args)
	{

        //加载spring
        log.info("----拉取studio69数据开始----");
		loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("studio69");
        try {
            fetchProduct.fetchProductAndSave();
        } catch (Exception e) {
           loggerError.error("拉取失败。" + e.getMessage());
        }

        log.info("----拉取studio69数据完成----");


		System.out.println("-------fetch end---------");
        System.exit(0);
	}

}
