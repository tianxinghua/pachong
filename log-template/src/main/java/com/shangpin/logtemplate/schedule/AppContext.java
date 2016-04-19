package com.shangpin.logtemplate.schedule;

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

@ComponentScan(basePackages={"com.shangpin"}
)
@EnableScheduling

public class AppContext {
	
    final static Logger log = LoggerFactory.getLogger(AppContext.class);
}

