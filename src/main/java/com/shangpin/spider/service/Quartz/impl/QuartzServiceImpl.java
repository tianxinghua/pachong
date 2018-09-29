package com.shangpin.spider.service.Quartz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Triple;
import org.eclipse.jetty.util.StringUtil;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.spider.common.Constants;
import com.shangpin.spider.entity.base.Result;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.entity.gather.SpiderWhiteInfo;
import com.shangpin.spider.gather.spider.CommonSpider;
import com.shangpin.spider.gather.utils.PageUtil;
import com.shangpin.spider.mapper.gather.SpiderRulesMapper;
import com.shangpin.spider.mapper.gather.SpiderWhiteInfoMapper;
import com.shangpin.spider.mapper.quartz.QuartzInfoMapper;
import com.shangpin.spider.quartz.QuartzJob;
import com.shangpin.spider.quartz.QuartzManager;
import com.shangpin.spider.entity.quartz.QuartzInfo;
import com.shangpin.spider.redis.RedisManager;
import com.shangpin.spider.service.Quartz.QuartzService;

@Service
public class QuartzServiceImpl implements QuartzService{

	
	private  Logger log = LoggerFactory.getLogger(QuartzServiceImpl.class);
	@Autowired
	private  SpiderRulesMapper spiderRulesMapper;
	@Autowired
	private  SpiderWhiteInfoMapper spiderWhiteInfoMapper;
//	@Autowired
//	private  SpiderProxyInfoMapper spiderProxyInfoMapper;
	@Autowired
	private  QuartzInfoMapper quartzInfoMapper;
	@Autowired
    private  QuartzManager quartzManager;
	@Autowired
	private CommonSpider commonSpider;
	@Autowired
	private  RedisManager redisService;
	
	@Override
	public String createQuartz(String cronExpression, Long whiteId,
			Long spiderRuleId) {
		SpiderRules ruleInfo = spiderRulesMapper.getRuleById(spiderRuleId);
		/*
		 * 设置IP代理
		Integer proxyId = ruleInfo.getProxyId();
		if(proxyId!=0){
			SpiderProxyInfo proxyInfo = spiderProxyInfoMapper.getProxyInfoById(proxyId);
			ruleInfo.setProxyHost(proxyInfo.getProxyHost());
			ruleInfo.setProxyPort(proxyInfo.getProxyPort());
		}else{
			ruleInfo.setProxyHost("");
			ruleInfo.setProxyPort(0);
		}*/
		SpiderWhiteInfo whiteInfo = spiderWhiteInfoMapper.getWhiteById(whiteId);
		ruleInfo.setWhiteId(whiteId);
		ruleInfo.setWhiteName(whiteInfo.getName());
		ruleInfo.setSourceUrl(whiteInfo.getUrl());
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("spiderRuleInfo", ruleInfo);
//		data.put("quartzServiceImpl", this);
		String jobkeyName = String.valueOf("编号："+ruleInfo.getId()+"  名称："+whiteInfo.getName());
		
//		String uuid = GatherUtil.getFefferrerHost(whiteInfo.getUrl());
//		UUID改为源网站对应的ID，为了区分调度中的键，针对同一网站不同分类的问题
		String uuid = String.valueOf(whiteId);
		try {
			//开启任务时，先将redis中存在的源链接删除，否则爬虫不启动
			redisService.delWhiteUrl(whiteInfo.getUrl(),uuid);
			if(StringUtil.isNotBlank(ruleInfo.getFilterUrlReg())){
				//删除redis中的需要过滤的链接（包括列表页）
				redisService.delFilterUrl(ruleInfo.getFilterUrlReg(),uuid);
			}
			
//			redisService.delTaskuuid(uuid);
		} catch (Exception e) {
			log.error("----删除redis相关源缓存有误！"+e.getMessage());
		}
		/*
		 * 检查该任务是否已创建
		 */
		Triple<JobDetail,String,Trigger> triple = quartzManager.findInfo(JobKey.jobKey(jobkeyName, Constants.QUARTZ_JOB_GROUP_NAME));
		if(triple == null){
			quartzManager.addJob(jobkeyName, Constants.QUARTZ_JOB_GROUP_NAME,
	                String.valueOf(cronExpression) + "-" + ruleInfo.getId() + Constants.QUARTZ_TRIGGER_NAME_SUFFIX, Constants.QUARTZ_TRIGGER_GROUP_NAME
	                , QuartzJob.class, data, cronExpression);
			return Constants.SUCCESS;
		}else{
			return Constants.EXIST;
		}
	}
	
	@Override
	public void start(SpiderRules spiderRuleInfo) {
		log.info("----定时抓取开启 ");
		try {
			commonSpider.start(spiderRuleInfo);
		} catch (Exception e) {
			log.error("定时抓取出错"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public Result<QuartzInfo> getQuartzList(Integer page, Integer rows) {
		log.info("----定时任务列表");
		Result<QuartzInfo> result = new Result<QuartzInfo>();
		Long totalCount = quartzInfoMapper.getCount();
		Long totalPages = PageUtil.getPage(totalCount, rows);
		List<QuartzInfo> quartzInfoList = new ArrayList<QuartzInfo>();
		quartzInfoList = quartzInfoMapper.getQuartzList((page-1)*rows,rows);
		if(quartzInfoList!=null&&quartzInfoList.size()>0){
			result.setDataList(quartzInfoList);
			result.setCurrentPage(page);
			result.setTotalCount(totalCount);
			result.setTotalPages(totalPages);
			result.setMsg(Constants.SUCCESS);
			result.setStatus(Constants.SUCCESSCODE);
		}else{
			result.setDataList(null);
			result.setCurrentPage(1);
			result.setTotalCount(0L);
			result.setTotalPages(0L);
			result.setMsg(Constants.FAIL);
			result.setStatus(Constants.ERRORCODE);
		}
		return result;
		
		/*Set<JobKey> listAll = quartzManager.listAll(Constants.QUARTZ_JOB_GROUP_NAME);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for (JobKey jobKey : listAll) {
			String name = jobKey.getName();
			Triple<JobDetail, String, Trigger> triple = quartzManager.findInfo(jobKey);
			QuartzInfo quartzInfo = new QuartzInfo();
            Date previousFireTime = triple.getRight().getPreviousFireTime();
            Date startTime = triple.getRight().getStartTime();
            Date nextFireTime = triple.getRight().getNextFireTime();
            String state = Constants.stateMap.get(triple.getMiddle());
            String delete = "<button class='btn btn-primary delete' value='"+name+"'>删除抓取任务</button>";
            quartzInfo.setName(name);
            quartzInfo.setPreviousTime(format.format(previousFireTime));
            quartzInfo.setNextTime(format.format(nextFireTime));
            quartzInfo.setStartTime(format.format(startTime));
            quartzInfo.setState(state);
            quartzInfo.setDelete(delete);
            quartzInfoList.add(quartzInfo);
		}
		return quartzInfoList;*/
	}

	@Override
	public int deleteQuartz(JobKey jobkey) {
		try {
			log.info("----删除定时任务");
			quartzManager.delete(jobkey);
			return 1;
		} catch (Exception e) {
			log.error("----删除定时任务出错"+e.getMessage());
			return 0;
		}
	}

	@Override
	public int pauseQuartz(JobKey jobKey) {
		try {
			log.info("----暂停定时任务");
			quartzManager.pause(jobKey);
			return 1;
		} catch (Exception e) {
			log.error("----暂停定时任务出错"+e.getMessage());
			return 0;
		}
	}

	@Override
	public int resumeQuartz(JobKey jobKey) {
		try {
			log.info("----继续定时任务");
			quartzManager.resume(jobKey);
			return 1;
		} catch (Exception e) {
			log.error("----继续定时任务出错"+e.getMessage());
			return 0;
		}
	}

}
