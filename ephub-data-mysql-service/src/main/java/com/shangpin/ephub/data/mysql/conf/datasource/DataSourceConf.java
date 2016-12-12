package com.shangpin.ephub.data.mysql.conf.datasource;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shangpin.rdbms.RDBMSDataSource;
import com.shangpin.rdbms.config.RDBMSConfigHelper;

/**
 * <p>
 * Title:DataSourceConf.java
 * </p>
 * <p>
 * Description:数据源配置
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author yanxiaobin
 * @date 2016年11月8日 下午5:38:09
 */
@Configuration
@MapperScan(annotationClass = Mapper.class ,basePackages = {"com.shangpin.ep.order.module.*.mapper"})
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConf {

	private static final String DATASOURCEID = "ephub-data-mysql-service";

	@Autowired
	private DataSourceProperties properties;

	/**
	 * 数据源配置
	 * 
	 * @return DataSource 数据源
	 */
	@Bean
	public RDBMSDataSource datasource() {
		RDBMSDataSource datasource = new RDBMSDataSource(RDBMSConfigHelper.help(properties.getClusters()));
		datasource.setClusterId(DataSourceConf.DATASOURCEID);
		return datasource;
	}

}
