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
import java.util.ResourceBundle;

public class Startup
{
    private static Logger log = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
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
	
	public static void main(String[] args)
	{

        //加载spring
        log.info("----拉取studio69数据开始----");
		loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("studio69");
        try {
        	fetchProduct.handleData("spu", supplierId, day, picpath);
        } catch (Exception e) {
        	e.printStackTrace();
           loggerError.error("拉取失败。" + e.getMessage());
        }

	}

}
