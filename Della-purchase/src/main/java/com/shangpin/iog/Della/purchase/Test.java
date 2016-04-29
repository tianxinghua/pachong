package com.shangpin.iog.Della.purchase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;

public class Test {
	
	 private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
	 private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
	public static void main(String[] args) {
		 testUpload(); 
		
	}
	
	public static void testUpload() { 
        FTPClient ftp = new FTPClient(); 
        FileInputStream fis = null ; 
        OutputStream out = null;
        try {
        	ftp.setConnectTimeout(1000*60*60);
        	ftp.setDataTimeout(1000*60*60);
        	
   		 ftp.connect("92.223.134.2", 21);
   		 ftp.login("mosuftp", "inter2015£");
   		 
   		 ftp.setControlEncoding("UTF-8" ); 
   		 
   		 ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 	
   		 
   		 File srcFile = new File("E:/a.txt"); ///usr/local/apporder/Della/a.csv
   		 fis = new FileInputStream(srcFile);
   		 logger.info("ftp连接成功");
   		 System.out.println("ftp连接成功");
   		 
   		 ftp.changeWorkingDirectory("/MOSU/Orders"); 
   		 //ftp.setBufferSize(1024); 
   		
   		logger.info("开始上传");
  		 System.out.println("开始上传");
   		 ftp.storeFile("2016-04-21.csv" , fis); 
   		 logger.info("上传成功");
   		 System.out.println("上传成功");
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！" , e); 
		}finally{
			 //IOUtils.closeQuietly(fis); 
			try { 
			 ftp.disconnect(); 
	            } catch (IOException e) { 
	                e.printStackTrace(); 
	                throw new RuntimeException("关闭FTP连接发生异常！" , e); 
	            } 
		}

}
}