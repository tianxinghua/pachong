package com.shangpin.spider.service.Quartz;

import org.quartz.JobKey;

import com.shangpin.spider.entity.base.Result;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.entity.quartz.QuartzInfo;

public interface QuartzService {

	String createQuartz(String cronExpression, Long whiteId, Long spiderRuleId);
	
	/**
	 * 获取任务列表
	 * @param rows 
	 * @param page 
	 * @return
	 */
	public Result<QuartzInfo> getQuartzList(Integer page, Integer rows);
	/**
	 * 暂停定时任务
	 * @param jobkey
	 * @return
	 */
	public int pauseQuartz(JobKey jobKey);
	/**
	 * 继续定时任务
	 * @param jobkey
	 * @return
	 */
	public int resumeQuartz(JobKey jobKey);
	/**
	 * 删除定时任务
	 * @param jobkey
	 * @return
	 */
	public int deleteQuartz(JobKey jobkey);
	/**
	 * 开始爬取
	 * @param spiderRuleInfo
	 */
	public void start(SpiderRules spiderRuleInfo);
	
}
