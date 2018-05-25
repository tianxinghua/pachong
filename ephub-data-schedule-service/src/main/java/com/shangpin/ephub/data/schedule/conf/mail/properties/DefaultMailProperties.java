package com.shangpin.ephub.data.schedule.conf.mail.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

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
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class DefaultMailProperties {
	/**
	 * 默认的邮件收件人
	 */
	private String defaultRecipient;
	
	private String defaultSender;

}
