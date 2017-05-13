package com.shangpin.ephub.price.consumer.conf.mail.message;


import com.shangpin.ephub.price.consumer.conf.mail.attach.AttachBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

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
	 * 其余的收件人
	 */
	private List<String> addTo;
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
