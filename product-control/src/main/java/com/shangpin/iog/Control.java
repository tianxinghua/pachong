package com.shangpin.iog;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SupplierMapper;

@Component("product-control")
public class Control {
	
	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
	
	private static ResourceBundle bundle = null;
	private static int MAX_TIMEVALUE = 24;
	
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String messageType = null;
	
	static{
		if(bundle == null){
			bundle = ResourceBundle.getBundle("conf");
		}	
		if(StringUtils.isNotBlank(bundle.getString("MAX_TIMEVALUE"))){
			MAX_TIMEVALUE = Integer.parseInt(bundle.getString("MAX_TIMEVALUE"));
		}
		
		fromUserPassword = bundle.getString("fromUserPassword");
		from = bundle.getString("from");
		to = bundle.getString("to");
		messageType = bundle.getString("messageType");
		smtpHost = bundle.getString("smtpHost");
	}

	@Autowired
	SkuMapper skuDAO;
	@Autowired
    SupplierMapper supplierDAO;
	
	public void doJob(){
		try {
		
			StringBuffer buffer = new StringBuffer();
			StringBuffer excBuffer = new StringBuffer();
			List<SupplierDTO> suppliers = supplierDAO.findByState("1");
			for(SupplierDTO supplier : suppliers){	
				try {
					Date maxDate = skuDAO.findMaxTimeBySupplier(supplier.getSupplierId());
					loggerInfo.info(supplier.getSupplierName() + " 上次更新时间==="+DateTimeUtil.convertFormat(maxDate, "yyyy-MM-dd HH:mm:ss"));
					long diffTime = new Date().getTime() - maxDate.getTime();
					int hour = (int)((diffTime/1000)/60)/60;
					if(hour >= MAX_TIMEVALUE){
						buffer.append("供应商 "+supplier.getSupplierName()+" 编号 "+supplier.getSupplierId()+" 已经超过"+hour+"小时没有拉取产品了，请检查原因；").append("<br>");
					}
				} catch (Exception e) {
					e.printStackTrace();
					loggerInfo.info(supplier.getSupplierName()+" "+e.toString()); 
					excBuffer.append(supplier.getSupplierName()+"监测失败，可能的原因是update time全为空；"+e.getMessage()).append("<br>");
				}
				
			}
			
			String messageText = buffer.append(excBuffer).toString(); 
			if(StringUtils.isNotBlank(messageText)){
				String subject = "香港拉取产品预警";
				SendMail.sendGroupMail(smtpHost, from, fromUserPassword, to, subject, messageText, messageType);
			}else{
				loggerInfo.info("================所有产品拉取完满结束=====================");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			loggerInfo.info(e.toString()); 
		}
		
	}
	
	private static ApplicationContext factory;
	private static void loadSpringContext(){
        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	public static void main(String[] args) {
		loadSpringContext();
		loggerInfo.info("初始化成功，检测开始");
		Control control = (Control)factory.getBean("product-control");
		control.doJob();
		loggerInfo.info("-----------------结束-----------------");
		System.exit(0); 
	}
}
