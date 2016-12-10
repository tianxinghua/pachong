package com.shangpin.ep.order.conf.mail.message;

import java.io.Serializable;
import java.util.List;

import com.shangpin.ep.order.conf.mail.attach.AttachBean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * <p>Title:ShangpinMail.java </p>
 * <p>Description: 尚品邮件消息实体类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月19日 下午4:39:52
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShangpinMail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5563939936117183041L;
	/**
	 * 发件人
	 */
	private String from;
	/**
	 * 收件人
	 */
	private String to;
	/**
	 * 主题
	 */
	private String subject;
	/**
	 * 
	 */
	private String text;
	/**
	 * 需要发送的邮件附件列表
	 */
	private List<AttachBean> attachList;
}
