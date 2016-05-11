package com.shangpin.iog.levelgroup;

import java.util.ResourceBundle;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.levelgroup.service.FetchProduct;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Created by sunny on 2015/8/17.
 */
public class Startup {


    private static Logger log = Logger.getLogger("info");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    
    private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String picpath = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");		
		day = Integer.valueOf(bdl.getString("day"));
		picpath = bdl.getString("picpath");
	}
	

    public static void main(String[] args)
    {
        //加载spring
        log.info("----拉取levelgroup数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("levelgroup");
        fetchProduct.handleData("sku", supplierId, day, picpath); 
        log.info("----拉取levelgroup数据完成----");


        System.out.println("-------fetch end---------");
    }
}
