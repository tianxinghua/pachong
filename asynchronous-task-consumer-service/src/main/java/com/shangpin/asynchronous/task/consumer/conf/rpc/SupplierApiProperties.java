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
@ConfigurationProperties(prefix = SupplierApiProperties.PARAMS)
@Component
public class SupplierApiProperties {
	
	public static final String PARAMS = "supplier.params";

	private String supplierName;

	private String  supplierId;
	
	private String supplierNo;
	
}
