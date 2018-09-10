/*package com.shangpin.spider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

//@ConfigurationProperties
@Configuration
public class MyRedisConfig  {

  @Value("${spring.redis.host}")
  private String host;
  @Value("${spring.redis.port}")
  private int port;
  @Value("${spring.redis.password}")
  private String password;
  @Value("${spring.redis.dataBaseIndex}")
  private int dataBaseIndex;
  @Value("${spring.redis.timeout}")
  private int timeout;
  @Value("${spring.redis.maxTotal}")
  private int maxTotal;
  @Value("${spring.redis.testOnBorrow}")
  private boolean testOnBorrow;
  @Value("${spring.redis.testOnReturn}")
  private boolean testOnReturn;
  
  @Bean
  public RedisConnectionFactory jedisConnectionFactory() {
      JedisPoolConfig poolConfig = new JedisPoolConfig();
      poolConfig.setMaxTotal(500);
      poolConfig.setTestOnBorrow(true);
      poolConfig.setTestOnReturn(true);
      JedisConnectionFactory ob = new JedisConnectionFactory(poolConfig);
      ob.setUsePool(true);
//      ob.setDatabase(dataBaseIndex);
      ob.setHostName(host);
      ob.setPort(port);
//      ob.setPassword(password);
      ob.setTimeout(timeout);
      return ob;
  }
  @Bean
  public StringRedisTemplate  stringRedisTemplate(){
      return new StringRedisTemplate(jedisConnectionFactory());
  }
  
  @Bean
  @Scope("prototype")
  public RedisConnection redisConnection(){
    return jedisConnectionFactory().getConnection();
  }

}
*/