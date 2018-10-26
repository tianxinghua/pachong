package com.shangpin.supplier.product.consumer.common.interfaceconf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
 * @author yanxiaobin
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SupplierProperties.SUPPLIER_PREFIX)
@Component
public class SupplierProperties {
	
	public static final String SUPPLIER_PREFIX = "shangpin.hub.Supplier";

	Supplier supplier;

}
