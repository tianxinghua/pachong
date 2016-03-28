package com.shangpin.iog.rossana.stock.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.rossana.stock.dto.Item;
import com.shangpin.iog.rossana.stock.schedule.AppContext;
import com.shangpin.iog.rossana.stock.utils.CsvUtil;

@Component("rossanaStock")
public class FetchProduct extends AbsUpdateProductStock{

	private static LoggerUtil logError = LoggerUtil.getLogger("error");
	private static Logger logInfo  = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String filePath = "";
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		filePath = bdl.getString("filepath");
	}
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<>();
		Map<String,String> stockMap = new HashMap<>();
		
		OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
		String result = "";
		List<Item> items = null;
		try{
			result = HttpUtil45.get(filePath, timeConfig, null);
			items = CsvUtil.readLocalCSV(result, Item.class, ";");
		}catch(Exception e){
			logError.error(e);
			return skustock;
		}		
		for(Item item:items){
			stockMap.put(item.getSku().replaceAll("\"", ""), 
							item.getQty_in_stock().replaceAll("\"", ""));
		}
		for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
		
		return skustock;
	}
	
	private static ApplicationContext factory;

	private static void loadSpringContext() {

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) {

		loadSpringContext();
//		FetchProduct fetchProduct = (FetchProduct) factory
//				.getBean("rossanaStock");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		logInfo.info("更新数据库开始");
//		System.out.println("===========更新数据库开始============");
//		try {
//			fetchProduct.updateProductStock(supplierId, "2015-01-01 00:00",
//					format.format(new Date()));
//		} catch (Exception e) {
//			logError.error(e.getMessage());
//			e.printStackTrace();
//		}
//		logInfo.info("更新数据库结束");
//		System.out.println("==========更新数据库结束==========");
//		System.exit(0);

	}
	
}
