package com.shangpin.ephub.product.business.ui.task.spuimport.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.product.business.conf.ftp.FtpProperties;
import com.shangpin.ephub.response.HubResponse;

@Component
public class FTPClientUtil {

	@Autowired
	FtpProperties ftpProperties;

	private static String host;
	private static String port;
	private static String userName;
	private static String password;
	private static String ftpHubPatht;
	
	@PostConstruct
	public void init(){
		host = ftpProperties.getHost();
		port = ftpProperties.getPort();
		userName = ftpProperties.getUserName();
		password = ftpProperties.getPassword();
		ftpHubPatht = ftpProperties.getFtpHubPath();
	}
	/**
	 * 
	 * @param file
	 *            上传的文件
	 * @throws Exception
	 */
	public static String uploadFile(byte[] data,String fileName) throws Exception {
		
		FTPClient ftp = new FTPClient();
		int reply;
		ftp.connect(host, Integer.parseInt(port));
		ftp.login(userName, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
		InputStream sbs = new ByteArrayInputStream(data);
		ftp.changeWorkingDirectory(ftpHubPatht);
		ftp.storeFile(fileName, sbs);
		ftp.quit();
		return ftpHubPatht;
	}
	public static InputStream downFile(String remotePath) throws Exception{
		
		FTPClient ftp = new FTPClient();
		int reply;
		ftp.connect(host, Integer.parseInt(port));
		ftp.login(userName, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
		
    	InputStream in = null;
		if(null != ftp){
	        if(StringUtils.isNotBlank(remotePath)){
	        	in = ftp.retrieveFileStream(remotePath);
	        	ftp.quit();
	        }
		}
		return in;
	}
	
}