/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.acanfora.stock;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;

import com.shangpin.iog.acanfora.stock.dto.Item;
import com.shangpin.iog.acanfora.stock.dto.Items;
import com.shangpin.iog.acanfora.stock.dto.Product;
import com.shangpin.iog.acanfora.stock.dto.Products;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.lang.String;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GrabStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, Integer> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();


        Products products = null;
        try {
            logger.info("拉取ACANFORA数据开始");


            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confConnectOutTime(360000);
            timeConfig.confRequestOutTime(360000);
            timeConfig.confSocketOutTime(360000);
            String result = HttpUtil45.get("http://www.acanfora.it/api_ecommerce_v2.aspx", timeConfig, null);

            mongMap.put("supplierId","2015050800242");
            mongMap.put("supplierName","acanfora");
            mongMap.put("result",result) ;
            try {
                logMongo.info(mongMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            products = ObjectXMLUtil.xml2Obj(Products.class, result);
            logger.info("拉取ACANFORA数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("拉取ACANFORA数据失败---" + e.getMessage());
            throw new ServiceMessageException("拉取ACANFORA数据失败");

        } finally {
            HttpUtil45.closePool();
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
                skustock.put(skuno, Integer.valueOf(stockMap.get(skuno)));
            } else{
                skustock.put(skuno, 0);
            }
        }
        logger.info("ACANFORA赋值库存数据成功");
        return skustock;
    }

    public static void main(String[] args) throws Exception {

        AbsUpdateProductStock grabStockImp = new GrabStockImp();grabStockImp.supplierSkuIdMain=true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("ACANFORA更新数据库开始");
        grabStockImp.updateProductStock("2015050800242","2015-01-01 00:00",format.format(new Date()));
        logger.info("ACANFORA更新数据库结束");
        System.exit(0);

    }

}
