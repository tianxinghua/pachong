package com.shangpin.iog.smets.stock;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.shangpin.iog.smets.dto.TxtDTO;
import com.shangpin.iog.smets.util.TXTUtil;

/**
 * Created by monkey on 2015/10/20.
 */
@Component("smetsstock")
public class StockClientImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	private static Logger errorLogs = Logger.getLogger("error");
	 private static ApplicationContext factory;
	    private static void loadSpringContext()
	    {
	        factory = new AnnotationConfigApplicationContext(AppContext.class);
	    }
	private static ResourceBundle bdl = null;
	private static String supplierId;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}




	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		String skuId ="";
		int stock = 0;
		System.out.println("开始获取sku");
		List<TxtDTO> skuLists = TXTUtil.downloadFTP(TxtDTO.class, ";");
		System.out.println("获取sku完毕");
		logger.info("获取sku完毕");
		Map<String,String> stockMap = new HashMap<String, String>();
		Map<String,String> returnMap = new HashMap<String, String>();
		for (TxtDTO TxtDTO : skuLists) {
			try {
				if (stockMap.containsKey(TxtDTO.getSkuId())) {
					stock = Integer.valueOf(stockMap.get(TxtDTO.getSkuId()))+Integer.valueOf(TxtDTO.getStock());
					stockMap.put(TxtDTO.getSkuId(), String.valueOf(stock));
				}else{
					stockMap.put(TxtDTO.getSkuId(), TxtDTO.getStock());
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorLogs.error(e); 
			}
			
		}
		Iterator<String> it = skuNo.iterator();
		System.out.println("遍历填充"+skuNo.size());
		int i =0;
		while (it.hasNext()) {
			try {
				i++;
				skuId = it.next();
				if (stockMap.containsKey(skuId)) {
					returnMap.put(skuId, stockMap.get(skuId));
				}else{
					returnMap.put(skuId, "0");
				}
				System.out.println("填充"+i);
			} catch (Exception e) {
				e.printStackTrace();
				errorLogs.error(e); 
			}
			
		}
		System.out.println("loop successfully");
		return returnMap;
	}

	public static void main(String[] args) throws Exception {
		//加载spring
//		System.out.println("加载spring");
//        loadSpringContext();
//        System.out.println("加载spring结束");
//        StockClientImp stockImp =(StockClientImp)factory.getBean("smetsstock");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		logger.info("更新数据库开始");
//		System.out.println("更新数据库开始");
//		try {
//			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//		} catch (Exception e) {
//			logger.info("更新库存数据库出错"+e.toString());
//		}
//		System.out.println("更新数据库结束");
//		logger.info("更新数据库结束");
//		System.exit(0);
		new StockClientImp().grabStock(null);
	}


}
