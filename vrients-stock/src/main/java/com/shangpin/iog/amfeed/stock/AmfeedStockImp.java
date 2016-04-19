package com.shangpin.iog.amfeed.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.amfeed.stock.dto.Product;
import com.shangpin.iog.amfeed.stock.util.MyCsvUtil;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.ice.ice.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangyuzhi on 2015/11/11.
 */
@Component("amfeed")
public class AmfeedStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
//    private static Logger loggerError = Logger.getLogger("error");
    private static LoggerUtil error = LoggerUtil.getLogger("error");

    private long start = 0;//计时开始时间
    private long end = 0;//计时结束时间
    private String skuId = "";//单个skuId

    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
       
    	Map<String,String> returnMap = new HashMap();
    	
    	logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法开始！");
        logger.info("AMFEED Sku 条数："+skuNo.size());
        start = System.currentTimeMillis();
        boolean flag = true;
        try {
            flag = MyCsvUtil.csvDownload();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            error.equals(e);
            return returnMap;
        }
        end = System.currentTimeMillis();
        logger.info("下载AMFEED文件结果"+flag+"，耗时："+(end-start)/1000+"秒");
        List<Product> list = null;
        Map<String,String> map = new HashMap();
        if (flag){
            start = System.currentTimeMillis();
            try {
                list = MyCsvUtil.readCSVFile();
            } catch (Exception e) {
                e.printStackTrace();
                error.equals(e);
                return returnMap;
            }
            for (int i = 1;i<list.size();i++){
                map.put(list.get(i).getSku(),list.get(i).getQty().trim());
            }
            end = System.currentTimeMillis();
            logger.info("解析AMFEED文件耗时："+(end-start)/1000+"秒");
        } else {
            error.error("下载AMFEED文件失败!");
        }
        
        Iterator<String> iterator=skuNo.iterator();
        logger.info("为AMFEED供应商产品库存循环赋值");
        start = System.currentTimeMillis();
        while (iterator.hasNext()){
            skuId = iterator.next();
            logger.info("SkuId is " +skuId + ",stock is " +map.get(skuId));
            if(map.containsKey(skuId)){
                returnMap.put(skuId, map.get(skuId));
            }else{
                returnMap.put(skuId, "0");
            }

        }
        end = System.currentTimeMillis();
        logger.info("为AMFEED产品库存赋值总共耗时："+(end-start)/1000+"秒");
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法结束！");
        return returnMap;
    }


}
