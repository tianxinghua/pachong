package com.shangpin.logtemplate.schedule;


import com.shangpin.logger.LoggerUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by lizhongren on 2016/2/25.
 */
@Component
public class LoggerServiceImpl {
//    private static LoggerUtil log = LoggerUtil.getLogger("error");
//     private static Log log = LogFactory.getLog(LoggerServiceImpl.class);
    public  Logger log = Logger.getLogger(LoggerServiceImpl.class);

    @Autowired
    TestImpl testImpl;
    public void printLog(){

        TestImpl test = new TestImpl();

        try {
            for(int i=0;i<10;i++){
                log.error("i="+i);
                System.out.println(testImpl.test());
                System.out.println(test.test());
                if(i==(int)(Math.random() * 10)){
                    int j=2/0;
                }
            }
            int j=2/0;
        } catch (Exception e) {
            log.error("exception ="+e.getMessage(),e);
        }

    }

}
