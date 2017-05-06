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
			if(null == ftpClient || !ip.equals(ftpClient.getRemoteAddress().getHostAddress())){
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
			
		} catch (Throwable e) {
			ftpClient = null;
			if("FTP response 421 received.  Server closed connection.".equals(e.getMessage())){
				return downFile(userName,password,ip,port,remotePath,remoteFileName);
			}
			throw e;
		}
	}
	
}
