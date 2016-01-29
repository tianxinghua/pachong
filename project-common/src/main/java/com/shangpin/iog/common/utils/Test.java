package com.shangpin.iog.common.utils;

import com.shangpin.iog.common.utils.logger.LoggerUtil;
import org.apache.log4j.Logger;

/**
 * Created by lizhongren on 2016/1/29.
 */
public class Test {

    private static LoggerUtil logger = LoggerUtil.getLogger("info");
    public static void main(String[] args) throws Exception{

        int i = 10/2;
        logger.info("i="+i);

        try {
            int j=2/0;
        } catch (Exception e) {
            logger.info("exception ="+e.getMessage());
        }


    }
}
