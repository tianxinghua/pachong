package com.shangpin.iog.reebonz.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lizhongren on 2017/2/22.
 */
@Component("redi")
public class FetchStockImpl extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    ProductFetchUtil  productFetchUtil;
    @Override
    public Map<String, String> grabStock(Collection<String> skuNos) throws ServiceException, Exception {
        Map<String, String> spStockMap = new HashMap<>(skuNos.size());
        Map<String, String> supplierStockMap = new HashMap<>();

        try {
            productFetchUtil.getProductStock(supplierStockMap);
        } catch (Exception e) {

            throw new ServiceMessageException("拉取stefaniamode数据失败");

        }

        for (String skuno : skuNos) {

            if (supplierStockMap.containsKey(skuno)) {
                spStockMap.put(skuno, supplierStockMap.get(skuno));
            } else {
                spStockMap.put(skuno, "0");
            }
        }

        logger.info("stefaniamode赋值库存数据成功");
        return spStockMap;
    }
}
