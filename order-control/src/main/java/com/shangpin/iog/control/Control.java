package com.shangpin.iog.control;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.product.dao.OrderDetailMapper;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SupplierMapper;

@Component("product-control")
public class Control {
	
private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
	
	private static ResourceBundle bundle = null;
	private static long MAX_TIMEVALUE = 3600000;
	
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String messageType = null;
	
	private static Map<String,String> statusMap = new HashMap<String,String>(){
		{
			put("nohandle","超时不处理");
			put("refunded","退款成功");
			put("SHpurExp","应该采购异常");
		}
	};
	
	static{
		if(bundle == null){
			bundle = ResourceBundle.getBundle("conf");
		}	
		if(StringUtils.isNotBlank(bundle.getString("MAX_TIMEVALUE"))){
			MAX_TIMEVALUE = Long.parseLong(bundle.getString("MAX_TIMEVALUE"));
		}
		
		fromUserPassword = bundle.getString("fromUserPassword");
		from = bundle.getString("from");
		to = bundle.getString("to");
		messageType = bundle.getString("messageType");
		smtpHost = bundle.getString("smtpHost");
	}

	@Autowired
	OrderDetailMapper orderDAO;
	@Autowired
    SupplierMapper supplierDAO;

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
	
	public void doJob(){
		try {
			
			StringBuffer buffer = new StringBuffer();
			StringBuffer excBuffer = new StringBuffer();
			List<SupplierDTO> suppliers = supplierDAO.findAll();
			Date endDate = new Date();
			Date startDate =  new Date(endDate.getTime()-MAX_TIMEVALUE);
			for(SupplierDTO supplier : suppliers){	
				try {	
					loggerInfo.info(supplier.getSupplierName()+"开始----------------");
					List<OrderDetailDTO> orders = orderDAO.findPushFailOrdersByUpdateTime(supplier.getSupplierId(), startDate, endDate);
					if(null != orders && orders.size()>0){
						for(OrderDetailDTO detail : orders){
							buffer.append("供应商 "+supplier.getSupplierName()+" 编号 "+supplier.getSupplierId()+" "+detail.getSpPurchaseNo()+"状态："+statusMap.get(detail.getStatus())).append("<br>");
						}						
					}
					loggerInfo.info(supplier.getSupplierName()+"结束----------------");
				} catch (Exception e) {
					e.printStackTrace();
					loggerInfo.info(supplier.getSupplierName()+" "+e.toString()); 
				}
				
			}
			
			String messageText = buffer.append(excBuffer).toString(); 
			if(StringUtils.isNotBlank(messageText)){
				String subject = DateTimeUtil.convertFormat(startDate, "yyyy-MM-dd HH:mm:ss")+"至"+DateTimeUtil.convertFormat(endDate, "yyyy-MM-dd HH:mm:ss")+"推送失败的采购单";
				SendMail.sendGroupMail(smtpHost, from, fromUserPassword, to, subject, messageText, messageType);
			}else{
				loggerInfo.info("================检测结束=====================");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			loggerInfo.info(e.toString()); 
		}
	}
	
	
}
