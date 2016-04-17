package com.shangpin.iog.rossana.stock.service;

import java.io.File;
import java.io.IOException;
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
import com.shangpin.iog.rossana.stock.utils.CVSUtil;
import com.shangpin.iog.rossana.stock.utils.CsvUtil;
import com.shangpin.iog.rossana.stock.utils.DownLoad;

@Component("rossanaStock")
public class FetchProduct extends AbsUpdateProductStock{

	private static LoggerUtil logError = LoggerUtil.getLogger("error");
	private static Logger logInfo  = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String filePath = "";
	private static String local = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		filePath = bdl.getString("filepath");
		local = bdl.getString("local");
	}
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<>();
		Map<String,String> stockMap = new HashMap<>();
		
		OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 60, 1000*60 * 60);
		String result = "";
		List<Item> items = null;
		try{
			logInfo.info("===========获取供货商库存开始=================");
			result = HttpUtil45.get(filePath, timeConfig, null);
			logInfo.info("result========"+result); 
			items = CsvUtil.readLocalCSV(result, Item.class, ";");
			logInfo.info("items.size======="+items.size()); 
//			DownLoad.downFromNet(filePath, local);
//			File file = new File(local);
//			items = CVSUtil.readCSV(file, Item.class, ';');
			logInfo.info("===========下载转化成功=================");
		}catch(Exception e){
			logError.error(e);
			return skustock;
		}
		try {
			for(Item item:items){
				try {
					stockMap.put(item.getSku().replaceAll("\"", ""), 
							item.getQty_in_stock().replaceAll("\"", ""));
				} catch (Exception e) {
					logError.error(e);
				}
				
			}
		} catch (Exception e) {
			logError.error(e);
		}
		
		logInfo.info("获取供货商库存stockMap==="+stockMap.toString());
		logInfo.info("skuNo======="+skuNo.toString()); 
		for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
		logInfo.info("返回的skustock.size======="+skustock.size()); 
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
//		OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 6, 1000*60 * 5);
//		String result = HttpUtil45.get(filePath, timeConfig, null);
//		System.out.println(result); 
//		try {
//			List<Item> items = CsvUtil.readLocalCSV(result, Item.class, ";");
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 60, 1000*60 * 60);
//			String result = HttpUtil45.get(filePath, timeConfig, null);
//			System.out.println(result); 
//			List<Item> items = CsvUtil.readLocalCSV(result, Item.class, ";");
//			DownLoad.downFromNet(filePath, local);
//			File file = new File(local);
////			List<Item> items = CVSUtil.readCSV(file, Item.class, ';');
//			System.out.println(items.size()); 
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

	}
	
}
