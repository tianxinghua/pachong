package com.shangpin.iog.opticalscribe;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.opticalscribe.service.GucciProductCN;
import com.shangpin.iog.opticalscribe.service.GucciProductIT;
import com.shangpin.iog.opticalscribe.service.LouisvuittonCN;
import com.shangpin.iog.opticalscribe.tools.CSVUtilsSolveRepeatData;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by zhaogenchun on 2015/9/25.
 */
public class StartSkuJobCN {

    private static Logger logger = Logger.getLogger("info");

    private static ResourceBundle bdl = null;

    private static String path ="",flag="",destFilePath="";

    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");

        path = bdl.getString("path-cn");
        flag = bdl.getString("flag-cn");
        destFilePath = bdl.getString("destFilePath-cn");
    }

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    public static void main(String[] args){
        loadSpringContext();
        //鎷夊彇鏁版嵁
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long start = System.currentTimeMillis();
        System.out.println("-------cn fetch start:"+sim.format(new Date())+"---------");
        logger.info("-------cn fetch start:"+sim.format(new Date())+"---------");

        LouisvuittonCN fetchProduct =(LouisvuittonCN)factory.getBean("louisvuittonCN");
        try {
            fetchProduct.getUrlList();
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("-------cn fetch end:"+sim.format(new Date())+"---------");
        System.out.println("总用时："+(System.currentTimeMillis()-start)/1000*60+"s");
        logger.info("-------cn fetch end:"+sim.format(new Date())+"---------");
        logger.info("总用时："+(System.currentTimeMillis()-start)/1000*60 +"s");
        System.exit(0);
    }
    
}
