/**
 * 
 */
package com.shang.iog.facade.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;


/**

 */

@Configuration

@ComponentScan(basePackages={"com.shangpin.iog"}
)
@ImportResource("classpath:dubbo.xml")
@Import({com.shangpin.iog.app.AppContext.class})

public class AppContext {

	final static Logger log = LoggerFactory.getLogger(AppContext.class);


}
