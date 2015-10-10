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
    private long startTime = 0;
    private long endTime = 0;
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法开始！");
        logger.info("计划更新库存的 Sku 条数："+skuNo.size());
        startTime = System.currentTimeMillis();
        boolean isOK = new MyFtpClient().downLoad();
        endTime = System.currentTimeMillis();
        logger.info("下载LINORICCI文件结果"+isOK+"，耗时："+(endTime-startTime)/1000+"秒");
        Map<String,Integer> stockMap = null;
        if (isOK){
            startTime = System.currentTimeMillis();
            stockMap = MyStringUtil.getStockByFile(Constant.LOCAL_STOCK_FILE);
            endTime = System.currentTimeMillis();
            logger.info("解析LINORICCI文件耗时："+(endTime-startTime)/1000+"秒");
        };
        //defination
        Map returnMap = new HashMap();
        String itemId = "";
        Integer value = null;
        Iterator<String> iterator=skuNo.iterator();
        logger.info("set stock for loop===========================");
        startTime = System.currentTimeMillis();
        while (iterator.hasNext()){
            itemId = iterator.next();
            value = stockMap.get(itemId);
            if (value == null){
                //logger.info("Item id is "+itemId+", stock is null,remove");
                continue;
            }
            //logger.info("SkuId is " + itemId + ", stock is " + value);
            returnMap.put(itemId, value);
        }
        endTime = System.currentTimeMillis();
        logger.info("为产品库存循环赋值耗时："+(endTime-startTime)/1000+"秒");
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法结束！");
        logger.info("返回需要更新库存的 Sku 条数："+returnMap.size());
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
