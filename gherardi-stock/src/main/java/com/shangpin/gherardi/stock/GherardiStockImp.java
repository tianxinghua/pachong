package com.shangpin.gherardi.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;


import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huxia on 2015/8/12.
 */
class GherardiStockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();

        Map<String,String> mongMap = new HashMap<>();
        try{

            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","gherardi-stock");
            logMongo.info("获取供应商商品列表失败");

            for (String skuno : skuNo) {
                stockMap = getStock(skuno);
                if (stockMap.size() > 0)
                    skustock.put(skuno, stockMap.get(skuno));
                else
                    skustock.put(skuno, "0");
            }

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            HttpUtil45.closePool();
        }
        return skustock;
    }


    private static Map<String,String> getStock(String sku){
        String url = "http://www.communicationislife.com/shanping/slist/?pid="+sku;

        OutTimeConfig timeConfig =new OutTimeConfig(1000*60,1000*60,1000*60);
        Map<String,String> map = new HashMap<>();
        String jsonstr = HttpUtil45.get(url,timeConfig,null,null,null);
        if( jsonstr != null){
            Collection<JSONObject> jsonList = JSONObject.fromObject(jsonstr).values();
            if (jsonList.size() >0){
                for (JSONObject jsonObject: jsonList){
                    map.put(jsonObject.getString("sku"), jsonObject.getString("stock"));
                }
            }
        }
        return map;
    }

    public static void main(String args[]) throws Exception {

        AbsUpdateProductStock gherardiStockImp = new GherardiStockImp();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("gherardi-stock更新数据库开始");
        //2015081401431
        gherardiStockImp.updateProductStock(supplierId,"2015-10-01 00:00",format.format(new Date()));
        logger.info("gherardi-stock更新数据库结束");
        System.exit(0);
    }



}
