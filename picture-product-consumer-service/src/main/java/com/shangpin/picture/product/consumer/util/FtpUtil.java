package com.shangpin.picture.product.consumer.util;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

public class FtpUtil {

	private static volatile FtpUtil ftpUtil = null;
	
	private static volatile FTPClient ftpClient = null;
	
	private FtpUtil(){}
	
	public static FtpUtil getFtpUtil(){
		if(null == ftpUtil){
			synchronized (FtpUtil.class) {
				if(null == ftpUtil){
					ftpUtil = new FtpUtil();
				}
			}
		}
		return ftpUtil;
	}
	
	public InputStream downFile(String userName, String password, String ip, int port,String remotePath, String remoteFileName) throws Throwable {
		try {
			if(null == ftpClient){
				ftpClient = new FTPClient();
			    ftpClient.connect(ip, port);
			    ftpClient.login(userName, password);
			    ftpClient.setControlEncoding("UTF-8");
			    ftpClient.setFileType(2);
			    ftpClient.enterLocalPassiveMode();
			    ftpClient.setConnectTimeout(36000000);
			    ftpClient.setDataTimeout(36000000);
			    ftpClient.setControlKeepAliveReplyTimeout(36000000);
			    ftpClient.setControlKeepAliveTimeout(36000000L);
			}else{
				if(!ip.equals(ftpClient.getRemoteAddress().getHostAddress())){
					ftpClient = new FTPClient();
				    ftpClient.connect(ip, port);
				    ftpClient.login(userName, password);
				    ftpClient.setControlEncoding("UTF-8");
				    ftpClient.setFileType(2);
				    ftpClient.enterLocalPassiveMode();
				    ftpClient.setConnectTimeout(36000000);
				    ftpClient.setDataTimeout(36000000);
				    ftpClient.setControlKeepAliveReplyTimeout(36000000);
				    ftpClient.setControlKeepAliveTimeout(36000000L);
				}
				if (StringUtils.isNotBlank(remotePath)) {
			        ftpClient.changeWorkingDirectory(remotePath);
			    }
		        return ftpClient.retrieveFileStream(remoteFileName);
			}
		} catch (Throwable e) {
			ftpClient = null;
			throw e;
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		try {
//			ftpClient = new FTPClient();
//		    ftpClient.connect("92.223.134.2", 21);
//		    ftpClient.login("mosuftp", "inter2015£");
//		    ftpClient.setControlEncoding("UTF-8");
//		    ftpClient.setFileType(2);
//		    ftpClient.enterLocalPassiveMode();
//		    ftpClient.setConnectTimeout(36000000);
//		    ftpClient.setDataTimeout(36000000);
//		    ftpClient.setControlKeepAliveReplyTimeout(36000000);
//		    ftpClient.setControlKeepAliveTimeout(36000000L);
////		    System.out.println(ftpClient.getPassiveLocalIPAddress().getHostAddress());
//		    
//		    System.out.println(ftpClient.getRemoteAddress().getHostAddress());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
////		
//		String picUrl = "ftp://mosuftp:inter2015?@92.223.134.2/rrrrrrr/32_VM362_ZZ719V_6V3_9999902845561-00012.JPG";
//		String url = picUrl.substring(picUrl.indexOf("@")+1);
//		String ip = url.substring(0,url.indexOf("/"));
//		String remotePath =  url.substring(url.indexOf("/"),url.lastIndexOf("/")); 
//		String remoteFileName = picUrl.substring(picUrl.lastIndexOf("/")+1);
//		System.out.println(ip);
//		System.out.println(remotePath);
//		System.out.println(remoteFileName);
//	}
}
