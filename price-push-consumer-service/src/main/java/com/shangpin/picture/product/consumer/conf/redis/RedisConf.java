package com.shangpin.picture.product.consumer.conf.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.commons.redis.cluster.jedis.ShangpinRedisClusterFactory;

/**
 * Redis集群配置
 * <p>Title:RedisConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月8日 下午5:07:22
 */
@Configuration
public class RedisConf {
	/**
	 * 应用编号：可以配置多个应用
	 */
	private static final String applicationId = "picture-product-consumer-service";
	/**
	 * 应用下的集群编号：一个应用下可以有多个集群
	 */
	private static final String clusterId = "default";
	/**
	 * 直接使用jedis API进行操作，况且该对象是线程安全的，请放心使用！
	 * @return IShangpinRedis 底层原理使用的是redis3.0集群机制
	 */
	@Bean
	public IShangpinRedis shangpinRedis(){
		return ShangpinRedisClusterFactory.getInstance().getShangpinRedis(applicationId, clusterId).getShangpinRedis();
	}
}
