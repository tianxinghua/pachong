package com.shangpin.ephub.data.mongodb.common.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * API通用响应数据
 *@Project: scm-message-service
 *@Author: yanxiaobin
 *@Date: 2016年10月25日
 *@Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class APIRsponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -751462478006167902L;
	/**
	 * 成功：0 ；失败：1
	 */
	private Integer code;
	/**
	 * 错误消息
	 */
	private String message;
}