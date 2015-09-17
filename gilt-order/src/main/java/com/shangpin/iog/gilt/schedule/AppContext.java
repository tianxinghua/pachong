/**
 * 
 */
package com.shangpin.iog.gilt.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description 父上下文，主要扫描服务层，数据库成，缓存
 * @author 陈小峰
 * <br/>2015年1月24日
 */

@Configuration

@ComponentScan(basePackages={"com.shangpin.shop.iog"}
)
@EnableScheduling
@Import({com.shangpin.iog.app.AppContext.class})


public class AppContext {
	final static Logger log = LoggerFactory.getLogger(AppContext.class);




}
