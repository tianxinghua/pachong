package com.shangpin.iog.monnierfreres.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.monnierfreres.utils.DownloadAndReadCSV;




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
    private static String supplierId;
    private static String url;
    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("param");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
    }
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //定义三方
    	Map<String,String> stockMap = new HashMap<String,String>();
    	Map<String,String> map = DownloadAndReadCSV.readLocalCSV();
    
    	for(String skuId:skuNo){
    		System.out.println("skuID:"+skuId+";sku:"+skuId+";qty:"+map.get("qty"));
    		logger.info("skuID:"+skuId+";sku:"+skuId+";qty:"+map.get("qty"));
    		if(map.containsKey(skuId)){
    			System.out.println("sku:"+skuId+";qty:"+map.get("qty"));
    			logger.info("sku:"+skuId+";qty:"+map.get("qty"));
    			stockMap.put(skuId,map.get(skuId));
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
    public static void main(String[] args) {
        
    	//加载spring
        loadSpringContext();
        //拉取数据
        StockImp stockImp =(StockImp)factory.getBean("monnierfreres");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("reebonz更新库存开始");
        try {
			stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("reebonz更新库存结束");
        System.exit(0);
    }

}
