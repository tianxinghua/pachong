package com.shangpin.iog.mario;

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
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.mario.dto.Item;
import com.shangpin.iog.mario.schedule.AppContext;

@Component("mario")
public class GrabStock extends AbsUpdateProductStock{

	private static Logger logInfo = Logger.getLogger("info");
	private static LoggerUtil logError = LoggerUtil.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String uri;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		uri = bdl.getString("uri");
	}

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
		List<Item> items = null;
		try{
			items = CVSUtil.readCSV(uri, Item.class, ';');
		}catch(Exception ex){
			ex.printStackTrace();
			logError.error(ex);
			return skustock;
		}
		if(items.size()>0){
			logInfo.info("------------------一共有"+items.size()+"条数据----------------"); 
			for(Item item :items){
				stockMap.put(item.getCodice_Prodotto()+"-"+item.getNumero(), item.getStock());
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
//		GrabStock grabStockImp = (GrabStock)factory.getBean("mario");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		logInfo.info("mario更新数据库开始");
//		System.out.println("mario更新数据库开始");
//		try {
//			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
//					format.format(new Date()));
//		} catch (Exception e) {
//			logError.error(e.getMessage());
//			e.printStackTrace();
//		}
//		logInfo.info("mario更新数据库结束");
//		System.out.println("mario更新数据库结束");
//		System.exit(0);
//		try{
//			GrabStock grabStockImp = new GrabStock();
//			grabStockImp.grabStock(null);
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
	}
}
