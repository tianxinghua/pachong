package com.shangpin.picture.product.consumer.conf.certificate;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title:CertificateConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年2月15日 下午2:30:41
 */
@Getter
@Setter
@ConfigurationProperties(prefix = CertificateConf.PREFIX)
public class CertificateConf {

	public static final String PREFIX = "shangpin.certificate";
	
	private Map<String, Map<String, String>> usernameAndPassword;
}
