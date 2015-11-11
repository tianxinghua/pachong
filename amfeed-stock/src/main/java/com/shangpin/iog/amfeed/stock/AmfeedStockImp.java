package com.shangpin.iog.amfeed.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.amfeed.stock.dto.Product;
import com.shangpin.iog.amfeed.stock.util.MyCsvUtil;
import com.shangpin.sop.AbsUpdateProductStock;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangyuzhi on 2015/11/11.
 */
public class AmfeedStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private  static  ResourceBundle bundle = ResourceBundle.getBundle("sop");
    private long start = 0;//计时开始时间
    private long end = 0;//计时结束时间
    private String skuId = "";//单个skuId

    @Override
    public Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法开始！");
        logger.info("AMFEED Sku 条数："+skuNo.size());
        start = System.currentTimeMillis();
        boolean flag = false;
        try {
            flag = MyCsvUtil.csvDownload();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        logger.info("下载AMFEED文件结果"+flag+"，耗时："+(end-start)/1000+"秒");
        List<Product> list = null;
        Map<String,Integer> map = new HashMap();
        if (flag){
            start = System.currentTimeMillis();
            try {
                list = MyCsvUtil.readCSVFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Product p:list){
                map.put(p.getSku(),Integer.parseInt(p.getQty()));
            }
            end = System.currentTimeMillis();
            logger.info("解析AMFEED文件耗时："+(end-start)/1000+"秒");
        }

        Map<String,Integer> returnMap = new HashMap();
        Iterator<String> iterator=skuNo.iterator();
        logger.info("为AMFEED供应商产品库存循环赋值");
        start = System.currentTimeMillis();
        while (iterator.hasNext()){
            skuId = iterator.next();
            //logger.info("SkuId is " +itemId + ",stock is " +stock);
            returnMap.put(skuId, map.get(skuId));
        }
        end = System.currentTimeMillis();
        logger.info("为AMFEED产品库存赋值总共耗时："+(end-start)/1000+"秒");
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法结束！");
        return returnMap;
    }

    /**
     * test
     * */
    public static void main(String[] args) throws Exception {
        String host = bundle.getString("HOST");
        String app_key = bundle.getString("APP_KEY");
        String app_secret= bundle.getString("APP_SECRET");
        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)||StringUtils.isBlank(app_secret)){
            logger.error("参数错误，无法执行更新库存");
        }
        AbsUpdateProductStock impl = new AmfeedStockImp();

      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("AMFEED更新数据库开始");
        impl.updateProductStock(host,app_key,app_secret, "2015-01-01 00:00", format.format(new Date()));
        logger.info("AMFEED更新数据库结束");
        System.exit(0);

/*
        List<String> skuNo = new ArrayList<>();
        skuNo.add("1983991600_12");
        skuNo.add("1983991600_11");
        skuNo.add("1983991600_10");
        skuNo.add("1983904634_11");
        skuNo.add("1983904634_12");
        skuNo.add("1983933587_1985020934");
        skuNo.add("1983933587_1985020935");
        skuNo.add("1983933587_1985020936");
        skuNo.add("1983933587_1985020937");
        skuNo.add("1983933587_1985020938");
        for (int i = 0;i<5000;i++){
            skuNo.add(i+"1983933587_1985020936");
        }
        Map returnMap = impl.grabStock(skuNo);
        System.out.println("test return size is "+returnMap.keySet().size());
        for(Object key: returnMap.keySet()){
           // System.out.println("skuId is "+key+",stock is "+returnMap.get(key));;
        }
*/


    }
}
