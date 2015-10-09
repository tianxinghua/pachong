package com.shangpin.iog.tony.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.tony.common.MyJsonClient;
import com.shangpin.iog.tony.common.StringUtil;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
public class TonyStockImp extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
   // @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //get 20 events per call
        logger.info("Tony get json begin...");
        String json = new MyJsonClient().getEvents();
        logger.info("Tony get json end...");
        logger.info("the return value is "+json);
        //定义三方
        Map returnMap = new HashMap();
        String[] fields = null;
        String key = "";
/*        String itemId = "";
        Iterator<String> iterator=skuNo.iterator();
        //为供应商循环赋值
        while (iterator.hasNext()){
            itemId = iterator.next();
            if (json.contains(itemId)){
                returnMap.put(itemId, StringUtil.getStockById(json.substring(json.indexOf(itemId),json.indexOf(itemId)+50)) );
            }
        }*/
         String[] strArr = json.split("additional_info");
        System.out.println(strArr.length);
        for (String item:strArr){
            if (item.contains("sku")){
                fields = item.split(",");
                key = fields[0].substring(10);
                returnMap.put(key.substring(0,key.length()-1),fields[1].split(":")[1].replace("}}",""));
            }
        }
        //记录日志
        for (Object logKey:returnMap.keySet()){
            logger.info("Sku ID is "+logKey+",stock is "+returnMap.get(logKey));
        }
        return returnMap;
    }

    public static void main(String[] args) throws Exception {
        TonyStockImp impl = new TonyStockImp();
/*        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("TESSABIT更新数据库开始");
        impl.updateProductStock("2015091501331", "2015-01-01 00:00", format.format(new Date()));
        logger.info("TESSABIT更新数据库结束");
        System.exit(0);*/

        List<String> skuNo = new ArrayList<>();
        skuNo.add("M4004574_001-40");
        Map returnMap = impl.grabStock(skuNo);
        System.out.println("test return size is "+returnMap.keySet().size());
        for (Object key:returnMap.keySet()){
            System.out.print("key is " + key);
            System.out.println(",value is "+returnMap.get(key));
        }
            Timer timer = new Timer();
            timer.schedule(null, 1000, 1000);
    }
}
