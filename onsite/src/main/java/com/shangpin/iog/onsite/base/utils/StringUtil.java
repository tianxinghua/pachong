package com.shangpin.iog.onsite.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
public class StringUtil {

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {

        //System.out.println(localFile);
        System.out.println("00000000000000000000000000000");
        System.out.println(StringUtil.getSubBySub("","1984127411_12","1984127411_12",1500));
    }



    /**
     *获取单品数量和价格
     */
    public static String getStockAndSupplyPrice(String item){

        System.out.println("item=="+item);
        String rtnStr = new StringBuffer(item.substring(item.indexOf("<stock>")+7,item.indexOf("</stock>")))
                .append("/")
                .append(item.substring(item.indexOf("<supply_price>")+14, item.indexOf("</supply_price>"))).toString();
        //System.out.println("rtnStr=="+rtnStr);
        return rtnStr;
    }
    /**
     *获取单品数量
     */
    public static String getSubBySub(String str,String begin,String end,int eAdd){
        str = str.substring(str.indexOf(begin)+begin.length(),str.indexOf(end)+eAdd);
        if(str.contains("stock")){
            str = getSubBySub(str,"<stock>","</stock>",0);
        }
        return str;
    }
}
