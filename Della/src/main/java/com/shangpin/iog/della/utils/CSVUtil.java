package com.shangpin.iog.della.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.csvreader.CsvReader;

public class CSVUtil {
	
	private static Logger log = Logger.getLogger("error");
	private static String HOST="92.223.134.2",USER="mosuftp",PASSWORD="inter2015£";

	/**
	 * 解析csv文件，将其转换为对象
	 * @param remoteFileName 远端文件名称 SKU_INVENTORY_STOCK.CSV
	 * @param clazz DTO类
	 * @param sep 分隔符
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readLocalCSV(String remoteFileName,Class<T> clazz, String sep)
			throws Exception {
		InputStream in = downloadFTP(remoteFileName);
		if(in == null){
			System.out.println("FTP下载失败！！！！！！！！！！");
			log.error("FTP下载失败！！！！！！！！！！");
			System.exit(0);
		}
		
		String rowString = null;
		List<T> dtoList = new ArrayList<T>();
		String[] split = null;
		List<String> colValueList = null;
		CsvReader cr = null;
		// 解析csv文件
		cr = new CsvReader(in,Charset.forName("UTF-8"));
		System.out.println("创建cr对象成功");
		// 得到列名集合
		cr.readRecord();
		int a = 0;
		while (cr.readRecord()) {
			a++;
			rowString = cr.getRawRecord();
			if (StringUtils.isNotBlank(rowString)) {
				split = rowString.split(sep);
				colValueList = Arrays.asList(split);
				T t = fillDTO(clazz.newInstance(), colValueList);
				// 过滤重复的dto。。。sku,
				// dtoSet.add(t);
				dtoList.add(t);
			}
			//System.out.println(a);
		}
		
		return dtoList;
	}
	
	public static <T> T fillDTO(T t,List<String> data){
    	try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				fields[i].set(t, data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
    }
	
	/**
	 * 
	 * @param remoteFileName 远端文件名称 SKU_INVENTORY_STOCK.CSV
	 * @return
	 */
	public static InputStream downloadFTP(String remoteFileName){
		FTPClient ftpClient = null;
		InputStream in = null;
        try {  
            ftpClient = new FTPClient();  
            ftpClient.connect("92.223.134.2", 21);// 连接FTP服务器  
            ftpClient.login("mosuftp", "inter2015£");// 登陆FTP服务器  
            ftpClient.setControlEncoding("UTF-8"); // 中文支持  
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
            ftpClient.enterLocalPassiveMode();  
            ftpClient.changeWorkingDirectory("/MOSU/");  
            in = ftpClient.retrieveFileStream(remoteFileName);
            
        }catch(Exception ex){
        	log.error(ex);
        	ex.printStackTrace();
        }
        return in;
	}
	
	
	
}
