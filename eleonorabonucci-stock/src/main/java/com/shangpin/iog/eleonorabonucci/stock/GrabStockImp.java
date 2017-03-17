package com.shangpin.iog.eleonorabonucci.stock;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.eleonorabonucci.dto.Item;
import com.shangpin.iog.eleonorabonucci.dto.Items;
import com.shangpin.iog.eleonorabonucci.dto.Product;
import com.shangpin.iog.eleonorabonucci.dto.Products;
import com.shangpin.iog.eleonorabonucci.schedule.AppContext;

@Component("eleonorabonucciStock")
public class GrabStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @SuppressWarnings("unused")
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();

        Products products = null;
        try {
            logger.info("拉取eleonorabonucci数据开始");
            OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30, 1000*60*30,1000*60*30);
            String result = HttpUtil45.get("http://www.eleonorabonucci.com/FEED_PRODUCT/59950/HK", timeConfig, null);
            int i = 0;
            while((StringUtils.isBlank(result) || HttpUtil45.errorResult.equals(result)) && i<10){
            	result = HttpUtil45.get("http://www.eleonorabonucci.com/FEED_PRODUCT/59950/HK", timeConfig, null);
            	i++;
            }
            logger.info("================="+i+"===================");
            //Remove BOM from String
            if (result != null && !"".equals(result)) {
                result = result.replace("\uFEFF", "");
            }
            products = ObjectXMLUtil.xml2Obj(Products.class, result);
            logger.info("拉取eleonorabonucci数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("拉取eleonorabonucci数据失败---" + e.getMessage());
            throw new ServiceMessageException("拉取eleonorabonucci数据失败");
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
                    skustock.put(skuno, stockMap.get(skuno));
                } catch (Exception e) {
                    skustock.put(skuno, "0");
//                    e.printStackTrace();
                }
            } else{
                skustock.put(skuno, "0");
            }
        }
        logger.info("eleonorabonucci赋值库存数据成功");
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
    }

}
