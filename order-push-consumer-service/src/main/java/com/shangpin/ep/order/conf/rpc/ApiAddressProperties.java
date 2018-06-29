package com.shangpin.ep.order.conf.rpc;

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
	
	public static final String API_ADDRESS_PREFIX = "callAddress";
	
	private String scmsSupplierInfoUrl;
	
}
