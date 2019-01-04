package com.shangpin.iog.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shangpin.iog.onsite.base.constance.Constant;


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
            returnMap.put(itemId, getSubBySub(json, itemId, itemId, Constant.ITEM_LENTH));
        }
        return returnMap;
    }
    public static String getSubBySub(String str,String begin,String end,int eAdd){
        str = str.substring(str.indexOf(begin)+begin.length(),str.indexOf(end)+eAdd);
        if(str.contains("stock")){
            str = getSubBySub(str,"<stock>","</stock>",0);
            Pattern p=Pattern.compile("(\\d+)");   
            Matcher m=p.matcher(str);       
            if(m.find()){
            	str =m.group(1);
            } else{
            	str="0";
            }
        }
        return str;
    }
}
