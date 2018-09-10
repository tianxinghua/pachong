package com.shangpin.spider.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSourceFactory;


@Configuration
public class DruidConfig {
    //配置文件
    @Autowired
    private Environment env;
    //默认为主数据源
    @Bean
    @Primary
    public DataSource getDataSource() throws Exception{
           Properties properties = new Properties();
           properties.put("driverClassName", env.getProperty("spring.datasource.driverClassName"));
           properties.put("url", env.getProperty("spring.datasource.url"));
           properties.put("username", env.getProperty("spring.datasource.username"));
           properties.put("password", env.getProperty("spring.datasource.password"));
           properties.put("initialSize", env.getProperty("spring.datasource.initialSize"));
           properties.put("minIdle", env.getProperty("spring.datasource.minIdle"));
           properties.put("maxActive", env.getProperty("spring.datasource.maxActive"));
           properties.put("maxWait", env.getProperty("spring.datasource.maxWait"));
           properties.put("timeBetweenEvictionRunsMillis", env.getProperty("spring.datasource.timeBetweenEvictionRunsMillis"));
           properties.put("minEvictableIdleTimeMillis", env.getProperty("spring.datasource.minEvictableIdleTimeMillis"));
           properties.put("validationQuery", env.getProperty("spring.datasource.validationQuery"));
           properties.put("filters", env.getProperty("spring.datasource.druid.sys.filters"));
           properties.put("validationQueryTimeout", env.getProperty("spring.datasource.validationQueryTimeout"));
           properties.put("testWhileIdle", env.getProperty("spring.datasource.testWhileIdle"));
           properties.put("testOnBorrow", env.getProperty("spring.datasource.testOnBorrow"));
           properties.put("testOnReturn", env.getProperty("spring.datasource.testOnReturn"));
           return DruidDataSourceFactory.createDataSource(properties);
       
    }
}
