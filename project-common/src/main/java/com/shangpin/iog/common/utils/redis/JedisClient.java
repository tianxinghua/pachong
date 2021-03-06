package com.shangpin.iog.common.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by loyalty on 15/2/28.
 * redis 客户端工具类
 * TODO 增加复杂对象的写入和读取
 * 可用 替代工具时spring-data-redis
 */
public class JedisClient {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private  static    String URL;
    private static int MAX_TOTAL,MAX_IDLE;
    private static long MAX_WAIT;
    private static int PORT;
    private static String PASSWORD;
    private static Boolean TEST_ON_BORROW,TEST_ON_RETURN;

    private static class JedisClientUtilHolder {
        private static final JedisClient INSTANCE = new JedisClient();
    }

    private JedisPool pool = null;    //Jedis 使用 commons-pool 完成池化实现
    private ShardedJedisPool sharepool = null; //Jedis分布式

    static{
        ResourceBundle bundle = ResourceBundle.getBundle("redis");
        if (bundle == null) {
            throw new IllegalArgumentException(
                    "[redis.properties] is not found!");
        }
        URL = bundle.getString("redis.url");
        PORT = Integer.valueOf(bundle.getString("redis.port"));
        PASSWORD = bundle.getString("redis.password");

        MAX_TOTAL = Integer.valueOf(bundle.getString("redis.pool.maxTotal"));
        MAX_IDLE = Integer.valueOf(bundle.getString("redis.pool.maxIdle"));
        MAX_WAIT = Long.valueOf(bundle.getString("redis.pool.maxWait"));
        TEST_ON_RETURN = Boolean.valueOf(bundle.getString("redis.pool.testOnReturn"));
        TEST_ON_BORROW = Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow"));
    }

    private JedisClient(){

        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_TOTAL);

            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            config.setTestOnReturn(TEST_ON_RETURN);

//            List<JedisShardInfo> shards =  AddrUtil.getAddresses(URL);

            pool=new JedisPool(config,URL,PORT );


        } catch (Exception e) {
            logger.info("redis初始化失败",e.getCause());
        }
    }

    public static final JedisClient getInstance(){


        return JedisClientUtilHolder.INSTANCE;
    }
    //同步设置值
    public  final void setValueOnSync(String key,String value){
        writeErrorForInit();
//        ShardedJedis jedis = null;
        Jedis jedis =null;
        try {

            jedis = pool.getResource();
            if(null!=PASSWORD&&!"".equals(PASSWORD))   jedis.auth(PASSWORD) ;

            jedis.set(key,value);


        } catch (Exception e) {
            logger.info("redis同步设置数据失败",e.getCause());
        }finally {
            // 释放对象池
            if(null!=jedis)   pool.returnResource(jedis);
        }
    }

    //异步设置值
    public  final void setValueOnAsync(String key,String value){
        writeErrorForInit();
//        ShardedJedis jedis = null;
        Jedis jedis = null;
        try {
             jedis = pool.getResource();
            if(null!=PASSWORD&&!"".equals(PASSWORD))   jedis.auth(PASSWORD) ;
            Pipeline pipeline = jedis.pipelined();
//            ShardedJedisPipeline pipeline = jedis.pipelined();
            pipeline.set(key,value);

        } catch (Exception e) {
            logger.info("redis异步设置数据失败",e.getCause());
        }finally{
            // 释放对象池
            if(null!=jedis)   pool.returnResource(jedis);
        }

    }
   //同步获取值
    public  String getValue(String key){
        writeErrorForInit();
//        ShardedJedis jedis = null;
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            if(null!=PASSWORD&&!"".equals(PASSWORD))   jedis.auth(PASSWORD) ;
            String  value = jedis.get(key);

            return value;
        } catch (Exception e) {
            logger.info("redis获取数据失败", e.getCause());
            return null;
        }finally {
            // 释放对象池
            if(null != jedis) pool.returnResource(jedis);
        }


    }

    public  long getIncValue(String key){
        writeErrorForInit();
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            if(null!=PASSWORD&&!"".equals(PASSWORD))   jedis.auth(PASSWORD) ;
            return jedis.incr(key);

        } catch (Exception e) {
            logger.error("redis获取数据失败", e.getCause());
            Random ra =new Random();
            return ra.nextInt(1000000);
        }finally {
            // 释放对象池
            if(null != jedis) pool.returnResource(jedis);
        }


    }



    private void writeErrorForInit(){
        if(null==pool) {
            logger.error("redis初始化失败");
            return;
        }
    }

    public static void main(String[] args){

    }


}
