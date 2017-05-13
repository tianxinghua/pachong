package com.shangpin.ephub.price.consumer.conf.mail.attach;

import lombok.*;

import java.io.File;
import java.io.Serializable;

/**
 * <p>Title:AttachBean.java </p>
 * <p>Description: 邮件附件实体</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月19日 下午6:08:38
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AttachBean implements Serializable {

	/**
	 * 版本ID
	 */
	private static final long serialVersionUID = -8754073822376591879L;
	/**
	 * 附件名称
	 */
	private String fileName;
	/**
	 * 附件文件
	 */
	private File file;
	
}
