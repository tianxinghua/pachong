package com.shangpin.picture.product.consumer.conf.supplier;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>Title:CertificateConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年2月15日 下午2:30:41
 */
@Getter
@Setter
@ConfigurationProperties(prefix = BrandSupplier.PREFIX)
@Component
public class BrandSupplier {

	public static final String PREFIX = "shangpin";
	
	private String brandSupplierIds;
}
