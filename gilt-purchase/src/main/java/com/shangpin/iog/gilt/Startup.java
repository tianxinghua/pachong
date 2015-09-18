package com.shangpin.iog.gilt;

import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.gilt.order.OrderServiceImpl;
import com.shangpin.iog.gilt.schedule.AppContext;
import com.shangpin.iog.gilt.service.FetchProduct;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by sunny on 2015/8/5.
 */
public class Startup {

    private static   Logger logger = LoggerFactory.getLogger(Startup.class);




    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args)
    {
        //加载spring
        loadSpringContext();
        logger.info(" schedule start  ");

    }






}
