package com.shangpin.iog.monnierfreres.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


public class FTPUtils {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");

	public static void uploadFile(String ip,int port,String usrName,String password,String remotePath ,String filePath){
		FTPClient ftp = new FTPClient(); 
        FileInputStream fis = null ; 
        String fileName = "";
        try {
			ftp.setConnectTimeout(1000 * 60 * 60);
			ftp.connect(ip, port);
			ftp.login(usrName, password);
			ftp.setControlEncoding("UTF-8");
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			if(StringUtils.isNotBlank(remotePath)){
				ftp.changeWorkingDirectory(remotePath);
			}	
			logger.info("ftp连接成功");
			File file  = new File(filePath);
			fis = new FileInputStream(file);
			fileName = file.getName();
			ftp.storeFile(fileName, fis);
			logger.info(fileName+"上传ftp成功!!!!!!!!!!!!!!!!!");			
			
		} catch (Exception e) {				
			loggerError.error(e); 
			loggerError.error(fileName+"上传失败!!!!!!!!!!");
			e.printStackTrace();
		}finally{			 
			try {
				if(null != fis){
					fis.close(); 
				}
				ftp.disconnect();
			} catch (IOException e) {
				loggerError.error(e);
			}
		}
	}
	
}
