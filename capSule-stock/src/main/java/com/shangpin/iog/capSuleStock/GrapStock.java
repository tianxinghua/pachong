package com.shangpin.iog.capSuleStock;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.capSuleStock.excel.Item;
import com.shangpin.iog.capSuleStock.excel.ReadExcel;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

@Component("capSuleStock")
public class GrapStock extends AbsUpdateProductStock{

	private static Logger logInfo  = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String path;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		path = bdl.getString("path");
	}
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
		
		List<Item> list = new ReadExcel().readExcel(path);
		if(list.size()>0){
			for(Item item:list){
				try{
					
					if(!item.getQuatity().equals("0") && !item.getQuatity().equals("0.0")){
						String si = item.getSize();
						String size = si.substring(si.indexOf(":")+1, si.length());						
						stockMap.put((item.getId()+"-"+size),item.getQuatity());
//						System.out.println(stockMap.toString());
					}
					
				}catch(Exception ex){
					logError.error(ex);
					ex.printStackTrace();
				}
				
			}
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
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args) {

		loadSpringContext();
		GrapStock grabStockImp = (GrapStock)factory.getBean("capSuleStock");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logInfo.info("capSuleStock更新数据库开始");
		System.out.println("capSuleStock更新数据库开始");
		try {
			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
					format.format(new Date()));
		} catch (Exception e) {
			logError.error(e.getMessage());
			e.printStackTrace();
		}
		logInfo.info("capSuleStock更新数据库结束");
		System.out.println("capSuleStock更新数据库结束");
		System.exit(0);
//		try{
//			GrapStock grabStockImp = new GrapStock();
//			grabStockImp.grabStock(null);
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
		
	}

}
