package com.shangpin.ephub.product.business.ui.task.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.product.business.conf.ftp.FtpProperties;

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
	public void init() {
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
	public static String uploadFile(byte[] data, String fileName) throws Exception {

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
		boolean flag = ftp.changeWorkingDirectory(ftpHubPatht);
		if (!flag) {
			ftp.makeDirectory(ftpHubPatht);
			ftp.changeWorkingDirectory(ftpHubPatht);
		}
		ftp.storeFile(fileName, sbs);
		ftp.disconnect();
		return ftpHubPatht;
	}
	
	/**
	 * 
	 * @param file
	 *            上传的文件
	 * @throws Exception
	 */
	public static void uploadNewFile(String pathName, String fileName,InputStream in) throws Exception {

		FTPClient ftp = new FTPClient();
		int reply;
		ftp.connect(host, Integer.parseInt(port));
		ftp.login(userName, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
		ftp.changeWorkingDirectory(pathName);
		ftp.storeFile(fileName, in);
		in.close();
		ftp.disconnect();
	}

	public static InputStream downFile(String remotePath) throws Exception {

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
		if (null != ftp) {
			if (StringUtils.isNotBlank(remotePath)) {
				in = ftp.retrieveFileStream(remotePath);
				ftp.quit();
			}
		}
		return in;
	}
	
	public static InputStream downFileNew(String remotePath, String remoteFileName) throws Exception {
		try {
			FTPClient ftp = new FTPClient();
			ftp.connect(host, Integer.parseInt(port));
			ftp.login(userName, password);
			ftp.setControlEncoding("UTF-8");
			ftp.setFileType(2);
			ftp.enterLocalPassiveMode();
			ftp.setConnectTimeout(36000000);
			ftp.setDataTimeout(36000000);
			ftp.setControlKeepAliveReplyTimeout(36000000);
			ftp.setControlKeepAliveTimeout(36000000L);
		    if (StringUtils.isNotBlank(remotePath)) {
		    	ftp.changeWorkingDirectory(remotePath);
		    }
	        return ftp.retrieveFileStream(remoteFileName);
			
		} catch (Throwable e) {
			throw e;
		}
	}
	
	public static void deleteFile(String remotePath) throws Exception {

		FTPClient ftp = new FTPClient();
		int reply;
		ftp.connect(host, Integer.parseInt(port));
		ftp.login(userName, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
		if (null != ftp) {
			if (StringUtils.isNotBlank(remotePath)) {
				ftp.deleteFile(remotePath);
				ftp.quit();
			}
		}
	}
	
	public static void deleteDir(String remotePath) throws Exception {

		FTPClient ftp = new FTPClient();
		int reply;
		ftp.connect(host, Integer.parseInt(port));
		ftp.login(userName, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
		if (null != ftp) {
			if (StringUtils.isNotBlank(remotePath)) {
				ftp.removeDirectory(remotePath);
				ftp.quit();
			}
		}
	}
	
	public static void createDir(String remotePath) throws Exception {

		FTPClient ftp = new FTPClient();
		int reply;
		ftp.connect(host, Integer.parseInt(port));
		ftp.login(userName, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
		if (null != ftp) {
			if (StringUtils.isNotBlank(remotePath)) {
				ftp.makeDirectory(remotePath);
				ftp.quit();
			}
		}
	}

	public static FTPFile[] getFiles(String pathName) throws Exception {
		FTPClient ftp = new FTPClient();
		int reply;
		ftp.connect(host, Integer.parseInt(port));
		ftp.login(userName, password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
		ftp.changeWorkingDirectory(pathName);
		FTPFile[] files = ftp.listFiles();
		return files;
	}

}