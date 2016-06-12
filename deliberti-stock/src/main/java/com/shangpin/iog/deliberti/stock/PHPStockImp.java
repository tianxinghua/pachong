package com.shangpin.iog.deliberti.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.deliberti.stock.dto.Product;
import com.shangpin.iog.deliberti.stock.utils.DataUtil;
import com.shangpin.iog.deliberti.stock.utils.MyUtil;
import com.shangpin.iog.dto.SkuDTO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by dongjinghui
 */
@Component("phpdelibertistock")
public class PHPStockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url;
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws Exception {
    	ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		List<String> list = new ArrayList<String>();
		Map<String,String> stockMap = new HashMap<String, String>();
		Map<String,String> returnMap = new HashMap<String, String>();
    	for (String skuId : skuNo) {
    		executor.execute(new DataUtil(stockMap, skuId.split("-")[0]));
		}
    	
	   while(true){
		 if(executor.getActiveCount()==0){
		   	break;
		 }
		 try {
			 System.out.println("活动线程数=="+executor.getActiveCount());
			Thread.sleep(1000*30);
		 } catch (InterruptedException e) {
			e.printStackTrace();
		 }
       }
	   
	    for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
            	returnMap.put(skuno, stockMap.get(skuno));
            } else{
            	returnMap.put(skuno, "0");
            }
        }

        return returnMap;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        PHPStockImp stockImp =(PHPStockImp)factory.getBean("phpdelibertistock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("deliberti更新数据库开始");
        System.out.println("deliberti更新数据库开始");
        try {
        	
        	Collection<String> skuNo = new ArrayList<String>();
        	skuNo.add("242705");
        	skuNo.add("242726");
        	skuNo.add("242727");
			stockImp.grabStock(skuNo );
        	
//			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//			stockImp.updateProductStock("2015111200218", "2015-01-01 00:00", format.format(new Date()));
		} catch (Exception e) {
			logger.info("deliberti更新库存数据库出错"+e.toString());
		}
        logger.info("deliberti更新数据库结束");
        System.out.println("deliberti更新数据库结束");
        System.exit(0);
    }
}
