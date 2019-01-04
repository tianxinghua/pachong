package com.shangpin.iog.menlook;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.menlook.stock.dto.Item;
import com.shangpin.iog.menlook.util.DownloadAndReadCSV;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by monkey on 2015/10/20.
 */
@Component("menlookstock")
public class StockClientImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	 private static ApplicationContext factory;
	    private static void loadSpringContext()
	    {
	        factory = new AnnotationConfigApplicationContext(AppContext.class);
	    }
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String host;
	private static String app_key;
	private static String app_secret;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("sop");
		host = bdl.getString("HOST");
		app_key = bdl.getString("APP_KEY");
		app_secret = bdl.getString("APP_SECRET");
		supplierId = bdl.getString("supplierId");
	}

	@Autowired
	DownloadAndReadCSV csvUtil;

	@Override
	public Map<String, Integer> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		 List<Item> list = csvUtil.readLocalCSV();
		 Map<String,String> skuMap = new HashMap<String, String>();
		 Map<String,Integer> reMap = new HashMap<String, Integer>();
		 for (Item item : list) {
			skuMap.put(item.getSkuId(), item.getStock());
		}
		Iterator<String> it = skuNo.iterator();
		while (it.hasNext()) {
			String skuId = it.next();
			if (skuMap.containsKey(skuId)) {
				reMap.put(skuId, Integer.valueOf(skuMap.get(skuId)));
			}else {
				reMap.put(skuId, 0);
			}
		}
		return reMap; 
	}

	public static void main(String[] args) throws Exception {
		//加载spring
        loadSpringContext();
        StockClientImp stockImp =(StockClientImp)factory.getBean("menlookstock");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("更新数据库开始");
		try {
			
			stockImp.updateProductStock(host, app_key, app_secret, "2015-01-01 00:00", format.format(new Date()));
		} catch (Exception e) {
			logger.info("更新库存数据库出错"+e.toString());
		}
		logger.info("更新数据库结束");
		System.exit(0);
	}
}
