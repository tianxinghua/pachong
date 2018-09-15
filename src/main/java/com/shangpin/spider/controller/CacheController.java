package com.shangpin.spider.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangpin.spider.entity.gather.RedisCache;
import com.shangpin.spider.redis.RedisManager;


/** 
 * @author  njt 
 * @date 创建时间：2017年12月8日 上午10:23:51 
 * @version 1.0 
 * @parameter  
 */
@Controller
@RequestMapping("cache")
public class CacheController {
	private Logger log = LoggerFactory.getLogger(CacheController.class);
	@Autowired
	private RedisManager redisManager;
	
	@RequestMapping("toCacheList")
	public String toCacheList(){
		log.info("----跳转缓存信息列表");
		return "spider/cacheList";
	}
	
	@RequestMapping("getCacheList")
	@ResponseBody
	public List<RedisCache> getCacheList(){
		log.info("----获取缓存信息列表");
		List<RedisCache> redisCacheList = null;
		try {
			redisCacheList = redisManager.getRedisList();
		} catch (Exception e) {
			log.error("----获取缓存信息列表出错！"+e.getMessage());
		}
		return redisCacheList;
	}
	
	@RequestMapping("delTaskuuid")
	@ResponseBody
	public int delTaskuuid(String uuid){
		log.info("----删除redis缓存uuid："+uuid);
		try {
			redisManager.delTaskuuid(uuid);
			return 1;
		} catch (Exception e) {
			log.info("----删除redis缓存uuid："+uuid+"，出错---"+e.getMessage());
			return 0;
		}
		
	}
	
}
