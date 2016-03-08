package com.shangpin.iog.hottest.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lubaijiang on 2015/9/14.
 */
@Component("hotteststock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger error = Logger.getLogger("error");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url;
    private static ApplicationContext factory;
    private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 60*5,
			1000 * 60 * 5, 1000 * 60 * 5);
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
	}
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String,String> skuMap = new HashMap<String,String>();
        Map<String,String> returnMap = new HashMap<String,String>();
        
        Map<String,String> headMap = new HashMap<>();
		headMap.put("Authorization", "RIX5NkHDIM25yUFZmDlVSdWEE7V3aSYv");
		int page = 1;
		int maxPage = 2;		
		while(page <=maxPage){
			String uri = url + page;
			logger.info("uri===="+uri); 
			String result = HttpUtil45.get(uri, outTimeConf, null, headMap, "", "");			
			logger.info(result);
			JSONObject jsonObj = JSONObject.fromObject(result);
			JSONObject products = jsonObj.getJSONObject("products");
			for(int k=0;k<100;k++){
				if (products.containsKey(String.valueOf(k))) {
					JSONObject product = products.getJSONObject(String.valueOf(k));// spu
					try {
						skuMap.put(product.getString("upc"),product.getString("qty"));
					} catch (Exception ex) {
						ex.printStackTrace();
						error.error(ex);
					}

				} else {
					break;
				}
			}
			
			maxPage = products.getInt("pages");
			logger.info("page====="+page);
			logger.info("maxPage====="+maxPage);
			page = page+1;
		}
		System.out.println(skuMap.toString()); 
		for (String skuno : skuNo) {
            if(skuMap.containsKey(skuno)){
            	returnMap.put(skuno, skuMap.get(skuno));
            } else{
            	returnMap.put(skuno, "0");
            }
        }
		
        return returnMap;
    }

    public static void main(String[] args){
    	//加载spring
        loadSpringContext();
        StockImp stockImp =(StockImp)factory.getBean("hotteststock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("hottest更新数据库开始");
        try {
			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
		} catch (Exception e) {
			logger.info("hottest更新库存数据库出错"+e.toString());
		}
        logger.info("hottest更新数据库结束");
        System.exit(0);
    	
//    	StockImp stock = new StockImp();
//    	try {
//			stock.grabStock(null);
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }
}
