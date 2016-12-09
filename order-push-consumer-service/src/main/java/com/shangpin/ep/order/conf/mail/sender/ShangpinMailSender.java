package com.shangpin.ep.order.conf.mail.sender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

import com.shangpin.ep.order.conf.mail.attach.AttachBean;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.model.MailModel;
import com.shangpin.ep.order.conf.mail.properties.DefaultMailProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Title:ShangpinMailSender.java </p>
 * <p>Description: 尚品邮件发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月19日 下午4:36:29
 */
@SuppressWarnings("deprecation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@EnableConfigurationProperties(DefaultMailProperties.class)
public class ShangpinMailSender {
	
	/**
	 * 注入邮件发射器
	 */
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private DefaultMailProperties defaultMailProperties;
	/**
	 * 发送邮件
	 * @param shangpinMail
	 * @throws Exception 
	 */
	@SuppressWarnings("all")
	public void sendShangpinMail(ShangpinMail shangpinMail) throws Exception{
		Assert.notNull(shangpinMail, "ShangpinMail is not allowed to be bull , please check!");
		Assert.notNull(defaultMailProperties, "defaultMailProperties is not allowed to be bull , please check!");
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		String subject = shangpinMail.getSubject();
		if (StringUtils.isNotBlank(subject)) {
			helper.setSubject(subject);
		}
		String inputTo = shangpinMail.getTo();
		helper.setTo(inputTo == null? defaultMailProperties.getDefaultRecipient():inputTo.trim());//收件人
		String inputFrom = shangpinMail.getFrom();
		String from = inputFrom == null? defaultMailProperties.getDefaultSender():inputFrom.trim();
		helper.setFrom(from); //发件人
		MailModel mailModel = new MailModel();
		mailModel.setHonorific("Dear:");
		String body = shangpinMail.getText();//邮件正文
		mailModel.setText(body == null ? "" : body);
		mailModel.setSender(from);
		mailModel.setDatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		Map<String, Object> map = new HashMap<>();
        map.put("mailModel", mailModel);
        VelocityEngineFactoryBean velocityEngineFactoryBean = new VelocityEngineFactoryBean();
        Properties velocityProperties = new Properties();
        velocityProperties.setProperty("resource.loader", "class");
        velocityProperties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngineFactoryBean.setVelocityProperties(velocityProperties);
        VelocityEngine velocityEngine = velocityEngineFactoryBean.createVelocityEngine();
        String text = VelocityEngineUtils.mergeTemplateIntoString(
                velocityEngine, "mail/mail.vm", map);
        helper.setText(text, true);
        List<AttachBean> attachList = shangpinMail.getAttachList();
        if (CollectionUtils.isNotEmpty(attachList)) {
			for (AttachBean attachBean : attachList) {
				helper.addAttachment(attachBean.getFileName(), attachBean.getFile());
			}
		}
		javaMailSender.send(mimeMessage);
	}

	 

}
