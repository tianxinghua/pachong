package com.shangpin.spider.quartz;

import java.io.Serializable;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.shangpin.spider.config.SpringContextHolder;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.service.Quartz.QuartzService;

/*import com.xr.config.SpringContextHolder;
import com.xr.gather.api.QuartzService;
import com.xr.gather.model.SpiderRuleInfo;*/

/** 
 * @author  njt 
 * @date 创建时间：2017年11月22日 上午11:35:07 
 * @version 1.0 
 * @parameter  
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QuartzJob extends QuartzJobBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(QuartzJob.class);
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		JobDataMap dataMap = arg0.getJobDetail().getJobDataMap();
		SpiderRules spiderRuleInfo = null;
		spiderRuleInfo = (SpiderRules) SerializableUtil.objToClass(dataMap.get("spiderRuleInfo"), spiderRuleInfo);
        
        QuartzService quartzService =  SpringContextHolder.getBean(QuartzService.class);
        
		log.info("定时抓取开始");
		quartzService.start(spiderRuleInfo);
//		log.info("定时抓取结束");
	}

}
