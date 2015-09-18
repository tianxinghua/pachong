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
     *更新库存用
     */
    public static Map<String,String> grabStock(Collection<String> skuNo,String json){
        //定义三方
        Map returnMap = new HashMap();
        String itemId = "";
        Iterator<String> iterator=skuNo.iterator();
        //为供应商循环赋值
        while (iterator.hasNext()){
            itemId = iterator.next();
            returnMap.put(itemId, StringUtil.getSubBySub(json, itemId, itemId, Constant.ITEM_LENTH));
        }
        return returnMap;
    }
}
