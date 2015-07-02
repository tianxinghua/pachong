/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.acanfora.stock;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;

import com.shangpin.iog.acanfora.stock.dto.Item;
import com.shangpin.iog.acanfora.stock.dto.Items;
import com.shangpin.iog.acanfora.stock.dto.Product;
import com.shangpin.iog.acanfora.stock.dto.Products;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
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
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, Integer> skustock = new HashMap<>(skuNo.size());
        List<Item> itemList = new ArrayList<>();
        Map<String,String> stockMap = new HashMap<>();

        String kk = HttpUtils.get("http://www.acanfora.it/api_ecommerce_v2.aspx");
        Products products = null;
        try {
            logger.info("拉取ACANFORA数据开始");
            products = ObjectXMLUtil.xml2Obj(Products.class, kk);
            logger.info("拉取ACANFORA数据成功");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        List<Product> productList = products.getProducts();
        for (Product product : productList) {

            Items items = product.getItems();
            if (null == items) {
                continue;
            }
            for(Item item:items.getItems()){
                if(StringUtils.isNotBlank(item.getStock()) ){
                     stockMap.put(item.getItem_id(),item.getStock());
                }
            }
//            itemList.containsAll(items.getItems());
        }

        for (String skuno : skuNo) {
//            for (Item item : itemList) {
//                if (skuno.equals(item.getItem_id())) {
//                    skustock.put(skuno, Integer.valueOf(item.getStock()));
//                    break;
//                }
//            }
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, Integer.valueOf(stockMap.get(skuno)));
            }
        }
        logger.info("ACANFORA赋值库存数据成功");
        return skustock;
    }

    public static void main(String[] args) throws Exception {

        AbsUpdateProductStock grabStockImp = new GrabStockImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("ACANFORA更新数据库开始");
        grabStockImp.updateProductStock("2015050800242","2015-01-01 00:00",format.format(new Date()));
        logger.info("ACANFORA更新数据库结束");

    }

}
