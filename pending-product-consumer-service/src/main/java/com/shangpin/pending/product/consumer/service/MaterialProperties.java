package com.shangpin.pending.product.consumer.service;

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
@ConfigurationProperties(prefix = MaterialProperties.COMMON_PREFIX)
@Component
public class MaterialProperties {
	
	public static final String COMMON_PREFIX = "shangpin.hub.translateProperty";

	private String googleKey;
	/**
	 * 是否全都翻译
	 */
	private boolean translateAll;
	/**
	 * 需要翻译的供货商的门户ID
	 */
	private String supplierId;
	/**
	 * 定时器指定某台机器是否查询翻译
	 */
	private boolean refresh;


	
}
