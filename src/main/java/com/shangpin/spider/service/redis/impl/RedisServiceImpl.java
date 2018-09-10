package com.shangpin.spider.service.redis.impl;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.shangpin.spider.service.redis.IRedisService;

public class RedisServiceImpl implements IRedisService {
	@Autowired
    private RedisTemplate<Serializable, Serializable> redisTemplate;
	
	@Override
	public Object getObject(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key,  Class<? extends Serializable> requiredType) {
		Serializable val = redisTemplate.opsForValue().get(key);
		if(val == null){
			return null;
		}
		return ((T)val);
	}

	@Override
	public void remove(String key) {
		redisTemplate.delete(key);
	}

	@Override
	public void saveObject(String key, Serializable object) {
		redisTemplate.opsForValue().set(key, object);
	}

	@Override
	public void saveWithExpireTime(String key, Serializable object, long timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, object, timeout, unit);
	}

	@Override
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
}
