package com.shangpin.iog.tony.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;


import com.shangpin.iog.tony.common.Constant;
import com.shangpin.iog.tony.common.StringUtil;
import com.shangpin.iog.tony.dto.Items;
import com.shangpin.iog.tony.common.MyJsonClient;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lizhongren on 2015/10/20.
 */
public class TonyProductStockImp extends AbsUpdateProductStock {


    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");


    private MyJsonClient jsonClient = new MyJsonClient();
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {


        //获取数据
        String  itemsJson = jsonClient.httpPostOfJson(Constant.ITEMS_INPUT, Constant.ITEMS_URL);
        //解析数据
        Items[] array = new Gson().fromJson(new StringUtil().getItemsArray(itemsJson),
                new TypeToken<Items[]>() {}.getType());
        if(null!=array){
            System.out.println("tony qty is ---------"+array.length);
            logger.info("tony qty is ---------"+array.length);
        } else{
            System.out.println("tony qty is ---------"+0);
            logger.info("tony qty is ---------"+0);
        }

        //保存数据
        String skuId = "";

        Map<String,String> map = new HashMap<>();
        Map<String,String> sopMap =new HashMap<>();
        for(String sku:skuNo){
            sopMap.put(sku,"");

        }
        for(Items item:array){
            skuId = item.getSku();
            logger.info("sku = " + skuId + " quantity =  " + item.getQty());
            if(sopMap.containsKey(skuId)){
                map.put(skuId,item.getQty());
            }

        }
        return map;
    }


    public static void main(String[] args) throws Exception {
        AbsUpdateProductStock impl = new TonyProductStockImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("TESSABIT更新数据库开始");
        impl.updateProductStock(Constant.SUPPLIER_ID, "2015-01-01 00:00", format.format(new Date()));
        logger.info("TESSABIT更新数据库结束");
        System.exit(0);


    }
}
