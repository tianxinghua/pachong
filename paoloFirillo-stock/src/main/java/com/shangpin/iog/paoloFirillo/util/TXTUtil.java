package com.shangpin.iog.paoloFirillo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.csvreader.CsvReader;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.paoloFirillo.dto.TxtDTO;

public class TXTUtil {
	
	private static Logger log = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String name;
	private static String path;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		name = bdl.getString("name");
		path = bdl.getString("path");
	}
	/**
	 * 解析csv文件，将其转换为对象
     * @param remoteFileName  远端文件名称 shangpin.csv
	 * @param clazz DTO类
	 * @param sep 分隔符
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readLocalCSV(Class<T> clazz, String sep)
			throws Exception {
		List<T> dtoList = new ArrayList<T>();
		String[] names = name.split(",");
		for (String namestr : names) {
			
			/*InputStream in = downloadFTP(namestr);
			if(in == null){
				System.out.println("FTP下载失败！！！！！！！！！！");
				log.error("FTP下载失败！！！！！！！！！！");
				System.exit(0);
			}*/
			String dataString = getDataString(namestr);
			String saveFile = saveFile(namestr, dataString);
			String rowString = null;
			String[] split = null;
			List<String> colValueList = null;
			CsvReader cr = null;
			// 解析csv文件
//			cr = new CsvReader(in,Charset.forName("UTF-8"));
			cr = new CsvReader(saveFile);
			
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
					dtoList.add(t);
				}
				System.out.println(a);
			}
//			in.close();
		}
		return dtoList;
	}
	
	public static <T> T fillDTO(T t,List<String> data){
    	try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < data.size(); i++) {
				fields[i].setAccessible(true);
				fields[i].set(t, data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
    }
	public static String saveFile(String name,String data){
		File file = new File(path+name);
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(path+name);
			fwriter.write(data);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return path+name;
	}
	
	public static InputStream downloadFTP(String name){
		FTPClient ftpClient = null;
		InputStream in = null;
        try {  
            ftpClient = new FTPClient();  
            ftpClient.setConnectTimeout(1000*60*10);
            System.out.println("开始连接");
            log.info("开始连接");
            ftpClient.connect("188.217.250.212",21);// 连接FTP服务器  
            boolean login = ftpClient.login("Shangpin", "amanda.lee");
            System.out.println("连接"+login);
            log.info("连接"+login);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory("/"+name);
            System.out.println("读取txt");
//            ftpClient.setBufferSize(1024*1024);
			in = ftpClient.retrieveFileStream(name+".txt");
//            ftpClient.logout();
        }catch(Exception ex){
        	log.error(ex);
        	ex.printStackTrace();
        }
        return in;
	}
	public static String getDataString(String name){
		//PAFYO.csv   SAFYO.csv
		String str = HttpUtil45.get("http://188.217.250.212/"+name, new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10), null);
		return str;
	}
	public static void main(String[] args) {
		//PAFYO.csv   SAFYO.csv
		String str = HttpUtil45.get("http://188.217.250.212/"+"PAFYO.csv", new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10), null);
		
		try {
			List<TxtDTO> readLocalCSV = TXTUtil.readLocalCSV(TxtDTO.class, ";");
			System.out.println(readLocalCSV.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
