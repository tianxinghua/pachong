package com.shangpin.iog.monnierfreres.utils;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

@Component
public class FTPClientUtil {

	private static Logger logger = Logger.getLogger("info");

	public static final String PROPERTIES_FILE_NAME = "conf";
	static ResourceBundle bundle = null;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");


	private static String host;
	private static String port;
	private static String userName;
	private static String password;
	private static String filePath;
	private static String fileName;



	private static FTPClient ftp = null;
	static {
		if(ftp==null){
			ftp = new FTPClient();	
		}

		if(null==bundle){
			bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
		}
		host = bundle.getString("host");
		port = bundle.getString("port");
		userName = bundle.getString("userName");
		password = bundle.getString("password");
		filePath = bundle.getString("filePath");
		fileName = bundle.getString("fileName");

	}

	public static InputStream downFile(String remotePath) throws Exception {
		
		try{
			if(null==ftp)	ftp = new FTPClient();
			int reply;
			String originFileName="";
			ftp.connect(host, Integer.parseInt(port));
			ftp.setConnectTimeout(1000*60*30);
			ftp.login(userName, password);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
//			reply = ftp.getReplyCode();
//			if (!FTPReply.isPositiveCompletion(reply)) {
//				ftp.disconnect();
//			}
	    	InputStream in = null;
			if(null != ftp){
				if(StringUtils.isBlank(remotePath)) remotePath =  fileName;
				originFileName = remotePath + dateFormat.format(new Date())+".csv";
				logger.info("filePath =" + filePath);
                logger.info("file =" + originFileName);
				ftp.changeWorkingDirectory(filePath);
				in = ftp.retrieveFileStream(originFileName);
				if(null==in){
					Calendar calendar =Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_YEAR,-1);
					originFileName =  remotePath + dateFormat.format(calendar.getTime())+".csv";
					logger.info("input stream is  null .file =" + originFileName);
					in = ftp.retrieveFileStream(originFileName);
				}else{
					logger.info("input stream is not null ");
				}

			}
			return in;
		}catch(Exception ex){
			logger.error("fetch file error = " + ex.getMessage(),ex);
			throw ex;
		}finally {
//			closeFtp();
		}
		
	}
	
	public static void closeFtp(){

		if(ftp!=null){
			try {
				ftp.quit();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
	}

	public static void main(String[] args){
		try {
			FTPClientUtil.downFile("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}