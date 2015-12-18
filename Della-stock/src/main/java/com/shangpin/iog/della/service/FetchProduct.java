package com.shangpin.iog.della.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;


import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.della.dto.Item;
import com.shangpin.iog.della.utils.CSVUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.sop.AbsUpdateProductStock;


@Component("dellaStock")
public class FetchProduct extends AbsUpdateProductStock {
	
	private static Logger logError = Logger.getLogger("error");
	private static Logger logInfo  = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String remoteFileName = "";

	private static String host;
	private static String app_key;
	private static String app_secret;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		remoteFileName = bdl.getString("remoteFileName");
		host = bdl.getString("HOST");
	    app_key = bdl.getString("APP_KEY");
	    app_secret = bdl.getString("APP_SECRET");

	}
	
	public Map<String, Integer> grabStock(Collection<String> skuNo)
			throws ServiceException ,Exception{
		
		Map<String, Integer> skustock = new HashMap<>();
		Map<String,Integer> stockMap = new HashMap<>();

		
		List<Item> items = CSVUtil.readLocalCSV(remoteFileName,Item.class, ";");
		for(Item item:items){
			

			stockMap.put(item.getItem_code(), Integer.parseInt(item.getQuantity()));

			stockMap.put(item.getItem_code(), Integer.parseInt(item.getQuantity()));

//			System.out.println(stockMap.toString());
		}
		
		for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, 0);

            }
        }
		
		return skustock;
	}
	
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args){
		
		loadSpringContext();		
		FetchProduct fetchProduct = (FetchProduct)factory.getBean("dellaStock");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logInfo.info("della更新数据库开始");
		System.out.println("della更新数据库开始");
		try {
			
			fetchProduct.updateProductStock(host, app_key, app_secret, "2015-01-01 00:00", format.format(new Date()));

		} catch (Exception e) {
			logError.error(e.getMessage());
			e.printStackTrace();
		}
		logInfo.info("della更新数据库结束");
		System.out.println("della更新数据库结束");
		System.exit(0);
		
//		try {
//			AbsUpdateProductStock fetchProduct = new FetchProduct();
//			fetchProduct.grabStock(null);
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
}
