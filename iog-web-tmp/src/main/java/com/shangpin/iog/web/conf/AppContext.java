/**
 * 
 */
package com.shangpin.iog.web.conf;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;


@Configuration
@ComponentScan(basePackages="com.shangpin.iog",
excludeFilters={
		@ComponentScan.Filter(type= FilterType.ANNOTATION,value=Controller.class)
})
public class AppContext {


}
