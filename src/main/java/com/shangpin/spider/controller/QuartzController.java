package com.shangpin.spider.controller;

import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.shangpin.spider.common.Constants;
import com.shangpin.spider.entity.base.Result;
import com.shangpin.spider.entity.quartz.QuartzInfo;
import com.shangpin.spider.service.Quartz.QuartzService;
import com.shangpin.spider.utils.shiro.ContentRole;



/** 
 * @author  njt 
 * @date 创建时间：2017年11月17日 下午2:39:59 
 * @version 1.0 
 * @parameter  
 */
@Controller
@RequestMapping("/quartz")
public class QuartzController {
	private Logger log = LoggerFactory.getLogger(QuartzController.class);
	@Autowired
	private QuartzService quartzService;
	
	/**
	 * 创建定时任务
	 * @param quartzTime
	 * @param whiteId
	 * @param spiderRuleId
	 * @return
	 */
	@RequestMapping("create")
	@ResponseBody
	public String createQuartz(String cronExpression,Long whiteId,Long spiderRuleId){
		log.info("创建定时任务"+cronExpression+"  "+whiteId+"  "+spiderRuleId);
		try {
			String msg = quartzService.createQuartz(cronExpression,whiteId,spiderRuleId);
			return msg;
		} catch (Exception e) {
			log.info("创建定时任务出错："+e.getMessage());
			return Constants.FAIL;
		}
	}
	
	/**
	 * 跳转quartz列表
	 * @return
	 */
	@RequestMapping("toQuartzList")
	public String toQuartzList(Model model){
//		控制按钮的权限使用
		JSONArray roleArray = ContentRole.queryRole();
		model.addAttribute("roleArray", roleArray);
		return "spider/quartzList";
	}
	/**
	 * 获取quartz列表
	 * @return
	 */
	@RequestMapping("getQuartzList")
	@ResponseBody
	public Result<QuartzInfo> getQuartzList(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer rows){
		Result<QuartzInfo> result = quartzService.getQuartzList(page ,rows);
		return result;
	}

	/**
	 * 暂停定时任务
	 */
	@RequestMapping("pauseQuartz")
	@ResponseBody
	public int pauseQuartz(String jobkeyName){
		JobKey jobKey = new JobKey(jobkeyName,Constants.QUARTZ_JOB_GROUP_NAME);
		int result = quartzService.pauseQuartz(jobKey);
		return result;
	}
	
	/**
	 * 继续定时任务
	 */
	@RequestMapping("resumeQuartz")
	@ResponseBody
	public int resumeQuartz(String jobkeyName){
		JobKey jobKey = new JobKey(jobkeyName,Constants.QUARTZ_JOB_GROUP_NAME);
		int result = quartzService.resumeQuartz(jobKey);
		return result;
	}	
	
	/**
	 * 删除定时任务
	 */
	@RequestMapping("deleteQuartz")
	@ResponseBody
	public int deleteQuartz(String jobkeyName){
		JobKey jobKey = new JobKey(jobkeyName,Constants.QUARTZ_JOB_GROUP_NAME);
		int result = quartzService.deleteQuartz(jobKey);
		return result;
	}
	
}
