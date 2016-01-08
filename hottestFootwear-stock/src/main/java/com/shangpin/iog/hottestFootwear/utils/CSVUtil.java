package com.shangpin.iog.hottestFootwear.utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.csvreader.CsvReader;

public class CSVUtil {
	
	private static Logger log = Logger.getLogger("error");
	/**
	 * 解析csv文件，将其转换为对象
     * @param remoteFileName  远端文件名称 shangpin.csv
	 * @param clazz DTO类
	 * @param sep 分隔符
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readLocalCSV(Class<T> clazz, String sep,Map<Integer,Integer> colNum)
			throws Exception {
		
		InputStream in = downloadFTP();
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
		//cr = new CsvReader(result);
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
				T t = fillDTO(clazz.newInstance(), colValueList,colNum);
				dtoList.add(t);
			}
			System.out.println(a);
		}
		
		return dtoList;
	}
	
	public static <T> T fillDTO(T t,List<String> data,Map<Integer,Integer> colNum){
    	try {
    		Field[] fields = t.getClass().getDeclaredFields();
    		for (int i = 0; i < colNum.size(); i++) {
    			fields[i].setAccessible(true);
    			fields[i].set(t, data.get(colNum.get(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
    }
	
	
	public static InputStream downloadFTP(){
		FTPClient ftpClient = null;
		InputStream in = null;
        try {  
            ftpClient = new FTPClient();  
            ftpClient.setConnectTimeout(3600000);
            ftpClient.connect("ftp.adrive.com");// 连接FTP服务器  
            ftpClient.login("shangpinftp123@gmail.com", "Shangpin-1234");
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.enterLocalPassiveMode();
            in = ftpClient.retrieveFileStream("shangpin.csv");
        }catch(Exception ex){
        	log.error(ex);
        	ex.printStackTrace();
        }
        return in;
	}
}
