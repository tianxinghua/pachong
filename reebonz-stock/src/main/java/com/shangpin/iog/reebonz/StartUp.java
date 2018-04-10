package com.shangpin.iog.reebonz;

import com.shangpin.iog.redi.schedule.AppContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ResourceBundle;

public class StartUp {
	private static Logger log = Logger.getLogger("info");
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String picpath = "";
    private static int day;
    
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        day = Integer.valueOf(bdl.getString("day"));
        
    }
    private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args)
	{

        //加载spring
		loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据

		System.out.println("-------fetch end---------");

	}
}
