package com.shangpin.iog.dellogliostore.service;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dellogliostore.dto.Feed;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Component("dellogliostore")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    public void fetchProductAndSave(String url) {

        String supplierId = "2015092501047";
        try {
            Map<String, String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(600000);
            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get(url, timeConfig, null);

            Feed feed = ObjectXMLUtil.xml2Obj(Feed.class, result);
            System.out.println(feed);

//            if (feed == null || feed.getItems() == null) {
//                return;
//            }
//
//            int count = 0;
//            for (SpuItem spuItem : feed.getItems()) {
//                if (spuItem == null) {
//                    continue;
//                }
//                System.out.println("count : " + ++count);
//                System.out.println("spuItem : " + spuItem);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
