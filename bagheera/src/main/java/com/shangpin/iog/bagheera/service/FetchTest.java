package com.shangpin.iog.bagheera.service;

import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.iog.bagheera.utils.AppContext;
import com.shangpin.product.AbsSaveProduct;
@Component("fetchtest")
public class FetchTest extends AbsSaveProduct{
	
	private static ResourceBundle bdl = null;
	private static String aaaaa;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		aaaaa = bdl.getString("aaaaa");
	}
	   private static ApplicationContext factory;
	    private static void loadSpringContext()
	    {
	        factory = new AnnotationConfigApplicationContext(AppContext.class);
	    }
	@Override
	public Map<String, Object> fetchProductAndSave() {
		return null;
	}
	  public static void main(String[] args) throws Exception {
	    	//加载spring
	        loadSpringContext();
	        System.out.println(aaaaa);
	        FetchTest stockImp = (FetchTest)factory.getBean("fetchtest");
	        stockImp.handleData("","",0,"");
	    }
}
