package com.shangpin.iog.itemInfo_purchase.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 */

@Configuration

@ComponentScan(basePackages={"com.shangpin.iog"}
)
@EnableScheduling
@Import({com.shangpin.iog.app.AppContext.class})


public class AppContext {
    final static Logger log = LoggerFactory.getLogger(AppContext.class);




}

