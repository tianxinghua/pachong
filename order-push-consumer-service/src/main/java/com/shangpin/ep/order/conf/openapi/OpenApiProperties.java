package com.shangpin.ep.order.conf.openapi;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title:SupplierProperties.java </p>
 * <p>Description: 所有供应商订单对接配置信息</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午3:39:07
 */
/**
 * Configuration properties for OpenApi.
 *
 * @author zhaogenchun
 */
@Getter
@Setter
@ConfigurationProperties(prefix = OpenApiProperties.OPENAPI_PREFIX)
@Component
public class OpenApiProperties {
	
	public static final String OPENAPI_PREFIX = "shangpin.hub";

	private OpenApi OpenApi;
	
}
