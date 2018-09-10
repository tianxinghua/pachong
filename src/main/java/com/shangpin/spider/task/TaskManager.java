package com.shangpin.spider.task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.spider.common.Constants;
import com.shangpin.spider.entity.gather.SpiderTaskInfo;
import com.shangpin.spider.gather.downloader.YcmWebDriverPool;
import com.shangpin.spider.gather.spider.CommonSpider.MySpider;
import com.shangpin.spider.redis.RedisManager;



/** 
 * @author  njt 
 * @date 创建时间：2017年12月5日 上午11:37:58 
 * @version 1.0 
 * @parameter  
 */
@Component
public class TaskManager {
	private Logger log = LoggerFactory.getLogger(TaskManager.class);
	private Map<String, MySpider> taskMap = new LinkedHashMap<>();
	
	@Autowired
	private RedisManager redisService;
	
	/**
	 * 初始化爬虫
	 * @param whiteName
	 * @param spider
	 */
	public void init(String whiteName,MySpider spider){
		log.info("-----初始化爬虫:"+whiteName);
		taskMap.put(whiteName, spider);
	}
	
	/**
	 * 
	 */
	public void deleteTask(String whiteName){
		if(taskMap.containsKey(whiteName)){
			taskMap.remove(whiteName);
			log.info("-----删除爬虫任务:"+whiteName);
		}
	}
	/**
	 * 爬虫列表
	 * @return
	 */
	public List<SpiderTaskInfo> taskList(){
		log.info("-----爬虫列表");
		List<SpiderTaskInfo> spiderTaskList = new ArrayList<SpiderTaskInfo>();
		Set<String> keySet = taskMap.keySet();
		if(keySet.size()>0){
			for (String key : keySet) {
				MySpider spider = taskMap.get(key);
				SpiderTaskInfo spiderTaskInfo = new SpiderTaskInfo();
				//spider状态序数
				int ordinal = spider.getStatus().ordinal();
				spiderTaskInfo.setId(spider.getUUID());
				spiderTaskInfo.setName(key);
				spiderTaskInfo.setCount(spider.getPageCount());
				spiderTaskInfo.setState(Constants.taskStateMap.get(ordinal));
				spiderTaskInfo.setStartTime(spider.getStartTime());
				spiderTaskInfo.setOrdinal(ordinal);
				spiderTaskList.add(spiderTaskInfo);
			}
		}
		return spiderTaskList;
	}
	/**
	 * 开启或关闭爬虫
	 */
	public void edit(String uuid,String name){
		MySpider spider = taskMap.get(name);
		if(spider.getUUID().equals(uuid)){
			//根据爬虫状态判断开启或是关闭
			int ordinal = spider.getStatus().ordinal();
			if(ordinal == 1){
				log.info("-----关闭爬虫:"+uuid+" name:"+name);
				spider.stop();
				if (spider.getSpiderRuleInfo().getAjaxFlag()) {
					YcmWebDriverPool pool = spider.getPool();
					// 停止phantomjs
					pool.shutdownEnd();
				}
			}else if(ordinal == 2){
				log.info("-----开启爬虫:"+uuid+" name:"+name);
				//开启前清除redis中的源链接
				String url = spider.getSpiderRuleInfo().getSourceUrl();
				redisService.delWhiteUrl(url,uuid);
				if(StringUtil.isNotBlank(spider.getSpiderRuleInfo().getFilterUrlReg())){
					//删除redis中的需要过滤的链接（包括列表页）
					redisService.delFilterUrl(spider.getSpiderRuleInfo().getFilterUrlReg(),uuid);
				}
				spider.start();
			}
			
		}
	}
	/**
	 * 强制关闭爬虫
	 */
	public void forceQuit(String uuid,String name){
		MySpider spider = taskMap.get(name);
		if(spider.getUUID().equals(uuid)){
				log.info("-----强制关闭爬虫:"+uuid+" name:"+name);
				spider.stop();
				if (spider.getSpiderRuleInfo().getAjaxFlag()) {
					YcmWebDriverPool pool = spider.getPool();
					// 停止phantomjs
					pool.shutdownEnd();
				}
		}
	}
	
}
