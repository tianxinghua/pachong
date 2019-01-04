package com.shangpin.iog.webcontainer.front.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.service.ProductSearchService;

@Component
@PropertySource("classpath:conf.properties")
public class Schedule {	
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static Logger log = LoggerFactory.getLogger(Schedule.class);
	private static String filepath = null;
	
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String subject = null;
	private static String messageType = null;
	
	private static String dailyUpdateProductFilePath=null;
	private static String dailyUpdateProductTo=null;
	private static String dailyUpdateProductSubject=null;
	private static String day_of_month = null;
	
	private static String supplierfilePath = null;
	private static String splitSign=null;
	private static String goodproduct_Schedule = null;
	private static String goodproduct_FilePath = null;
	private static String goodproduct_To = null;
	private static String goodproduct_Subject = null;
	private static String supplierId_diffSeason = null;
	private static String diffSeasonFilepath = null;
	
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		
		smtpHost  = bdl.getString("smtpHost");
		from = bdl.getString("from");
		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		subject = bdl.getString("subject");
		messageType = bdl.getString("messageType");
		
		filepath = bdl.getString("filepath");
		
		dailyUpdateProductFilePath=bdl.getString("dailyUpdateProductFilePath");
		dailyUpdateProductTo = bdl.getString("dailyUpdateProductTo");
		dailyUpdateProductSubject = bdl.getString("dailyUpdateProductSubject");
		day_of_month = bdl.getString("day_of_month");
		
		supplierfilePath = bdl.getString("supplierfilePath");
		splitSign = bdl.getString("splitSign");
		goodproduct_Schedule = bdl.getString("goodproduct_Schedule");
		goodproduct_FilePath = bdl.getString("goodproduct_FilePath");
		goodproduct_To = bdl.getString("goodproduct_To");
		goodproduct_Subject = bdl.getString("goodproduct_Subject");
		
		supplierId_diffSeason = bdl.getString("supplierId_diffSeason");
		diffSeasonFilepath = bdl.getString("diffSeasonFilepath");
		
	}

	@Autowired
    ProductSearchService productService;
	
	/**
	 * 每天发送 价格发生变化了 的产品列表
	 */
	@Scheduled(cron="${sendmailSchedule}")
	public void sendMailDiffProduct(){
		
		Date endDate = new Date();
//		Calendar calendar = Calendar.getInstance();		
//		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - Integer.parseInt(hour_of_day)); 
//		Date startDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		Date startDate = null;
		try {
			startDate = sdf.parse("2015-01-01 00:00:00");
		} catch (ParseException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage()); 
		}
		
		try {
			if(StringUtils.isNotBlank(to)){
				
				StringBuffer buffer = productService.getDiffProduct(startDate,endDate,null,null,"diff");
				String messageText  = buffer.toString();
				if(StringUtils.isNotBlank(messageText)){ 
					try {
						BufferedInputStream in = null;
						File file = null;
						FileOutputStream out = null;
						try{
							in = new BufferedInputStream(new ByteArrayInputStream(messageText.getBytes("gb2312")));
							file = new File(filepath);
							out = new FileOutputStream(file);
							byte[] data = new byte[1024];
				            int len = 0;
				            while (-1 != (len=in.read(data, 0, data.length))) {
				                out.write(data, 0, len);
				            }
						}catch(Exception e){
							e.printStackTrace();
						}finally {
				            if (in != null) {
				                in.close();
				            }
				            if (out != null) {
				                out.close();
				            }
				        }
						if(null != file){
							SendMail.sendGroupMailWithFile(smtpHost, from, fromUserPassword, to, subject,"请查看附件", messageType,file);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						log.error(e.getMessage());
					}
				}
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage()); 
			e.printStackTrace();
		}
	}
	
	@Scheduled(cron="${dailyUpdateProductSchedule}")
	public void sendMailDailyUpdateProducts(){
		Date endDate = new Date();		
		try{
			if(StringUtils.isNotBlank(dailyUpdateProductTo)){
				StringBuffer buffer = productService.dailyUpdatedProduct("", Integer.parseInt(day_of_month), endDate, null, null, "same");
				String messageText  = buffer.toString();
				if(StringUtils.isNotBlank(messageText)){ 
					try {
						BufferedInputStream in = null;
						File file = null;
						FileOutputStream out = null;
						try{
							in = new BufferedInputStream(new ByteArrayInputStream(messageText.getBytes("gb2312")));
							file = new File(dailyUpdateProductFilePath);
							out = new FileOutputStream(file);
							byte[] data = new byte[1024];
				            int len = 0;
				            while (-1 != (len=in.read(data, 0, data.length))) {
				                out.write(data, 0, len);
				            }
						}catch(Exception e){
							e.printStackTrace();
						}finally {
				            if (in != null) {
				                in.close();
				            }
				            if (out != null) {
				                out.close();
				            }
				        }
						if(null != file){
							SendMail.sendGroupMailWithFile(smtpHost, from, fromUserPassword, dailyUpdateProductTo, dailyUpdateProductSubject,"上新产品统计信息请查看附件", messageType,file);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						log.error(e.getMessage());
					}
				}
				
			}			
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 发送每天拉取下来的所有信息完好的产品列表
	 */
	@Scheduled(cron="${goodproduct_Schedule}")
	public void sendDailyGoodProducts(){
		
		if(StringUtils.isNotBlank(goodproduct_To)){
		
			Date now = new Date();
			Calendar calendar = Calendar.getInstance();	
			calendar.setTime(now);
		    calendar.set(Calendar.HOUR_OF_DAY, 0);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)); 
			Date startDate = calendar.getTime();
			
			BufferedReader reader = null;
			try{
				String suppliers = "";
				File supplierFile = new File(supplierfilePath);
				reader = new BufferedReader(new FileReader(supplierFile));
				String temp = null;
				while(null != (temp = reader.readLine())){
					suppliers += temp;
				}
				if(StringUtils.isNotBlank(suppliers)){
					String[] sus = suppliers.split(splitSign);
					if(sus.length>0){
						StringBuffer buffer = productService.dailyGoodProducts(sus, startDate, now, null, null);
						String messageText  = buffer.toString();
						if(StringUtils.isNotBlank(messageText)){ 
							try {
								BufferedInputStream in = null;
								File file = null;
								FileOutputStream out = null;
								try{
									in = new BufferedInputStream(new ByteArrayInputStream(messageText.getBytes("gb2312")));
									file = new File(goodproduct_FilePath);
									out = new FileOutputStream(file);
									byte[] data = new byte[1024];
						            int len = 0;
						            while (-1 != (len=in.read(data, 0, data.length))) {
						                out.write(data, 0, len);
						            }
								}catch(Exception e){
									e.printStackTrace();
								}finally {
						            if (in != null) {
						                in.close();
						            }
						            if (out != null) {
						                out.close();
						            }
						        }
								if(null != file){
									SendMail.sendGroupMailWithFile(smtpHost, from, fromUserPassword, goodproduct_To, goodproduct_Subject,"今日拉取的新品请查看附件", messageType,file);
								}
								
							} catch (Exception e) {
								e.printStackTrace();
								log.error(e.getMessage());
							}
						}
					}
				}			
				
			}catch(Exception ex){
				ex.printStackTrace();
			}finally{
				try{
					reader.close();
				}catch(Exception e){
					e.printStackTrace();
				}			
			}
		}	
	}
	
	//季节码发生变化发送邮件
	@Scheduled(cron="${sendMaildiffSeason_Schedule}")
	public void sendMaildiffSeason(){
//		if(StringUtils.isBlank(supplierId_diffSeason)){
//			return;
//		}
		Date endDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		Date startDate = null;
		try {
			startDate = sdf.parse("2015-01-01 00:00:00");
		} catch (ParseException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage()); 
		}
		
		try {
			if(StringUtils.isNotBlank(to)){
				
				StringBuffer buffer = productService.getDiffSeasonProducts(null,startDate,endDate,null,null);
				if(null != buffer){
					String messageText  = buffer.toString();
					if(StringUtils.isNotBlank(messageText)){ 
						try {
							BufferedInputStream in = null;
							File file = null;
							FileOutputStream out = null;
							try{
								in = new BufferedInputStream(new ByteArrayInputStream(messageText.getBytes("gb2312")));
								file = new File(diffSeasonFilepath);
								out = new FileOutputStream(file);
								byte[] data = new byte[1024];
					            int len = 0;
					            while (-1 != (len=in.read(data, 0, data.length))) {
					                out.write(data, 0, len);
					            }
							}catch(Exception e){
								e.printStackTrace();
							}finally {
					            if (in != null) {
					                in.close();
					            }
					            if (out != null) {
					                out.close();
					            }
					        }
							if(null != file){
								SendMail.sendGroupMailWithFile(smtpHost, from, fromUserPassword, to, "以下产品季节发生了变化，请查看","以下产品季节发生了变化,请查看附件", messageType,file);
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							log.error(e.getMessage());
						}
					}
				}			
			}
			
		} catch (Exception e) {
			log.error(""+e); 
			e.printStackTrace();
		}
	}
	
}
