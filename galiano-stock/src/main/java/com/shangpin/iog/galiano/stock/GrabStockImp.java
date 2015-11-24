/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.galiano.stock;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;


import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.galiano.stock.dto.Item;
import com.shangpin.iog.galiano.stock.dto.Items;
import com.shangpin.iog.galiano.stock.dto.Product;
import com.shangpin.iog.galiano.stock.dto.Products;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;

import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("galianoStock")
public class GrabStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");

    private static ResourceBundle bdl=null;
    private static String supplierId;
    
    private  static  ResourceBundle bundle = ResourceBundle.getBundle("sop");

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, Integer> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();




        Products products = null;
        try {
            logger.info("拉取galiano数据开始");
            Map<String,String> mongMap = new HashMap<>();

            OutTimeConfig timeConfig =new OutTimeConfig(1000*60*20,1000*60*20,1000*60*20);
            String result = HttpUtil45.get("http://www.galianostore.com/shangpin.xml", timeConfig, null);

            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","galiano");
            mongMap.put("result",result) ;
            try {
                logMongo.info(mongMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            products = ObjectXMLUtil.xml2Obj(Products.class, result);
            logger.info("拉取galiano数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("拉取galiano数据失败---" + e.getMessage());
            throw new ServiceMessageException("拉取galiano数据失败");
        }finally {
            HttpUtil45.closePool();
        }
        List<Product> productList = products.getProducts();
        String skuId = "";

        for (Product product : productList) {

            Items items = product.getItems();
            if (null == items) {
                continue;
            }
            List<Item>  itemList = items.getItems();
            if(null==itemList) continue;
            for(Item item:itemList){
                try {
                    if(StringUtils.isNotBlank(item.getStock()) ){

                        skuId = item.getItem_id();
                        if(skuId.indexOf("½")>0){
                            skuId = skuId.replace("½","+");
                        }
                         stockMap.put(skuId,item.getStock());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }

        }

        for (String skuno : skuNo) {

            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, Integer.valueOf(stockMap.get(skuno)));
            }else{
                skustock.put(skuno,0);
            }
        }

        logger.info("galiano赋值库存数据成功");
        return skustock;
    }

    private static ApplicationContext factory;
    private static void loadSpringContext()

    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) throws Exception {
//        AbsUpdateProductStock grabStockImp = new GrabStockImp();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("galiano更新数据库开始");
//        grabStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
//        
//        logger.info("galiano更新数据库结束");
//        System.exit(0);


        loadSpringContext();
        logger.info("----初始SPRING成功----");
        //拉取数据
        GrabStockImp grabStockImp =(GrabStockImp)factory.getBean("galianoStock");

    	String host = bundle.getString("HOST");
        String app_key = bundle.getString("APP_KEY");
        String app_secret= bundle.getString("APP_SECRET");
        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)||StringUtils.isBlank(app_secret)){
            logger.error("参数错误，无法执行更新库存");
        }
        


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("galiano更新数据库开始");
        try {
            grabStockImp.updateProductStock(host,app_key,app_secret,"2015-01-01 00:00",format.format(new Date()));
        } catch (Exception e) {
            loggerError.error("galiano更新库存失败:" + e.getMessage());
            e.printStackTrace();
        }
        logger.info("galiano更新数据库结束");
        System.exit(0);

    }

}
