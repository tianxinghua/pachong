package com.shangpin.iog.styleside.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Created by lizhongren on 2017/2/22.
 */
@Component("thestyleside")
public class FetchStockImpl extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    ProductFetchUtil  productFetchUtil;
    @Override
    public Map<String, String> grabStock(Collection<String> skuNos) throws ServiceException, Exception {
        //定义返回map 结果集
        Map<String, String> spStockMap =null;
        try {
            logger.info("  ============开始拉取 thestyleside 库存信息========================");
            spStockMap = productFetchUtil.getProductStock(skuNos);
            /**
             * 判断供应商接口没有提供的商品库存信息，没有提供库存数量置0
             */
            for (String skuno : skuNos) {
                if (!spStockMap.containsKey(skuno)) {
                    logger.info("==拉取库存结果中不存在skuno："+skuno+" !!!!!!!!!!!!!!");
                    spStockMap.put(skuno, "0");
                    logger.info("== skuno:"+skuno+"  置0  !!!!!!!!!!!!!!");
                }
            }
        } catch (Exception e) {
            throw new ServiceMessageException("拉取thestyleside 库存数据失败");
        }
        logger.info(" thestyleside 赋值库存数据成功");
        logger.info("======spStockMap.size():"+spStockMap.size()+"===========");
        return spStockMap;
    }
}
