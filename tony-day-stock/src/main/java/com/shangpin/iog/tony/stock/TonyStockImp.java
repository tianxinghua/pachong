package com.shangpin.iog.tony.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;


import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.tony.common.*;
import com.shangpin.iog.tony.dto.Data;
import com.shangpin.iog.tony.dto.Items;
import com.shangpin.iog.tony.dto.ReturnObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lizhongren on 2015/10/20.
 */
@Component("tonyStock")
public class TonyStockImp extends AbsUpdateProductStock {


    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    ProductFetchService productFetchService;
    private static MyJsonClient jsonClient = new MyJsonClient();
    private String itemsJson;
    private static Map<String,String> map = null;
    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	map = new HashMap<String,String>();
        itemsJson =  jsonClient.httpPostOfJson(Constant.ITEMS_INPUT, Constant.ITEMS_URL);
        //解析数据
        getStock(itemsJson);
        //保存数据
        Map<String,String> stockmap = new HashMap<String,String>();
        for(String sku:skuNo){
        	if(map.containsKey(sku)){
        		stockmap.put(sku, map.get(sku));
        	}else{
        		stockmap.put(sku,"0");
        	}
        }
        return stockmap;
    }
    private static void getStock(String itemsJson){
   	 
    	ReturnObject obj = new Gson().fromJson(itemsJson, ReturnObject.class);
    	if(obj!=null){
    		String next = obj.getNext_step();
    		Data data = obj.getData();
    		if(data!=null){
    			Items[] array = data.getInventory();
                String skuId = "";
                if(array!=null){
            	   for(Items item:array){
                       skuId = item.getSku();
                       map.put(skuId,item.getQty());
                   }
                }
                if(next!=null){
              	  getStock(jsonClient.httpPostOfJson("{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\",\"step\":\""+next+"\"}", Constant.ITEMS_URL));
                }
    		}
    	}
    }
    public static void main(String[] args) throws Exception {
    	
    	//加载spring
        loadSpringContext();
        //拉取数据
        TonyStockImp stockImp =(TonyStockImp)factory.getBean("tonyStock");
//        stockImp.grabStock(null);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("tony更新库存开始");
        System.out.println("tony更新库存开始");
        try {
			stockImp.updateProductStock(Constant.SUPPLIER_ID,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("tony更新数据库结束");
        System.out.println("tony更新库存结束");
        System.exit(0);


    }
}
