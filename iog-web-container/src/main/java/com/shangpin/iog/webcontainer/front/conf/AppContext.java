/**
 * 
 */
package com.shangpin.iog.webcontainer.front.conf;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;



@Configuration
@ComponentScan(basePackages="com.shangpin.iog",
excludeFilters={
		@ComponentScan.Filter(type= FilterType.ANNOTATION,value=Controller.class)
})
@ImportResource("classpath:project-mybatis.xml")

public class AppContext {


}
