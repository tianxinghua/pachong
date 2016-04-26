/**
 * GIGLIO更新库存
 * Created by Kelseo on 2015/9/23.
 */
package com.shangpin.iog.dante5.stock;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dante5.dto.Item;
import com.shangpin.iog.dante5.dto.Rss;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;
@Component("dante5Stock")
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
    public Map<String, String> grabStock(Collection<String> skuNos) throws ServiceException {
        Map<String, String> skuStock = new HashMap<>(skuNos.size());
        Map<String, String> stockMap = new HashMap<>();

        OutTimeConfig timeConfig = new OutTimeConfig(1000*60, 1000*60*20,1000*60*20);
/*        timeConfig.confRequestOutTime(600000);
        timeConfig.confSocketOutTime(600000);*/
        String url = "https://www.dante5.com/en-US/home/feedShangpin";
        String result = HttpUtil45.get(url, timeConfig, null);
        System.out.println("success");
        HttpUtil45.closePool();

        if (result == null || "".equals(result)) {
            logger.error("从接口未取到数据. url : " + url);
            throw new ServiceException();
        }

        XmlParserCreator parserCreator = new XmlParserCreator() {
            @Override
            public XmlPullParser createParser() {
                try {
                    return XmlPullParserFactory.newInstance().newPullParser();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        GsonXml gsonXml = new GsonXmlBuilder()
                .setSameNameLists(true)
                .setXmlParserCreator(parserCreator)
                .create();

        Rss rss = gsonXml.fromXml(result, Rss.class);

        if (rss == null || rss.getChannel() == null) {
            return skuStock;
        }

        try {
            logger.info("拉取dante5数据开始");

//            Map<String, String> mongMap = new HashMap<>();
//            mongMap.put("supplierId", supplierId);
//            mongMap.put("supplierName", "giglio");
//            mongMap.put("result", "stream data.");
//            try {
//                logMongo.info(mongMap);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            for (Item item : rss.getChannel().getItem()) {
                if (item == null) {
                    continue;
                }
                String skuId = item.getId() + item.getSize(); //接口中g:id是spuId,对应不同尺码
                String stock = item.getAvailability();
                stockMap.put(skuId, stock);
            }

            for (String skuNo : skuNos) {
                if (stockMap.containsKey(skuNo)) {
                    skuStock.put(skuNo, stockMap.get(skuNo));
                } else {
                    skuStock.put(skuNo, "0");
                }
            }

            logger.info("dante5赋值库存数据成功");
            logger.info("拉取dante5数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("拉取dante5数据失败---" + e.getMessage());
            throw new ServiceMessageException("拉取dante5数据失败");
        }
        return skuStock;
    }

    public static void main(String[] args) throws Exception {

    	
    	 OutTimeConfig timeConfig = new OutTimeConfig(1000*60, 1000*60*20,1000*60*20);
    	 /*        timeConfig.confRequestOutTime(600000);
    	         timeConfig.confSocketOutTime(600000);*/
//    	         String url = "http://www.dante5.com/en-US/home/feedShangpin";
//    	         String result = HttpUtil45.get(url, timeConfig, null);
//    	         HttpUtil45.closePool();
    	//    	//加载spring
        loadSpringContext();
        GrabStockImp grabStockImp = (GrabStockImp)factory.getBean("dante5Stock");
        //AbsUpdateProductStock grabStockImp = new GrabStockImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("dante5更新数据库开始");
        try {
            grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
        } catch (Exception e) {
            loggerError.error("dante5 更新库存失败。");
            e.printStackTrace();
        }
        logger.info("dante5更新数据库结束");
        System.exit(0);
    }

}
