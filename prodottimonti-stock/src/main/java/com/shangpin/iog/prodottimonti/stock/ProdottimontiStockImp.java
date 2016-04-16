package com.shangpin.iog.prodottimonti.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;


import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.prodottimonti.stock.schedule.AppContext;

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
 * Created by huxia on 2015/8/12.
 */
@Component("prodottimontiStock")
public class ProdottimontiStockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static LoggerUtil logError = LoggerUtil.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }


    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();

        Map<String,String> mongMap = new HashMap<>();
        try{

            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","erminiomonti");
            logMongo.info("获取供应商商品列表失败");

            for (String skuno : skuNo) {
            	try{
            		
            		stockMap = getStock(skuno);
//            		logger.info("抓取的供货商stockMap==="+stockMap.toString());
                    if (stockMap.size() > 0)
                        skustock.put(skuno, stockMap.get(skuno));
                    else
                        skustock.put(skuno, "0");
            		
            	}catch(Exception e){
            		logError.equals(e);
            	}
                
            }

        }catch (Exception e){
        	logError.equals(e);
        } finally {
            HttpUtil45.closePool();
        }
        logger.info("erminiomonti赋值库存数据成功"+skustock.toString());
        return skustock;
    }


    private static Map<String,String> getStock(String sku){
        String url = "http://www.3forb.it/webserver/prodotto/?pid="+sku;

        logger.info("url====="+url); 
        OutTimeConfig timeConfig =new OutTimeConfig(1000*60,1000*60,1000*60);
        Map<String,String> map = new HashMap<>();
        String jsonstr = HttpUtil45.get(url,timeConfig,null,null,null);
        logger.info("jsonstr===="+jsonstr); 
        if( jsonstr != null){
            JSONObject obj = JSONObject.fromObject(jsonstr);
//            System.out.println("stock="+obj.has("stock"));
            if (!obj.isNullObject() && obj.has("stock")){
                map.put(obj.getString("sku"), obj.getString("stock"));
            }
        }
        logger.info("map====="+map); 
        return map;
    }

    public static void main(String args[]) throws Exception {
    	//加载spring
        loadSpringContext();
//        ProdottimontiStockImp stockImp = (ProdottimontiStockImp)factory.getBean("prodottimontiStock");
//
//        //AbsUpdateProductStock prodottimontiStockImp = new ProdottimontiStockImp();
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("prodottimonti更新库存开始");
//        try {
//        	stockImp.updateProductStock(supplierId,"2015-10-01 00:00",format.format(new Date()));
//		} catch (Exception e) {
//			loggerError.error("prodottimonti更新库存失败"+e.getMessage());
//			e.printStackTrace();
//		}
//        logger.info("prodottimonti更新库存结束");
//        System.exit(0);
    }
}
