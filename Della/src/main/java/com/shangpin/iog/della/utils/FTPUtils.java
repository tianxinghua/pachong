package com.shangpin.iog.della.utils;

import java.io.File;
import java.io.FileInputStream;
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
        ftpClient.setDataTimeout(1000*60*60*3); 
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
						//is.close();
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
//			is.close();
			
		}else{
			System.out.println("链接FTP失败！！！！"); 
		} 
		
	}
	
	
	/**  
     * 上传文件或文件夹  
     * @param file 上传的文件或文件夹  
     * @throws Exception  
     */    
    private void upload(File file) throws Exception{      
        if(file.isDirectory()){           
        	ftpClient.makeDirectory(file.getName());                
        	ftpClient.changeWorkingDirectory(file.getName());      
            String[] files = file.list();             
            for (int i = 0; i < files.length; i++) {      
                File file1 = new File(file.getPath()+File.separator+files[i] );      
                if(file1.isDirectory()){      
                    upload(file1);      
                    ftpClient.changeToParentDirectory();      
                }else{                    
                    File file2 = new File(file.getPath()+File.separator+files[i]);      
                    FileInputStream input = new FileInputStream(file2);      
                    ftpClient.storeFile(file2.getName(), input);      
                    input.close();                            
                }                 
            }      
        }else{      
            File file2 = new File(file.getPath());      
            FileInputStream input = new FileInputStream(file2);      
            ftpClient.storeFile(file2.getName(), input);      
            input.close();        
        }      
    }  
    
    public void logout() throws IOException{
    	if(null != ftpClient){
    		try {
				ftpClient.logout();
			} catch (IOException e) {				
				e.printStackTrace();
				throw e; 
			}
    	}
    }

	
}
