package com.shangpin.iog.logs;

import java.io.File;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.logs.schedule.AppContext;

/**
 * 2个定时任务：1>定时压缩每天的日志，然后删除源文件  2>定时删除n天前的日志
 * @author lubaijiang
 * @date 2016-09-26
 */
public class Start {
	
	private static Logger log = Logger.getLogger("info");
	
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args){
		loadSpringContext();
		log.info("---------------初始化成功------------------");
	}
}
