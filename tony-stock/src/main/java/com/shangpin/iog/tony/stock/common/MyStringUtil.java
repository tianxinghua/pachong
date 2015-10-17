package com.shangpin.iog.tony.stock.common;

import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2015/10/16.
 */
public class MyStringUtil {
    private static Logger loggerError = Logger.getLogger("error");
    /**
     *ͨ��event�¼���ȡSkuId
     */
    public static String getSkuId(String item){
        String skuId = null;
        if (item.contains("sku")){
            skuId = item.substring(item.indexOf("sku")+7).split("\",\"")[0];
        }
        return  skuId;
    }
    /**
     *ͨ��event�¼���ȡSkuId��Ӧ�Ŀ����Ϣ
     */
    public static Integer getQty(String item){
        Integer qty = null;
        if (item.contains("sku")){
            try{
                qty = Integer.parseInt(item.split(",")[2].split(":")[1]);
            }catch (NumberFormatException e){
                loggerError.error("�������ת���쳣,�����Ϣ��"+item);
                return qty;
            }
        }
        return  qty;
    }
}
