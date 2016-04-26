/**
 * GIGLIO更新库存
 * Created by Kelseo on 2015/9/23.
 */
package com.shangpin.iog.dellogliostore.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dellogliostore.dto.Feed;
import com.shangpin.iog.dellogliostore.dto.SkuItem;
import com.shangpin.iog.dellogliostore.dto.SkuItems;
import com.shangpin.iog.dellogliostore.dto.SpuItem;
import com.shangpin.iog.dellogliostore.stock.schedule.AppContext;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
@Component("delloglioStore")
public class GrabStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    private static ResourceBundle bdl = null;
    private static String supplierId;

    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    public Map<String, Integer> grabStock(Collection<String> skuNos) throws ServiceException {
        Map<String, Integer> skuStock = new HashMap<>(skuNos.size());
        Map<String, Integer> stockMap = new HashMap<>();

        try {
            logger.info("拉取dellogliostore数据开始");

//            Map<String, String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*60*10, 1000*60*60,1000*60*60);
            String result = HttpUtil45.get("http://www.dellogliostore.com/admin/temp/xi125.xml", timeConfig, null);
            logger.info("result==="+result);
            HttpUtil45.closePool();

//            mongMap.put("supplierId", supplierId);
//            mongMap.put("supplierName", "giglio");
//            mongMap.put("result", "stream data.");
//            try {
//                logMongo.info(mongMap);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            //Remove BOM from String
            if (result != null && !"".equals(result)) {
                result = result.replace("\uFEFF", "");
            }

            Feed feed = ObjectXMLUtil.xml2Obj(Feed.class, result);
            
            logger.info("从供货商拉取的信息feed大小==========="+feed.getSpuItems().getItems().size()); 

            if (feed == null || feed.getSpuItems() == null) {
                return stockMap;
            }

            for (SpuItem spuItem : feed.getSpuItems().getItems()) {
                if (spuItem == null) {
                    continue;
                }

                String spuId = spuItem.getSpuId();

                SkuItems skuItems = spuItem.getSkuItems();
                if (skuItems == null || skuItems.getSkuItems() == null) {
                    continue;
                }

                for (SkuItem skuItem : skuItems.getSkuItems()) {
                    String skuId = spuId + skuItem.getSize();
                    stockMap.put(skuId, Integer.parseInt(skuItem.getStock()));
                }

                for (String skuNo : skuNos) {
                    if (stockMap.containsKey(skuNo)) {
                        skuStock.put(skuNo, stockMap.get(skuNo));
                    } else {
                        skuStock.put(skuNo, 0);
                    }
                }
            }

            logger.info("dellogliostore赋值库存数据成功");
            logger.info("拉取dellogliostore数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("拉取dellogliostore数据失败---" + e.getMessage(),e);
            throw new ServiceMessageException("拉取dellogliostore数据失败");
        }
        logger.info("返回的map的大小是======="+skuStock.size());
        return skuStock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
//        GrabStockImp grabStockImp = (GrabStockImp)factory.getBean("delloglioStore");
//        //AbsUpdateProductStock grabStockImp = new GrabStockImp();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("dellogliostore更新数据库开始");
//        try {
//        	grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//		} catch (Exception e) {
//			loggerError.error("dellogliostore更新库存失败");
//			e.printStackTrace();
//		}
//        logger.info("dellogliostore更新数据库结束");
//        System.exit(0);
    }

}
