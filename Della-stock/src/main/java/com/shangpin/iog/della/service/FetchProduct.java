package com.shangpin.iog.della.service;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.della.dto.Item;
import com.shangpin.iog.della.schedule.AppContext;
import com.shangpin.iog.della.utils.CSVUtil;
import com.shangpin.iog.della.utils.FTPUtils;


@Component("dellaStock")
public class FetchProduct extends AbsUpdateProductStock {
	
//	private static Logger logError = Logger.getLogger("error");
	private static Logger logInfo  = Logger.getLogger("info");
	private static LoggerUtil error = LoggerUtil.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String remoteFileName = "";
	private static String local = "";

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("sop");
		remoteFileName = bdl.getString("remoteFileName");
	    local = bdl.getString("local");

	}
	
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException ,Exception{
		
		Map<String, String> skustock = new HashMap<String, String>();
		Map<String, String> stockMap = new HashMap<String, String>();
		List<Item> items =null; 
		try{
			FTPUtils ftp =new FTPUtils("mosuftp", "inter2015£", "92.223.134.2", 21);
			ftp.downFile("MOSU", remoteFileName, local);
			File file = new File(local+File.separator+remoteFileName);
			items = CSVUtil.readCSV(file, Item.class, ';');
//			ftp.logout();
		}catch(Exception ex){
			error.error("first======"+ex); 
			try{
				System.out.println("============第一次失败，再试一次=============");
				FTPUtils ftp =new FTPUtils("mosuftp", "inter2015£", "92.223.134.2", 21);
				ftp.downFile("MOSU", remoteFileName, local);
				File file = new File(local+File.separator+remoteFileName);
				items = CSVUtil.readCSV(file, Item.class, ';');
//				ftp.logout();
			}catch(Exception e){
				error.error("second======"+e); 
				return skustock;
			}			
		}
		if(null != items && items.size() > 0){
			logInfo.info("获取到的供应商的数据大小是========"+items.size()); 
			for(Item item:items){
				String stock = item.getQuantity();
				stockMap.put(item.getItem_code(), StringUtils.isNotBlank(stock) ? stock : "0");
			}
			for (String skuno : skuNo) {
	            if(stockMap.containsKey(skuno)){
	                skustock.put(skuno, stockMap.get(skuno));
	            } else{
	                skustock.put(skuno, "0");

	            }
	        }
		}else{
			logInfo.info("获取到的供应商的数据大小是0"); 
		}
		
		return skustock;
	}
	
	@SuppressWarnings("unused")
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args){
		loadSpringContext();		
	}
	
}
