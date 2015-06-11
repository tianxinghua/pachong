/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.acanfora.stock;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.acanfora.dto.Item;
import com.shangpin.iog.acanfora.dto.Items;
import com.shangpin.iog.acanfora.dto.Product;
import com.shangpin.iog.acanfora.dto.Products;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;

import javax.xml.bind.JAXBException;
import java.lang.String;
import java.util.*;

public class GrabStockImp extends AbsUpdateProductStock {

    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, Integer> skustock = new HashMap<String, Integer>(skuNo.size());
        List<Item> itemList = new ArrayList<>() ;

        String kk = HttpUtils.get("http://www.acanfora.it/api_ecommerce_v2.aspx");
        Products products = null;
        try {
            products = ObjectXMLUtil.xml2Obj(Products.class, kk);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        List<Product> productList = products.getProducts();
        for (Product product : productList) {

            Items items = product.getItems();
            if (null == items) {//ÎÞSKU
                continue;
            }
            itemList.containsAll(items.getItems());
        }

//        Map<String,itemList> maplist  = new HashMap<String,itemList>();
//        for (String skuno : skuNo) {
//                 Item item = maplist.get(skuno);
//                 skustock.put(skuno,item.getStock());
//        }

        for (String skuno : skuNo) {
            for (Item item : itemList) {
                if (skuno.equals(item.getItem_id())) {
                    break;
                }
            }
        }
        return skustock;
    }
}
