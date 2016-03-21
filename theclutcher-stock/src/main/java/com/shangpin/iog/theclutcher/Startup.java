package com.shangpin.iog.theclutcher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.theclutcher.stock.service.GrabStockImp;

public class Startup {

	private static Logger logger = Logger.getLogger("info");
//	private static Logger loggerError = Logger.getLogger("error");
	private  static  ResourceBundle bundle = ResourceBundle.getBundle("sop");
	
	private static ApplicationContext factory;
	
    public static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args) throws Exception {
		
		loadSpringContext();
		String host = bundle.getString("HOST");
        String app_key = bundle.getString("APP_KEY");
        String app_secret= bundle.getString("APP_SECRET");
        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)||StringUtils.isBlank(app_secret)){
            logger.error("参数错误，无法执行更新库存");
        }

        GrabStockImp theclutcher = (GrabStockImp)factory.getBean("theclutcherStock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("theclutcher-stock更新数据库开始");
		try{
			
			theclutcher.updateProductStock(host,app_key,app_secret,"2015-01-01 00:00",format.format(new Date()));
			
		}catch(Exception ex){
			logger.error(ex);
			ex.printStackTrace();
		}
		
		logger.info("theclutcher-stock更新数据库结束");
		System.exit(0);
	}

	
}
