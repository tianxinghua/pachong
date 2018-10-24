package com.shangpin.spider.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.shangpin.spider.common.Constants;
import com.shangpin.spider.entity.gather.RedisCache;
import com.shangpin.spider.entity.gather.SpiderWhiteInfo;
import com.shangpin.spider.mapper.gather.SpiderWhiteInfoMapper;

/**
 * @author njt
 * @date 创建时间：2017年12月5日 下午4:50:18
 * @version 1.0
 * @parameter
 */
@Component
public class RedisManager {
	private Logger log = LoggerFactory.getLogger(RedisManager.class);
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private SpiderWhiteInfoMapper spiderWhiteInfoMapper;
	
	/**
	 * 删除redis中缓存的白名单链接
	 * @param url
	 * @param uuid
	 */
	public void delWhiteUrl(String url, String uuid) {
		Boolean flag = redisTemplate.hasKey(Constants.TASKUUID+uuid);
		ZSetOperations<String, String> zoper = redisTemplate.opsForZSet();
		if (flag) {
			zoper.remove(Constants.TASKUUID+uuid, url);
			log.info("---删除TASKUUID中的源链接：" + url);
		}
		Boolean flag2 = redisTemplate.hasKey(Constants.REMTASKUUID+uuid);
		if (flag2) {
			zoper.remove(Constants.REMTASKUUID+uuid, url);
			log.info("---删除REMTASKUUID中的源链接：" + url);
		}
	}
	
	/**
	 * 删除taskuuid中所有的链接
	 * @param uuid
	 */
	public void delTaskuuid(String uuid) {
		Boolean flag = redisTemplate.hasKey(Constants.TASKUUID+uuid);
		ZSetOperations<String, String> zoper = redisTemplate.opsForZSet();
		if (flag) {
			zoper.removeRange(Constants.TASKUUID+uuid, 0, -1);
			log.info("---删除"+Constants.TASKUUID+uuid+"中的残留链接");
		}

	}
	
	/**
	 * 删除REMTASKUUID中包含的过滤链接
	 * @param filterUrlReg
	 * @param uuid
	 */
	public void delFilterUrl(String filterUrlReg, String uuid) {
		Boolean flag = redisTemplate.hasKey(Constants.REMTASKUUID+uuid);
		ZSetOperations<String, String> zoper = redisTemplate.opsForZSet();
		if (flag) {
			Set<String> set = zoper.range(Constants.REMTASKUUID+uuid, 0, -1);
			if(set.size()>0){
				Pattern compile = Pattern.compile(filterUrlReg);
				for (String url : set) {
					Matcher matcher = compile.matcher(url);
					if(matcher.find()){
						String filterUrl = matcher.group();
						zoper.remove(Constants.REMTASKUUID+uuid, filterUrl);
						log.info("---删除REMTASKUUID中包含的过滤链接:"+filterUrl);
					}
					
				}
			}
		}
		
	}
	
	/**
	 * 获取所有redis中包括TASKUUID和REMTASKUUID的键及其数据信息
	 * @return 
	 */
	public List<RedisCache> getRedisList(){
		List<RedisCache> redisCacheList = new ArrayList<RedisCache>();
		String reg = Constants.TASKUUID+"*";
		String regRem = Constants.REMTASKUUID+"*";
		Set<String> keys = redisTemplate.keys(reg);
		Set<String> remKeys = redisTemplate.keys(regRem);
		Set<String> uuidSet = new HashSet<String>();
		for (String key : keys) {
			String uuid = key.replace(Constants.TASKUUID, "");
			uuidSet.add(uuid);
		}
		for (String remKey : remKeys) {
			String uuid = remKey.replace(Constants.REMTASKUUID, "");
			uuidSet.add(uuid);
		}
		if(uuidSet.size()>0){
			Map<String, String> domainMap = getDomainMap();
			ZSetOperations<String, String> zoper = redisTemplate.opsForZSet();
			for (String uuid : uuidSet) {
				RedisCache redisCache = new RedisCache();
				String webName = "无";
				if(domainMap!=null&&domainMap.size()>0){
					webName = domainMap.get(uuid);
				}
				Boolean taskFlag = redisTemplate.hasKey(Constants.TASKUUID+uuid);
				int taskCount = 0;
				if(taskFlag){
					taskCount = zoper.zCard(Constants.TASKUUID+uuid).intValue();
				}
				Boolean remTaskFlag = redisTemplate.hasKey(Constants.REMTASKUUID+uuid);
				int remTaskCount = 0;
				if(remTaskFlag){
					remTaskCount = zoper.zCard(Constants.REMTASKUUID+uuid).intValue();
				}
				
				Boolean errorTaskFlag = redisTemplate.hasKey(Constants.ERRORTASKUUID+uuid);
				int errorTaskCount = 0;
				if(errorTaskFlag){
					errorTaskCount = zoper.zCard(Constants.ERRORTASKUUID+uuid).intValue();
				}
				redisCache.setUuid(uuid);
				redisCache.setWebName(webName);
				redisCache.setTaskCount(taskCount);
				redisCache.setRemTaskCount(remTaskCount);
				redisCache.setErrorTaskCount(errorTaskCount);
				redisCacheList.add(redisCache);
			}
			
		}
		return redisCacheList;
	}
	
	/**
	 * 获取whiteInfo的域名和名字对应关系
	 * @return 
	 */
	public Map<String, String> getDomainMap(){
		log.info("获取域名和名字的map");
		Map<String,String> domainMap = null;
		try {
			List<SpiderWhiteInfo> whiteInfoList = spiderWhiteInfoMapper.getwhiteInfo();
			if(whiteInfoList!=null&&whiteInfoList.size()>0){
				domainMap = new HashMap<String,String>();
				for (SpiderWhiteInfo spiderWhiteInfo : whiteInfoList) {
//					String domain = GatherUtil.getFefferrerHost(spiderWhiteInfo.getUrl());
					String domain = spiderWhiteInfo.getId().toString();
					String name = spiderWhiteInfo.getName();
					if(domainMap.containsKey(domain)){
						String nameOld = domainMap.get(domain);
						String nameNew = nameOld+"\n"+name;
						domainMap.put(domain, nameNew);
					}else{
						domainMap.put(domain, name);
					}
				}
			}
		} catch (Exception e) {
			log.info("获取域名和名字的map出错！"+e.getMessage());
		}
		return domainMap;
	}
	
	/**
	 * 链接抓取成功后的处理
	 * @param uuid
	 * @param url
	 */
	public void successHandleRedis(Long uuid, String url) {
//		成功后将链接放入REMTASKUUID中，并删除ERRORTASKUUID中对应的链接
		ZSetOperations<String, String> zoper = redisTemplate.opsForZSet();
		Long rank = zoper.rank(Constants.ERRORTASKUUID+uuid, url);
		Double score = Double.valueOf(0);
		if(rank!=null) {
			score = zoper.score(Constants.ERRORTASKUUID+uuid, url);
			zoper.remove(Constants.ERRORTASKUUID+uuid, url);
		}
		zoper.add(Constants.REMTASKUUID+uuid, url, score);
	}
	
	/**
	 * 链接抓取失败后的处理
	 * @param uuid
	 * @param url
	 * @param retryNum
	 */
	public void errorHandleRedis(Long uuid, String urlFlag, Integer retryNum) {
//		链接失败后，判断失败次数（优先级即失败次数）
		ZSetOperations<String, String> zoper = redisTemplate.opsForZSet();
		Long rank = zoper.rank(Constants.ERRORTASKUUID+uuid, urlFlag);
		Double score = Double.valueOf(0);
		if(rank!=null) {
			score = zoper.score(Constants.ERRORTASKUUID+uuid, urlFlag);
		}
//		zoper.incrementScore(Constants.ERRORTASKUUID+uuid, url, 1);
		if(score<retryNum) {
			zoper.remove(Constants.ERRORTASKUUID+uuid, urlFlag);
			String url = urlFlag;
			if(urlFlag.contains("#AND")) {
				url = urlFlag.substring(0, urlFlag.indexOf("#AND"));
			}
			score++;
			zoper.add(Constants.TASKUUID+uuid, url, score);
			zoper.add(Constants.ERRORTASKUUID+uuid, urlFlag, score);
		}
	}
	
}
