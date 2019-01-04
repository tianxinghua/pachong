package com.shangpin.iog.stefaniamode.util;

import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.stefaniamode.dto.Products;



public class FtpUtil {
	
	private static Logger log = Logger.getLogger("info");

	
	public Products downloadFTP(){
		Products  products = null;
		FTPClient ftpClient = null;
		InputStream in = null;
        try {  
            ftpClient = new FTPClient();  
            ftpClient.setConnectTimeout(1000*60*30);
            System.out.println("开始连接");
            log.info("开始连接");
            ftpClient.connect("185.2.4.65");// 连接FTP服务器
            boolean login = ftpClient.login("smproducts@stefaniamode.com", "mxZrje224w");
            System.out.println("连接"+login);
            log.info("连接"+login);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory("/");

			String filename = "StefaniaMode.xml";

            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setDataTimeout(1000*60*60);
			in = ftpClient.retrieveFileStream(filename);
			int i = 0;
			while(null == in && i<10){
				in = ftpClient.retrieveFileStream(filename);
				i++;
			}
			log.info("================"+i+"==================="); 
			if(in != null){

				products = ObjectXMLUtil.xml2Obj(Products.class, in);
			}			
            
        }catch(Exception ex){
        	log.error(ex.toString());
        	ex.printStackTrace();
        }finally{
        	try {
        		if(null != in){
            		in.close();
            	}
        		ftpClient.logout();
        		
			} catch (Exception e) {
				e.printStackTrace();
			}        	
        }
		return products;
	}

	public static void main(String[] args) {
//		FtpUtil ftpUtil = new FtpUtil();
//		ftpUtil.downloadFTP();

	}
}
