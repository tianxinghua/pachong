package com.shangpin.iog.stefaniamode.stock;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;

import com.shangpin.iog.stefaniamode.stock.dto.Products;
import org.apache.commons.lang.StringUtils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;



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
		FtpUtil ftpUtil = new FtpUtil();
		ftpUtil.downloadFTP();

	}
}
