/**
 * GIGLIO更新库存
 * Created by Kelseo on 2015/9/23.
 */
package com.shangpin.iog.dellogliostore.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class GrabStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl = null;
    private static String supplierId;

    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    public Map<String, String> grabStock(Collection<String> skuNos) throws ServiceException {
        Map<String, String> skuStock = new HashMap<>(skuNos.size());
        Map<String, String> stockMap = new HashMap<>();

        try {
            logger.info("拉取GIGLIO数据开始");

//            Map<String, String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(600000);
            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get("http://www.giglio.com/feeds/shangpin.csv", timeConfig, null);
            HttpUtil45.closePool();

//            mongMap.put("supplierId", supplierId);
//            mongMap.put("supplierName", "giglio");
//            mongMap.put("result", "stream data.");
//            try {
//                logMongo.info(mongMap);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            logger.info("dellogliostore赋值库存数据成功");
            logger.info("拉取dellogliostore数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("拉取dellogliostore数据失败---" + e.getMessage());
            throw new ServiceMessageException("拉取dellogliostore数据失败");
        }
        return skuStock;
    }

    public static void main(String[] args) throws Exception {
        AbsUpdateProductStock grabStockImp = new GrabStockImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("dellogliostore更新数据库开始");
        grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
        logger.info("dellogliostore更新数据库结束");
        System.exit(0);
    }

}
