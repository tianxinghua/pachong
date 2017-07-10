package com.shangpin.iog.parisi.service;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhongren on 2017/2/22.
 */
@Component("parisi")
public class FetchStockImpl extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    ProductFetchUtil  productFetchUtil;


    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> stock_map = new HashMap<String, String>();

        String url="",stockXml="",stock;
        OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60,1000*60,1000*60);
        for (String skuno : skuNo) {

            stock_map.put(skuno, "0");


            try {
                stockXml = productFetchUtil.getProductStock(skuno);
                logger.debug("get skuNo:"+ skuno + " result :" + stockXml);
                stock  = productFetchUtil.convertStock(stockXml);
                logger.info("get skuNo:"+ skuno + " stock :" + stock);

                stock_map.put(skuno,stock);

            } catch (Exception e) {

                loggerError.error("拉取失败 "+e.getMessage());
                e.printStackTrace();
                continue;
            }




        }

        logger.info("返回的map大小  stock_map.size======"+stock_map.size());
        return stock_map;
    }
}
