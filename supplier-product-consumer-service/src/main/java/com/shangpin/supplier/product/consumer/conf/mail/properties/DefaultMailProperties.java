package com.shangpin.supplier.product.consumer.conf.mail.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 邮件默认值
 * <p>Title:DefaultMailProperties.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月19日 下午6:31:12
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ConfigurationProperties(prefix = "spring.mail")
public class DefaultMailProperties {
	/**
	 * 默认的邮件收件人
	 */
	private String defaultRecipient;
	
	private String defaultSender;

}
