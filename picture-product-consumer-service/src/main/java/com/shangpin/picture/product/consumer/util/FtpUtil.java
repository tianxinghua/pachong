package com.shangpin.picture.product.consumer.util;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
/**
 * <p>Title: FtpUtil</p>
 * <p>Description: ftp工具，单例模式 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年5月8日 下午2:24:01
 *
 */
public class FtpUtil {

	private static volatile FtpUtil ftpUtil = null;
	
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
	
	public InputStream downFile(FTPClient ftpClient,String userName, String password, String ip, int port,String remotePath, String remoteFileName) throws Throwable {
		try {
			
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
		    if (StringUtils.isNotBlank(remotePath)) {
		        ftpClient.changeWorkingDirectory(remotePath);
		    }
	        return ftpClient.retrieveFileStream(remoteFileName);
			
		} catch (Throwable e) {
			throw e;
		}
	}
	
}
