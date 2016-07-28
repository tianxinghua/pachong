package com.shangpin.iog.stef;

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.service.PictureOfMongoService;

@Component("remove")
public class Start {
	
	private static Logger log = Logger.getLogger("info");
	private static String supplierId = null;
	private static ResourceBundle bdl = null;	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}
	
	@Autowired
	PictureOfMongoService pictureOfMongoService;
	
	public void excute(){
		try {
			for(String su : supplierId.split(",")){
				if(StringUtils.isNotBlank(su)){
					pictureOfMongoService.removePicBySupplierId(supplierId);
				}
			}
			
		} catch (Exception e) {
			log.info(e.getMessage()); 
		}
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
		System.out.println("-------start Spring successful------");	
		log.info("-------开始删除------");
		Start fetchProduct = (Start)factory.getBean("remove");
		fetchProduct.excute();		
		log.info("----------------删除完成-----------------");

	}
}
