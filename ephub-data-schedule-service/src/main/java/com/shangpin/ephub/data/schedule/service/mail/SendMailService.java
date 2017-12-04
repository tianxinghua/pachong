package com.shangpin.ephub.data.schedule.service.mail;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.schedule.conf.mail.attach.AttachBean;
import com.shangpin.ephub.data.schedule.conf.mail.message.ShangpinMail;
import com.shangpin.ephub.data.schedule.conf.mail.sender.ShangpinMailSender;
/**
 * <p>Title: SendMailService</p>
 * <p>Description: 发送邮件 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月21日 下午12:00:07
 *
 */
@Service
@Slf4j
public class SendMailService {
	
	@Autowired
	private ShangpinMailSender shangpinMailSender;

	/**
	 * 发送邮件
	 * @param subject
	 * @param text
	 */
	public void sendMail(String subject,String text){
		try {
			ShangpinMail shangpinMail = new ShangpinMail();
			shangpinMail.setFrom("chengxu@shangpin.com");
			shangpinMail.setSubject(subject);
			shangpinMail.setText(text);
			shangpinMail.setTo("ephub_support.list@shangpin.com");
			
			
			shangpinMailSender.sendShangpinMail(shangpinMail);
		} catch (Exception e) {
			log.error("发送邮件失败："+e.getMessage(),e); 
		}
	}
	public void sendMailWithFile(String subject,String text,File file,String fileName){
		try {
			ShangpinMail shangpinMail = new ShangpinMail();
			shangpinMail.setFrom("chengxu@shangpin.com");
			shangpinMail.setSubject(subject);
			shangpinMail.setText(text);
			shangpinMail.setTo("ephub_support.list@shangpin.com");
			
			List<String> addTo = new ArrayList<>();
			addTo.add("bu3.list@shangpin.com");
			addTo.add("bu2.list@shangpin.com");
			addTo.add("bu1.list@shangpin.com");
			shangpinMail.setAddTo(addTo);
			
			List<AttachBean> attachList = new ArrayList<AttachBean>();
			AttachBean bean = new AttachBean();
			bean.setFileName(fileName);
			bean.setFile(file);
			attachList.add(bean);
			shangpinMail.setAttachList(attachList);
			shangpinMailSender.sendShangpinMail(shangpinMail);
		} catch (Exception e) {
			log.error("发送邮件失败："+e.getMessage(),e); 
		}
	}
	

	
}
