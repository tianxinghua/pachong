package com.shangpin.pending.product.consumer.supplier.common;

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
@ConfigurationProperties(prefix = CommonProperties.COMMON_PREFIX)
@Component
public class CommonProperties {
	
	public static final String COMMON_PREFIX = "shangpin.hub.commonProperty";

	private String googleKey;

	private boolean translateAll;

	private String supplierId;


	
}
