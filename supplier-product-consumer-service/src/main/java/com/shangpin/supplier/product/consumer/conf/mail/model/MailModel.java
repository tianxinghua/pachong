package com.shangpin.supplier.product.consumer.conf.mail.model;

import lombok.*;

import java.io.Serializable;

/**
 * <p>Title:MailModel.java </p>
 * <p>Description: 邮件信息模型</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月19日 下午6:14:48
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MailModel implements Serializable{

	/**
	 * 版本ID
	 */
	private static final long serialVersionUID = 5041219898470025343L;
	/**
	 * 尊称
	 */
	private String honorific;
	/**
	 * 正文
	 */
	private String text;
	/**
	 * 发件人
	 */
	private String sender;
	/**
	 * 发送时间
	 */
	private String datetime;
}
