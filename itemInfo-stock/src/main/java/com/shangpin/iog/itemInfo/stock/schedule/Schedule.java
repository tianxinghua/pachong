package com.shangpin.iog.itemInfo.stock.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.itemInfo.stock.service.GrabStockImp;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {

	private static Logger logger = Logger.getLogger("info");
	private static LoggerUtil logError = LoggerUtil.getLogger("error");
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    
    private static int status=0;
    
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
	
	@Autowired
	GrabStockImp stockImp;
	
	
	@Scheduled(cron="${jobsSchedule}")
	public void start(){
		if(status == 0){
			status = 1;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        logger.info("alduca daosta更新数据库开始");
	        try{
	        	stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
	        }catch(Exception ex){
	        	logError.error(ex);
	        	ex.printStackTrace();
	        }
	        logger.info("alduca daosta更新数据库结束");
	        status = 0;
		}
        
	}
	
	
}
