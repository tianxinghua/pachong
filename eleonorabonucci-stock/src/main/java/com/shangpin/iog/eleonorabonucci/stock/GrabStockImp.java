/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.eleonorabonucci.stock;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;


import com.shangpin.iog.eleonorabonucci.dto.Item;
import com.shangpin.iog.eleonorabonucci.dto.Items;
import com.shangpin.iog.eleonorabonucci.dto.Product;
import com.shangpin.iog.eleonorabonucci.dto.Products;
import com.shangpin.iog.eleonorabonucci.schedule.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;

import java.lang.String;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
@Component("eleonorabonucciStock")
public class GrabStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    private static String supplierId;

    private  static  ResourceBundle bundle = ResourceBundle.getBundle("sop");
    static {

        supplierId = bundle.getString("supplierId");

    }

    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, Integer> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();


        Products products = null;
        try {
            logger.info("拉取eleonorabonucci数据开始");


            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30, 1000*60*30,1000*60*30);
//            timeConfig.confRequestOutTime(600000);
//            timeConfig.confSocketOutTime(600000);
            // http://www.eleonorabonucci.com/rss/demo.aspx
            String result = HttpUtil45.get("http://www.eleonorabonucci.com/FEED_PRODUCT/59950/HK", timeConfig, null);
            int i = 0;
            while((StringUtils.isBlank(result) || HttpUtil45.errorResult.equals(result)) && i<10){
            	result = HttpUtil45.get("http://www.eleonorabonucci.com/FEED_PRODUCT/59950/HK", timeConfig, null);
            	i++;
            }
            logger.info("================="+i+"===================");
//            HttpUtil45.closePool();


            //Remove BOM from String
            if (result != null && !"".equals(result)) {
                result = result.replace("\uFEFF", "");
            }

            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","eleonorabonucci");
            mongMap.put("result",result) ;
            try {
                logMongo.info(mongMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            products = ObjectXMLUtil.xml2Obj(Products.class, result);
            logger.info("拉取eleonorabonucci数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("拉取eleonorabonucci数据失败---" + e.getMessage());
            throw new ServiceMessageException("拉取eleonorabonucci数据失败");

        } finally {
//            HttpUtil45.closePool();
        }
        List<Product> productList = products.getProducts();
        String skuId = "";
        for (Product product : productList) {

            Items items = product.getItems();
            if (null == items) {
                continue;
            }
            List<Item> itemList = items.getItems();
            if(null==itemList) continue;
            for(Item item:items.getItems()){
                if(StringUtils.isNotBlank(item.getStock()) ){

                    skuId = item.getItem_id();
                    if(skuId.indexOf("½")>0){
                        skuId = skuId.replace("½","+");
                    }
                     stockMap.put(skuId,item.getStock());
                }
            }
        }

        for (String skuno : skuNo) {

            if(stockMap.containsKey(skuno)){
                try {
                    skustock.put(skuno, Integer.valueOf(stockMap.get(skuno)));
                } catch (Exception e) {
                    skustock.put(skuno, 0);
//                    e.printStackTrace();
                }
            } else{
                skustock.put(skuno, 0);
            }
        }
        logger.info("eleonorabonucci赋值库存数据成功");
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
//        GrabStockImp grabStockImp = (GrabStockImp)factory.getBean("eleonorabonucciStock");
//
//        String host = bundle.getString("HOST");
//        String app_key = bundle.getString("APP_KEY");
//        String app_secret= bundle.getString("APP_SECRET");
//        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)||StringUtils.isBlank(app_secret)){
//            logger.error("参数错误，无法执行更新库存");
//        }
//        //AbsUpdateProductStock grabStockImp = new GrabStockImp();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("eleonorabonucci更新数据库开始");
//        try {
//        	grabStockImp.updateProductStock(host,app_key,app_secret,"2015-01-01 00:00",format.format(new Date()));
//		} catch (Exception e) {
//			loggerError.error("eleonorabonucci更新库存失败"+e.getMessage());
//		}
//        logger.info("eleonorabonucci更新数据库结束");
//        System.exit(0);

    }

}
