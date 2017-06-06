package com.shangpin.studio.data.mysql.conf.datasource;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.shangpin.rdbms.config.Cluster;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for DataSource.
 *
 * @author yanxiaobin
 */
@Getter
@Setter
@ConfigurationProperties(prefix = DataSourceProperties.DATASOURCE_PREFIX)
public class DataSourceProperties {
	
	public static final String DATASOURCE_PREFIX = "shangpin.studio.dataSource";
	
	private List<Cluster> clusters;
	
}
