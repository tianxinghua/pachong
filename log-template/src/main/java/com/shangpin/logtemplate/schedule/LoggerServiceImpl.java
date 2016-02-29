package com.shangpin.logtemplate.schedule;


import com.shangpin.logger.LoggerUtil;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by lizhongren on 2016/2/25.
 */
@Component
public class LoggerServiceImpl {
    private static LoggerUtil logError = LoggerUtil.getLogger("error");


    public void printLog(){



        try {
            for(int i=0;i<10;i++){
                logError.error("i="+i);
                if(i==(int)(Math.random() * 10)){
                    int j=2/0;
                }
            }
            int j=2/0;
        } catch (Exception e) {
            logError.error("exception ="+e.getMessage());
        }

    }

}
