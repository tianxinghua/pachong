package com.shangpin.iog.antonacci.schedule;

import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.iog.antonacci.GrapStock;
import com.shangpin.iog.common.utils.logger.LoggerUtil;

@Component
@PropertySource("classpath:param.properties")
public class Schedule {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("param");
        supplierId = bdl.getString("supplierId");
    }
	@Autowired
	GrapStock stockImp;
	
	
	@SuppressWarnings("deprecation")
	@Scheduled(cron="${jobsSchedule}")
//	@Scheduled(cron="0 * 0/1 * * ? ")
	public void start(){
		System.out.println(new Date().toLocaleString()+"开始更新");
    	Murder mur = Murder.getMur();
    	mur.setStockImp(stockImp);
    	Thread t = new Thread(mur);
    	t.start();
	}
	
	
}
