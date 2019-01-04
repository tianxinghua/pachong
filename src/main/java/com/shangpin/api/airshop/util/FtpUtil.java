package com.shangpin.api.airshop.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.csvreader.CsvReader;


/**
 *
 * 简单的ftp下载工具
 * 一天用一次，不需要连接池
 * 14-4-29.
 */
public class FtpUtil {
	public static InputStream getFile(String path){
	    FTPClient ftpClient = new FTPClient();
	    String hostName = "192.168.20.219";
	    String userName = "erp@ftp";
	    String password = "erp@ftp";
	    path = "/FedExShipmentLabels/15/02/03/TCI201502030002_O20150130046690_M794629620485.txt";
	    try {
	      ftpClient.connect(hostName, 21);
	      ftpClient.setControlEncoding("UTF-8");
	      ftpClient.login(userName, password);
	      ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	      InputStream inStream =ftpClient.retrieveFileStream(path);;
	      ftpClient.quit();
	      return  inStream;
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    return null;
	  }
	
	public static void main(String[] args) throws IOException {
		InputStream in = getFile("");
		StringBuffer   out   =   new   StringBuffer(); 
	        byte[]   b   =   new   byte[4096]; 
	        for   (int   n;   (n   =   in.read(b))   !=   -1;)   { 
	                out.append(new   String(b,   0,   n)); 
	        } 
	        System.out.println(out.toString());
	}
}
