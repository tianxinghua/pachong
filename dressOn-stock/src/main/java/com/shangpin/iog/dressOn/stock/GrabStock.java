package com.shangpin.iog.dressOn.stock;

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
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dressOn.stock.dto.Product;
import com.shangpin.iog.dressOn.stock.utils.CsvUtil;

@Component("dressOnStock")
public class GrabStock extends AbsUpdateProductStock{

	private static Logger logInfo = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String url;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
	}

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
		
		List<Product> items = CsvUtil.readCSV(url, Product.class, ',');
		if(items.size()>0){
			logInfo.info("------------------一共有"+items.size()+"条数据----------------"); 
			for(Product product :items){
				stockMap.put(product.getSku_No(), product.getQty());
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
		GrabStock grabStockImp = (GrabStock)factory.getBean("dressOnStock");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logInfo.info("dressOn更新数据库开始");
		System.out.println("dressOn更新数据库开始");
		try {
			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
					format.format(new Date()));
		} catch (Exception e) {
			logError.error(e.getMessage());
			e.printStackTrace();
		}
		logInfo.info("dressOn更新数据库结束");
		System.out.println("dressOn更新数据库结束");
		System.exit(0);
//		try{
//			GrabStock grabStockImp = new GrabStock();
//			grabStockImp.grabStock(null);
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
	}
}
