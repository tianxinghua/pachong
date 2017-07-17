package com.shangpin.supplier.product.consumer.conf.mail;

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
 * Configuration properties for DataSource.
 *
 * @author zhaogenchun
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ShangpinMailProperties.SUPPLIER_PREFIX)
@Component
public class ShangpinMailProperties {
	
	public static final String SUPPLIER_PREFIX = "shangpin.hub";
	private String mailSendTo;
}
