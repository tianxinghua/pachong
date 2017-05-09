package com.shangpin.iog.DellaHK.purchase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.SendMail;

@Component
public class Upload {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String subject = null;
	private static String messageType = null;
	private static String saveLocal = null;
	
	static{
		if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
		smtpHost  = bdl.getString("smtpHost");
		from = bdl.getString("from");
		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		subject = bdl.getString("subject");
		messageType = bdl.getString("messageType");
		saveLocal = bdl.getString("saveLocal");
	}
	
//	public static void main(String[] args) {
//		 testUpload(); 
//		
//	}
	
	public void testUpload() { 
		String fileName = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
		String dateStr = format.format(new Date());
		fileName = dateStr + ".csv";
		
		int i=0;
		for(;i<10;i++){
			logger.info("第"+i+"次开始上传92.223.134.2开始");
			FTPClient ftp = new FTPClient(); 
	        FileInputStream fis = null ; 
//	        OutputStream out = null;
	        try {
				ftp.setConnectTimeout(1000 * 60 * 5);
				ftp.connect("92.223.134.2", 21);
				ftp.login("mosuftp", "inter2015£");
				ftp.setControlEncoding("UTF-8");
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				// ftp.changeWorkingDirectory("/home/apiftp");
				ftp.changeWorkingDirectory("/MOSU/Orders");
				// ftp.enterLocalActiveMode();
				File srcFile = new File("/home/apiftp/a.csv"); // /usr/local/apporder/Della/a.csv
				fis = new FileInputStream(srcFile);
				logger.info("ftp连接成功");
				ftp.storeFile(fileName, fis);
				logger.info(fileName+"上传92.223.134.2成功!!!!!!!!!!!!!!!!!");
				
				//保存到本地
				BufferedWriter out = null;
				BufferedReader in = null;
				try {
					out =new BufferedWriter(new FileWriter(saveLocal+File.separator+fileName));
					in =new BufferedReader(new FileReader(srcFile));
					String temp = "";
					while(StringUtils.isNotBlank(temp=in.readLine())){
						out.append(temp).append("\n"); 
					}
					out.flush();
					logger.info(fileName+"保存本地成功!!!!!!!!!!!!!!!!!");
				} catch (Exception e) {
					loggerError.error(e);
					loggerError.error(fileName+"保存到本地失败"); 
				}finally{
					try {
						if(null != out){
							out.close();
						}
						if(null != in){
							in.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				break;
	   		
			} catch (Exception e) {				
				loggerError.error(e); 
			}finally{
				 //IOUtils.closeQuietly(fis); 
				try {
					ftp.disconnect();
				} catch (IOException e) {
					loggerError.error(e);
				}
			}
		}
		logger.info("i========="+i);
		
		if(i==10){
			loggerError.error("上传失败的fileName================="+fileName); 
			Thread t = new Thread(new Runnable() {				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						SendMail.sendGroupMail(smtpHost, from, fromUserPassword, to, "della订单上传92.223.134.2ftp失败", "della订单上传92.223.134.2ftp失败", messageType);
					} catch (Exception e) {
						loggerError.error(e);
					}
				}
			});
			
		}
        

}
	
	public static void main(String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH");
		String dateStr = format.format(new Date());
		System.out.println(dateStr);
	}
}