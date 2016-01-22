package com.shangpin.iog.smets.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.csvreader.CsvReader;
import com.shangpin.iog.smets.dto.TxtDTO;

public class TXTUtil {
	
	private static Logger log = Logger.getLogger("info");
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
		InputStream in = downloadFTP();
		if(in == null){
			System.out.println("FTP下载失败！！！！！！！！！！");
			log.error("FTP下载失败！！！！！！！！！！");
			System.exit(0);
		}
//		
//	 	File f = new File("D:/smetssssssssss.txt");
//    	if (!f.exists()) {
//			f.createNewFile();
//		}
//    	FileOutputStream fs = new FileOutputStream("D:/smetssssssssss.txt");
//    	byte[] buffer = new byte[1204];
//    	int length;
//        int bytesum = 0;
//        int byteread = 0;
//    	while ((byteread = in.read(buffer)) != -1) {
//    		bytesum += byteread;
//    		fs.write(buffer, 0, byteread);
//    	}
//		fs.close();
//		
//		FileReader fr = new FileReader("D:/smetssssssssss.txt");
//		BufferedReader br = new BufferedReader(fr);
//		while(br.read()!=-1){
//			System.out.println(br.readLine());
//		}
//		
		String rowString = null;
		String[] split = null;
		List<String> colValueList = null;
		CsvReader cr = null;
		// 解析csv文件
		//cr = new CsvReader(result);
		
		cr = new CsvReader(in,Charset.forName("unicode"));
		System.out.println("创建cr对象成功");
		// 得到列名集合
//		cr.readRecord();
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
		in.close();
		return dtoList;
	}
	
	public static <T> T fillDTO(T t,List<String> data){
    	try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < data.size(); i++) {
				fields[i].setAccessible(true);
				fields[i].set(t,data.get(i));
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
            ftpClient.setConnectTimeout(1000*60*20);
            System.out.println("开始连接");
            log.info("开始连接");
            ftpClient.connect("194.154.193.146");// 连接FTP服务器  
            boolean login = ftpClient.login("shangpin", "SmetsXShangpin");
            System.out.println("连接"+login);
            log.info("连接"+login);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory("/Connexion CEGID");
			String[] names = ftpClient.listNames();
			String filename = getFileName(names,new Date());
            System.out.println("读取txt");
            ftpClient.setControlEncoding("UTF-8");
			in = ftpClient.retrieveFileStream(filename);
            ftpClient.logout();
        }catch(Exception ex){
        	log.error(ex);
        	ex.printStackTrace();
        }
        return in;
	}
	private static String getFileName(String[] names,Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH");
		String dateStr = sdf.format(date);
        String file = "";
        for (int i = names.length-1; i>=0; i--) {
        	if (names[i].contains(dateStr)) {
				file = names[i];
				break;
			}
		}
        if (StringUtils.isBlank(file)) {
			file = getFileName(names, new Date(date.getTime()-3600000));
		}
		return file;
	}
	public static void main(String[] args) {
		
		
		try {
			List<TxtDTO> list = TXTUtil.readLocalCSV(TxtDTO.class, ";");
			System.out.println(list.size());
			for (TxtDTO txtDTO : list) {
				System.out.println(txtDTO.getSkuId()+"====="+txtDTO.getStock());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
