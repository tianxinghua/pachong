package com.shangpin.iog.common.utils;

import java.util.Calendar;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class SendMail {

	/**
	 * 发送邮件
	 * @param smtpHost 传输协议，如SMTP协议
	 * @param from 发件人email地址
	 * @param fromUserPassword 发件人email密码
	 * @param to  收件人email地址
	 * @param subject  邮件主题
	 * @param messageText  邮件内容
	 * @param messageType  Mime type of the object
	 * @throws Exception
	 */
	public static void sendMessage(String smtpHost, String from,  
            String fromUserPassword, String to, String subject,  
            String messageText, String messageType) throws Exception{  
        // 第一步：配置javax.mail.Session对象  
        System.out.println("为" + smtpHost + "配置mail session对象");  
          
          
        Properties props = new Properties();  
        props.put("mail.smtp.host", smtpHost);  
        props.put("mail.smtp.starttls.enable","true");//使用 STARTTLS安全连接  
        //props.put("mail.smtp.port", "25");             //google使用465或587端口  
        props.put("mail.smtp.auth", "true");        // 使用验证  
        //props.put("mail.debug", "true");  
        Session mailSession = Session.getInstance(props,new MyAuthenticator(from,fromUserPassword));  
  
        // 第二步：编写消息  
        System.out.println("编写消息from——to:" + from + "——" + to);  
  
        InternetAddress fromAddress = new InternetAddress(from);  
        InternetAddress toAddress = new InternetAddress(to);  
  
        MimeMessage message = new MimeMessage(mailSession);  
  
        message.setFrom(fromAddress);  
        message.addRecipient(RecipientType.TO, toAddress);  
  
        message.setSentDate(Calendar.getInstance().getTime());  
        message.setSubject(subject);  
        message.setContent(messageText, messageType);  
  
        // 第三步：发送消息  
        Transport transport = mailSession.getTransport("smtp");  
        //transport.connect(smtpHost,"lubaijiang", fromUserPassword);  
        transport.connect(from, fromUserPassword);
        transport.send(message, message.getRecipients(RecipientType.TO));  
        System.out.println("message yes");  
    }  
	
	/**
	 * 按组群发邮件
	 * @param smtpHost 传输协议，如SMTP协议
	 * @param from 发件人email地址
	 * @param fromUserPassword 发件人email密码
	 * @param to 收件人地址 多个地址之间用,分隔
	 * @param subject 邮件主题
	 * @param messageText 邮件内容
	 * @param messageType 
	 * @throws MessagingException 
	 */
	public static void sendGroupMail(String smtpHost, String from,  
            String fromUserPassword, String to, String subject,  
            String messageText, String messageType) throws MessagingException{
		
		// 第一步：配置javax.mail.Session对象  
        System.out.println("为" + smtpHost + "配置mail session对象");  
          
          
        Properties props = new Properties();  
        props.put("mail.smtp.host", smtpHost);  
        props.put("mail.smtp.starttls.enable","true");//使用 STARTTLS安全连接  
        //props.put("mail.smtp.port", "25");             //google使用465或587端口  
        props.put("mail.smtp.auth", "true");        // 使用验证  
        //props.put("mail.debug", "true");  
        Session mailSession = Session.getInstance(props,new MyAuthenticator(from,fromUserPassword));  
 
        //第二步：编写消息 
        MimeMessage message = new MimeMessage(mailSession);  
        InternetAddress fromAddress = new InternetAddress(from);
        message.setFrom(fromAddress); 
        //创建收件人列表
        String[] arr = to.split(",");
        InternetAddress[] address = new InternetAddress[arr.length];  
        for (int i = 0; i < arr.length; i++) {  
            address[i] = new InternetAddress(arr[i]);  
        }  
        message.addRecipients(RecipientType.TO, address);
        message.setSentDate(Calendar.getInstance().getTime());  
        message.setSubject(subject);  
        message.setContent(messageText, messageType);  
  
        // 第三步：发送消息  
        Transport transport = mailSession.getTransport("smtp"); 
        transport.connect(from, fromUserPassword);
        transport.send(message, message.getRecipients(RecipientType.TO));  
        System.out.println("message yes");
	}
  

    public static void main(String[] args) {
//        try {  
//            SendMail.sendMessage("smtp.shangpin.com", "lubaijiang@shangpin.com",  
//                    "*********", "lubaijiang@vansky.cn", "hello",  
//                    "测试中文测试connect",  
//                    "text/html;charset=utf-8");  
//        } catch (Exception e) {  
//            // TODO Auto-generated catch block  
//            e.printStackTrace();  
//        }  
    	try {
			SendMail.sendGroupMail("smtp.shangpin.com", "lubaijiang@shangpin.com",  
			            "********", "lubaijiang@shangpin.com,lubaijiang@vansky.cn", "hello",  
			            "测试中文，测试群发",  
			            "text/html;charset=utf-8");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
} 

/**
 * 验证类
 * @author sunny
 *
 */
class MyAuthenticator extends Authenticator{  
    String userName="";  
    String password="";  
    public MyAuthenticator(){  
          
    }  
    public MyAuthenticator(String userName,String password){  
        this.userName=userName;  
        this.password=password;  
    }  
     protected PasswordAuthentication getPasswordAuthentication(){     
        return new PasswordAuthentication(userName, password);     
     }   
}  

