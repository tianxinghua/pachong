package com.shangpin.picture.product.consumer.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title:AuthenticationInformation.java </p>
 * <p>Description: 认证信息类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年2月10日 下午5:25:14
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationInformation {
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
}
