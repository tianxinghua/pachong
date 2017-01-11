package com.shangpin.ephub.fdfs.client.service.conf.fdfs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



/**
 * Configuration call api  properties
 *
 *
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ApiAddressProperties.API_ADDRESS_PREFIX)
@Component
public class ApiAddressProperties {
	
	public static final String API_ADDRESS_PREFIX = "shangpin.hub.fdfs";

	private String port;

	private String resHost; //fdfs 地址

	private String groupName;  //分配的组名称


	
}
