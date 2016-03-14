package com.shangpin.iog.luisaworld.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


public class FTPUtils {

	private static FTPClient ftpClient = null; // FTP 客户端代理
	
	public FTPUtils(String userName,String password,String ip,int port) throws SocketException, IOException{
		FTPClient ftpClient = new FTPClient();  
        ftpClient.connect(ip, port);// 连接FTP服务器  
        ftpClient.login(userName, password);// 登陆FTP服务器              
        ftpClient.setControlEncoding("UTF-8"); // 中文支持  
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
        ftpClient.enterLocalPassiveMode();  
        ftpClient.setConnectTimeout(1000*60*60*10); 
        ftpClient.setControlKeepAliveReplyTimeout(1000*60*60*10);
        ftpClient.setControlKeepAliveTimeout(1000*60*60*10);  
        this.ftpClient = ftpClient;
	}

	/**
	 * 批量下载文件
	 * @param remotePath  目录名 nullable
	 * @param localPath  本地存放地址
	 * @throws IOException 
	 */
	public void bulkDownload(String remotePath,String localPath) throws IOException {

		if(null != ftpClient){
			FTPFile[] fs;
	        if(StringUtils.isNotBlank(remotePath)){
	        	ftpClient.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
	        }			
			fs = ftpClient.listFiles();
			new File(localPath).mkdir();//创建
			for (FTPFile ff : fs) {
				if (ff.isFile()) {
					File localFile = new File(localPath + File.separator
							+ ff.getName()); 
					if(!localFile.exists()){						
						FileOutputStream is = new FileOutputStream(localFile); 
						ftpClient.retrieveFile(ff.getName(), is);
						is.close();
					}	
				}
			}
			
		}else{
			System.out.println("链接FTP失败！！！！"); 
		}
	}
	
	/**
	 * 
	 * @param remotePath ftp文件存放目录
	 * @param remoteFileName  文件名字
	 * @param localPath 本地下载地址
	 * @throws IOException 
	 */
	public void downFile(String remotePath,String remoteFileName,String localPath) throws IOException{
		
		if(null != ftpClient){
			FTPFile[] fs;
	        if(StringUtils.isNotBlank(remotePath)){
	        	ftpClient.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
	        }
			new File(localPath).mkdir();//创建
			File localFile = new File(localPath + File.separator
					+ remoteFileName);
			FileOutputStream is = new FileOutputStream(localFile); 
			ftpClient.retrieveFile(remoteFileName, is);
			is.close();
			
		}else{
			System.out.println("链接FTP失败！！！！"); 
		} 
		
	}

	
}
