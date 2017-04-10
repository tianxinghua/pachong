package com.shangpin.iog.theclutcher.stock.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.theclutcher.stock.dto.Product;
import com.shangpin.iog.theclutcher.utils.Test;

@Component("theClucherNew")
public class StockImp extends AbsUpdateProductStock {
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ApplicationContext factory;

	@Autowired
	EventProductService eventProductService;
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;
	public static int max;

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(
				new Class[] { AppContext.class });
	}

	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		Map<String, String> stockMap = new HashMap<String, String>();
		Map<String, String> supplierMap = new HashMap<String, String>();
		
		String json = Test.getJson();
		Gson gson = new Gson();
		List<Product> list = gson.fromJson(json,
				new TypeToken<List<Product>>() {
				}.getType());
		
		if(list!=null&&list.size()>0){
			for(Product product : list){
				supplierMap.put(product.getSkuID(),product.getStock());
			}
			for (String skuno : skuNo) {
				if (supplierMap.containsKey(skuno)) {
					stockMap.put(skuno, supplierMap.get(skuno));
				}else{
					stockMap.put(skuno, "0");	
				}
			}
		}
		return stockMap;
	}

	public static void main(String[] args) {
		loadSpringContext();

		StockImp stockImp = (StockImp) factory.getBean("theClucherNew");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("efashiom更新库存开始");
		System.out.println("efashiom更新库存开始");
		try {
			List<String> list = new ArrayList<String>();
			list.add("9949-137");
			list.add("9949-1372");
			stockImp.grabStock(list);
			stockImp.updateProductStock(supplierId, "2015-01-01 00:00",
					format.format(new Date()));
		} catch (Exception e) {
			loggerError.info(e);
			e.printStackTrace();
		}
		logger.info("efashiom更新库存结束");
		System.out.println("efashiom更新库存结束");
		System.exit(0);
	}

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}
}