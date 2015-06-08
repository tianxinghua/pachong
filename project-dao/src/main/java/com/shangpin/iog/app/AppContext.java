/**
 * 
 */
package com.shangpin.iog.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;


@Configuration
@ComponentScan(basePackages="com.shangpin.iog",
excludeFilters={
		@ComponentScan.Filter(type= FilterType.ANNOTATION,value=Controller.class)
})
@ImportResource("classpath:project-mybatis.xml")

public class AppContext {


}
