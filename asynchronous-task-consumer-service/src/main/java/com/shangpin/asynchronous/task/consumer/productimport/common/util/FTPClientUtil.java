package com.shangpin.asynchronous.task.consumer.productimport.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esotericsoftware.minlog.Log;
import com.shangpin.asynchronous.task.consumer.conf.ftp.FtpProperties;

@Component
public class FTPClientUtil {

	@Autowired
	FtpProperties ftpProperties;

	private static String host;
	private static String port;
	private static String userName;
	private static String password;
	private static String ftpHubPatht;
	private static String exportPath;
	
	@PostConstruct
	public void init(){
		if(ftpProperties!=null){
			host = ftpProperties.getHost();
			port = ftpProperties.getPort();
			userName = ftpProperties.getUserName();
			password = ftpProperties.getPassword();
			ftpHubPatht = ftpProperties.getFtpHubPath();
			exportPath = ftpProperties.getExportPath();
		}
	
	}
	private static FTPClient ftp = null;
	static {
		if(ftp==null){
			ftp = new FTPClient();	
		}
	}
	
	/**
	 * 上传文件到导出目录（pending_export）
	 * @param file
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String uploadToExportPath(File file,String fileName) throws Exception {
		Log.info("=====开始上传ftp=====");
		int reply;
		ftp.connect(host, Integer.parseInt(port));
		ftp.login(userName, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
		InputStream sbs = new FileInputStream(file);
		
		boolean flag = ftp.changeWorkingDirectory(exportPath);
		if(!flag){
			ftp.makeDirectory(exportPath);
			ftp.changeWorkingDirectory(exportPath);
		}
		ftp.storeFile(fileName, sbs);
		sbs.close();
		Log.info("=====上传ftp结束=====");
		return exportPath;
	}
	
	/**
	 * 
	 * @param file
	 *            上传的文件
	 * @throws Exception
	 */
	public static String uploadFile(File file,String fileName) throws Exception {
		
		int reply;
		ftp.connect(host, Integer.parseInt(port));
		ftp.login(userName, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
//		InputStream sbs = new ByteArrayInputStream(data);
		InputStream sbs = new FileInputStream(file);
		ftp.changeWorkingDirectory(ftpHubPatht);
		ftp.storeFile(fileName, sbs);
		sbs.close();
		return ftpHubPatht;
	}
	public static InputStream downFile(String remotePath) throws Exception{
		
		try{
			ftp = new FTPClient();
			int reply;
			ftp.connect(host, Integer.parseInt(port));
			ftp.setConnectTimeout(1000*60*30);
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
		        }
			}
			return in;
		}catch(Exception ex){
			throw ex;
		}finally {
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
}