/**
 * 
 */
package com.shangpin.iog.app;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;


@Configuration
@ComponentScan(basePackages="com.shangpin.iog",
excludeFilters={
		@ComponentScan.Filter(type= FilterType.ANNOTATION,value=Controller.class)
})
@ImportResource("classpath:project-mybatis.xml")
@Import(com.shangpin.iog.mongobase.MongodbConfiguration.class)
public class AppContext {


}
