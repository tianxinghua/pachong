package com.shangpin.iog.linoricci.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.linoricci.stock.common.Constant;
import com.shangpin.iog.linoricci.stock.common.MyFtpClient;
import com.shangpin.iog.linoricci.stock.common.MyStringUtil;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
public class LinoricciStockImp  extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {

        logger.info("down load stock file... ");
        boolean isOK = new MyFtpClient().downLoad();
        Map<String,Integer> stockMap = null;
        if (isOK){
            logger.info("down load stock file success,convert file into string ");
            stockMap = MyStringUtil.getStockByFile(Constant.LOCAL_STOCK_FILE);
        };
        //定义三方
        Map returnMap = new HashMap();
        String itemId = "";
        Iterator<String> iterator=skuNo.iterator();
        //为产品库存循环赋值
        logger.info("为产品库存循环赋值===========================");
        while (iterator.hasNext()){
            itemId = iterator.next();
            logger.info("SkuId is " + itemId + ", stock is " + stockMap.get(itemId));
            returnMap.put(itemId, stockMap.get(itemId));
        }
        return returnMap;
    }

    public static void main(String[] args) throws Exception {
        LinoricciStockImp impl = new LinoricciStockImp();

/*        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("TESSABIT更新数据库开始");
        impl.updateProductStock(Constant.SUPPLIER_ID, "2015-01-01 00:00", format.format(new Date()));
        logger.info("TESSABIT更新数据库结束");
        System.exit(0);*/

        List<String> skuNo = new ArrayList<>();
        skuNo.add("46347062113323105203");
        skuNo.add("46347062110248785299");
        skuNo.add("194032109501461360");
        Map returnMap = impl.grabStock(skuNo);
        System.out.println("test return size is "+returnMap.keySet().size());
        for(Object key: returnMap.keySet()){
            System.out.println("skuId is "+key+",stock is "+returnMap.get(key));;
        }
    }
}
