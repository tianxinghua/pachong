package com.shangpin.spider.gather.scheduler;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

/**
 * @author njt
 * @date 创建时间：2017年10月31日 上午10:21:06
 * @version 1.0
 * @parameter
 */
@Service
public class RedisScheduler extends DuplicateRemovedScheduler implements
		MonitorableScheduler, DuplicateRemover {

	private static Logger log = Logger.getLogger(RedisScheduler.class);

	private static final String QUEUE_PREFIX = "queue_";

	private static final String SET_PREFIX = "set_";

	private static final String ITEM_PREFIX = "item_";

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void resetDuplicateCheck(Task task) {
		try {
			redisTemplate.delete(getSetKey(task));
		} finally {
			log.info("redis operation:resetDulicateCheck");

		}
	}

	@Override
	public boolean isDuplicate(Request request, Task task) {
		SetOperations soper = redisTemplate.opsForSet();
		try {
			return soper.add(getSetKey(task), request.getUrl()) > 0;
		} finally {
			log.info("redis operation:isDuplicate");
		}

	}

	@Override
	protected void pushWhenNoDuplicate(Request request, Task task) {
		ListOperations loper = redisTemplate.opsForList();
		HashOperations hoper = redisTemplate.opsForHash();
		try {
			loper.rightPush(getQueueKey(task), request.getUrl());
			if (request.getExtras() != null) {
				String field = DigestUtils.shaHex(request.getUrl());
				String value = JSON.toJSONString(request);
				hoper.put((ITEM_PREFIX + task.getUUID()), field, value);
			}
		} finally {
			log.info("redis operation:pushWhenNoDuplicate");
		}
	}

	@Override
	public synchronized Request poll(Task task) {
		ListOperations loper = redisTemplate.opsForList();
		HashOperations hoper = redisTemplate.opsForHash();
		try {
			Object url = loper.leftPop(getQueueKey(task));
			if (url == null) {
				return null;
			}
			String key = ITEM_PREFIX + task.getUUID();
			String field = DigestUtils.shaHex(url.toString());
			Object obj = hoper.get(key, field);
			if (obj != null) {
				Request o = JSON.parseObject(obj.toString(), Request.class);
				return o;
			}
			Request request = new Request(url.toString());
			return request;
		} finally {
			log.info("redis operation:poll");
		}
	}

	protected String getSetKey(Task task) {
		return SET_PREFIX + task.getUUID();
	}

	protected String getQueueKey(Task task) {
		return QUEUE_PREFIX + task.getUUID();
	}

	protected String getItemKey(Task task) {
		return ITEM_PREFIX + task.getUUID();
	}

	@Override
	public int getLeftRequestsCount(Task task) {
		ListOperations loper = redisTemplate.opsForList();
		try {
			Long size = loper.size(getQueueKey(task));
			return size.intValue();
		} finally {
			log.info("redis operation:getLeftRequestsCount");
		}
	}

	@Override
	public int getTotalRequestsCount(Task task) {
		SetOperations soper = redisTemplate.opsForSet();
		try {
			Long size = soper.size(getSetKey(task));
			return size.intValue();
		} finally {
			log.info("redis operation:getTotalRequestsCount");
		}
	}
}
