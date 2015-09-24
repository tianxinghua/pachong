package com.shangpin.iog.onsite.base.utils;

import com.shangpin.iog.onsite.base.constance.Constant;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/18.
 */
public class MapUtil {
    /**
     *update stock
     */
    public static Map<String,String> grabStock(Collection<String> skuNo,String json){
        //definition
        Map returnMap = new HashMap();
        String itemId = "";
        Iterator<String> iterator=skuNo.iterator();
        //put value into map
        while (iterator.hasNext()){
            itemId = iterator.next();
            returnMap.put(itemId, StringUtil.getSubBySub(json, itemId, itemId, Constant.ITEM_LENTH));
        }
        return returnMap;
    }
}
