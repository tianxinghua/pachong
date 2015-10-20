package com.shangpin.iog.tony.stock.common;

import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2015/10/16.
 */
public class MyStringUtil {
    private static Logger loggerError = Logger.getLogger("error");
    /**
     *通过event事件获取SkuId
     */
    public static String getSkuId(String item){
        String skuId = null;
        if (item.contains("sku")){
            skuId = item.substring(item.indexOf("sku")+7).split("\",\"")[0];
        }
        return  skuId;
    }
    /**
     *通过event事件获取SkuId对应的库存信息
     */
    public static Integer getQty(String item){
        Integer qty = null;
        if (item.contains("sku")){
            try{
                qty = Integer.parseInt(item.split(",")[2].split(":")[1]);
            }catch (NumberFormatException e){
                loggerError.error("库存类型转换异常,相关信息："+item);
                return qty;
            }
        }
        return  qty;
    }
}
