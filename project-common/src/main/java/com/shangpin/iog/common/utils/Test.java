package com.shangpin.iog.common.utils;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import org.apache.log4j.Logger;

/**
 * Created by lizhongren on 2016/1/29.
 */
public class Test {

    private static LoggerUtil logger = LoggerUtil.getLogger("info");
    public static void main(String[] args) throws Exception{

//        int i = 10/2;
//        logger.info("i="+i);
//
//        try {
//            int j=2/0;
//        } catch (Exception e) {
//            logger.info("exception ="+e.getMessage());
//        }
//           System.out.println( "201602161167427".compareTo("201602161167427"));
        HttpUtil45.get("http://www.pos123.us/api/v2/products/.json?limit=50&page=2",new OutTimeConfig(),
                null,"RIX5NkHDIM25yUFZmDlVSdWEE7V3aSYv","");
    }
}
