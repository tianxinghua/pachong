package com.shangpin.iog.webcontainer.front.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.service.ProductSearchService;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {	
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
	}

	@Autowired
    ProductSearchService productService;
	
	@Scheduled(cron="${sendmailSchedule}")
	public void sendMailDiffProduct(){
		if(StringUtils.isBlank(supplierId)){
			supplierId = "-1";
		}
		Date endDate = new Date();
		Calendar calendar = Calendar.getInstance();		
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
		Date startDate = calendar.getTime();
		
		try {
			StringBuffer buffer = productService.exportDiffProduct(supplierId,startDate,endDate,null,null,"diff");
			if(buffer != null){
				System.out.println(buffer.toString());
			}
			System.out.println(startDate+"---------------"+endDate);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
