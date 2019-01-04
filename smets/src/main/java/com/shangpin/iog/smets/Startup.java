package com.shangpin.iog.smets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.smets.service.SearchAndSend;

public class Startup {
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) {
    	try {
    		loadSpringContext();
    		SearchAndSend searchAndSend = (SearchAndSend) factory.getBean("searchAndSend");
    		searchAndSend.fetchProductAndSave();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			System.exit(0); 
		}
        
        
    }
}
