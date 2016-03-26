package com.shangpin.iog.meifeng.stock;

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
import com.shangpin.iog.meifeng.stock.util.EfastUtil;

/**
 * Created by monkey on 2015/10/20.
 */
@Component("meifengStock")
public class StockClientImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
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
		Map<String,String> returnMap = new HashMap<String, String>();
		Iterator<String> it = skuNo.iterator();
		System.out.println("遍历填充"+skuNo.size());
		while (it.hasNext()) {
			skuId = it.next();
			String stock = EfastUtil.getTotalStock(skuId);
			returnMap.put(skuId, stock);
		}
		System.out.println("loop successfully");
		return returnMap;
	}

	public static void main(String[] args) throws Exception {
		//加载spring
        loadSpringContext();
        StockClientImp stockImp =(StockClientImp)factory.getBean("meifengStock");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("更新数据库开始");
		System.out.println("开始更新");
		try {
			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
		} catch (Exception e) {
			logger.info("更新库存数据库出错"+e.toString());
		} 
		System.out.println("更新结束");
		logger.info("更新数据库结束");
		System.exit(0);
	}


}
