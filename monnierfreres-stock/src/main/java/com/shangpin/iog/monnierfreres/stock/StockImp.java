package com.shangpin.iog.monnierfreres.stock;

import com.shangpin.framework.ServiceException;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.monnierfreres.utils.DownloadAndReadCSV;


import com.shangpin.sop.AbsUpdateProductStock;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 赵根春 on 2015/10/12.
 */
@Component("monnierfreres")
public class StockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl = null;
    private  static  ResourceBundle bundle = ResourceBundle.getBundle("sop");
    private static String supplierId;
    private static String url;
    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("param");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
    }

    @Override
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //定义三方
    	Map<String,Integer> stockMap = new HashMap<String,Integer>();
    	Map<String,String> map = DownloadAndReadCSV.readLocalCSV();
    
    	for(String skuId:skuNo){
    		System.out.println("skuID:"+skuId+";sku:"+skuId+";qty:"+map.get("qty"));
    		logger.info("skuID:"+skuId+";sku:"+skuId+";qty:"+map.get("qty"));
    		if(map.containsKey(skuId)){
    			System.out.println("sku:"+skuId+";qty:"+map.get("qty"));
    			logger.info("sku:"+skuId+";qty:"+map.get("qty"));
                try {
                    stockMap.put(skuId,Integer.valueOf(map.get(skuId)));
                } catch (NumberFormatException e) {
                    stockMap.put(skuId, 0);
                }
            }else{
                stockMap.put(skuId,0);
            }
    	}
        return stockMap;
    }
    public static String getPath(String realpath){
		   Date dt=new Date();
	        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
	        String date=matter1.format(dt).replaceAll("-","").trim();
     realpath = realpath+"_"+date+".csv";
     return realpath;
 }


    private static ApplicationContext factory;
    private static void loadSpringContext()

    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }



    public static void main(String[] args) {
        
    	//加载spring
        loadSpringContext();



        String host = bundle.getString("HOST");
        String app_key = bundle.getString("APP_KEY");
        String app_secret= bundle.getString("APP_SECRET");
        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)||StringUtils.isBlank(app_secret)){
            logger.error("参数错误，无法执行更新库存");
        }

        StockImp stockImp =(StockImp)factory.getBean("monnierfreres");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("monnierfreres更新库存开始");
        try {
			stockImp.updateProductStock(host,app_key,app_secret,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
            loggerError.error("monnierfreres更新库存失败");
			e.printStackTrace();
		}
        logger.info("monnierfreres更新库存结束");
        System.exit(0);
    }

}
