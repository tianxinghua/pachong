package com.shangpin.asynchronous.task.consumer.conf.rpc;

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
	
	public static final String API_ADDRESS_PREFIX = "shangpin.hub.callAddress";

	private String pendingProductInfoUrl;
	
}
