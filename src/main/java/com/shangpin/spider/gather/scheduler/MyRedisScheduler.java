package com.shangpin.spider.gather.scheduler;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.shangpin.spider.common.Constants;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

/**
 * @author njt
 * @date 创建时间：2017年10月30日 下午2:10:50
 * @version 1.0
 * @parameter
 */
@Service
public class MyRedisScheduler implements Scheduler {

	private static Logger log = Logger.getLogger(MyRedisScheduler.class);

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Override
	public void push(Request request, Task task) {
		try {
			ZSetOperations<String, String> zoper = redisTemplate.opsForZSet();
				String url = request.getUrl();
				String uuid = task.getUUID();
				Boolean keyFlag = redisTemplate.hasKey(Constants.REMTASKUUID+uuid);
				if (keyFlag) {
					Set<String> remSet = zoper.range(Constants.REMTASKUUID+uuid, 0,
							-1);
					if (remSet.size() > 0) {
						if (remSet.contains(url)) {
							return;
						} else {
							zoper.add(Constants.TASKUUID+uuid, url,
									request.getPriority());
						}
					} else {
						zoper.add(Constants.TASKUUID+uuid, url,
								request.getPriority());
					}
				} else {
					zoper.add(Constants.TASKUUID+uuid, url, request.getPriority());
				}
//			setExtrasInItem(request, task);
		} finally {
			log.info("redis priority scheduler:pushWhenNoDuplicate");
		}
	}

	@Override
	public synchronized Request poll(Task task) {
		String url = "";
		try {
			url = getRequest(task);
			if(url==null) {
				log.info("redis priority scheduler:poll null");
				return null;
			}
			log.info("redis priority scheduler:poll success");
			return new Request(url);
//			return getExtrasInItem(url, task);
		} finally {
			log.info("redis priority scheduler:poll finally");
		}
	}

	private String getRequest(Task task) {
		String url = "";
		ZSetOperations<String, String> zoper = redisTemplate.opsForZSet();
		String uuid = task.getUUID();
		// 每次取出jedis中的第一个url，按优先级排（从大到小）
		Set<String> urls = zoper.reverseRange(Constants.TASKUUID+uuid, 0, 0);
		if (urls.isEmpty()) {
			return null;
		} else {
			url = urls.toArray(new String[0])[0];
			Double i = zoper.score(Constants.TASKUUID+uuid, url);
			zoper.add(Constants.REMTASKUUID+uuid, url, i);
			zoper.remove(Constants.TASKUUID+uuid, url);
		}
		return url;
	}

	/*@Deprecated
	public void resetDuplicateCheck(Task task) {
		try {
			redisTemplate.delete(getSetKey(task));
		} finally {
			log.info("redis priority scheduler:resetDuplicateCheck");
		}
	}
	
	@Deprecated
	private void setExtrasInItem(Request request, Task task) {
		if (request.getExtras() != null) {
			String field = DigestUtils.shaHex(request.getUrl());
			String value = JSON.toJSONString(request);
			HashOperations hoper = redisTemplate.opsForHash();
			hoper.put(getItemKey(task), field, value);
		}
	}
	@Deprecated
	private Request getExtrasInItem(String url, Task task) {
		String key = getItemKey(task);
		String field = DigestUtils.shaHex(url);
		HashOperations hoper = redisTemplate.opsForHash();
		Object obj = hoper.get(key, field);
		if (obj != null){
			return JSON.parseObject(obj.toString(), Request.class);
		}
		return new Request(url);
	}*/

}
