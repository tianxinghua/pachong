package com.shangpin.iog.sarenza.util;

import com.csvreader.CsvReader;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.shangpin.iog.sarenza.dto.Product;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * 简单的ftp下载工具 一天用一次，不需要连接池 14-4-29.
 */
public class FtpUtil {
	private static Log log = LogFactory.getLog(FtpUtil.class);
	private static String HOST = "89.185.38.144", PORT = "21",
			USER = "ftp_shangpin", PASSWORD = "Z3!C8=mp";
	public static final String PROPERTIES_FILE_NAME = "conf";
	public static  String path = null;
	public static  String file = null;
	static ResourceBundle bundle = ResourceBundle.getBundle("conf");

	static{
		path = bundle.getString("path");
		file = bundle.getString("file");
	}
	public static boolean isDirExist(String dirname, String[] files) {
		for (int i = 0; i < files.length; i++) {
			if (files[i].indexOf("<DIR>") > -1
					&& files[i].indexOf(dirname) > -1) {
				return true;
			}
		}
		return false;
	}

	public static <T> T fillDTO(T t, List<String> data) {
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

	public static <T> List<T> readLocalCSV(Class<T> clazz) throws Exception {
		List<String> realPaths = null;
		if(StringUtils.isNotBlank(file)){
			realPaths = new ArrayList();
			String ss[] = file.split(",",-1);
			for(String s:ss){
				realPaths.add(s);
			}
			
		}else{
			realPaths = download();
		}
		
		List<T> AlldtoList = new ArrayList<T>();
//		String realPath = "C://AMD//output_11052016.csv";
		for(String realPath : realPaths){
			String rowString = null;
			List<T> dtoList = new ArrayList<T>();
			String[] split = null;
			List<String> colValueList = null;
			CsvReader cr = null;
			// 解析csv文件
			if(realPath!=null){
				cr = new CsvReader(new FileReader(path+ realPath));
				System.out.println("创建cr对象成功");
				// 得到列名集合
				cr.readRecord();
				int a = 0;
				while (cr.readRecord()) {
					a++;
					rowString = cr.getRawRecord();
					if (StringUtils.isNotBlank(rowString)) {
						split = rowString.split("\\|");
						colValueList = Arrays.asList(split);
						T t = fillDTO(clazz.newInstance(), colValueList);
						// 过滤重复的dto。。。sku,
						// dtoSet.add(t);
						dtoList.add(t);
					}
					System.out.println(a);
				}
				AlldtoList.addAll(dtoList);
				cr.close();
				File flie = new File(path+realPath);
				boolean falg = flie.delete();
				 if(falg){
				 System.out.println("文件删除success");
				 }else{
				 System.out.println("文件删除fail");
				 }
			}
		}
		return AlldtoList;
	}

	/**
	 * 下载文件 请注意未使用连接池 另外文件类型已知 要么文件夹要么文件 所以不做复杂判断
	 * 
	 * @param remoteFilePath
	 *            远端文件路径
	 * @param remoteFileName
	 *            远端文件名称
	 * @param localFilePath
	 *            本地文件存放路径
	 */
	public static void main(String[] args) {

//		download();
		try {
			readLocalCSV(Product.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static List<String> download() {
		List<String> filePath = new ArrayList<String>();
		/** 定义FTPClient便利 */
		FTPClient ftp = null;
		String remoteFilePath = "/";
		try {
			/** 创建FTPClient */
			ftp = new FTPClient();
			/** 连接服务器 */
			ftp.setRemoteHost(HOST);

			ftp.setRemotePort(Integer.parseInt(PORT));
			ftp.setTimeout(3600000);
			ftp.connect();

			/** 登陆 */
			ftp.login(USER, PASSWORD);

			/** 连接模式 */
			ftp.setConnectMode(FTPConnectMode.PASV); //

			/**
			 * ASCII方式：只能传输一些如txt文本文件， zip、jpg等文件需要使用BINARY方式
			 * */
			// ftp.setType(FTPTransferType.ASCII);
			ftp.setType(FTPTransferType.BINARY);
//			remoteFilePath = FILE_PATH + "/";
			String[] files = null;
			ftp.chdir(remoteFilePath);

			/**
			 * 切换到主目录，并枚举主目录的所有文件及文件夹 包括日期、文件大小等详细信息 files = ftp.dir(".")，则只有文件名
			 */
			// String[] files = ftp.dir(".", true);

			/** 下载文件 */

			try {
				files = ftp.dir(remoteFilePath);
				if(files!=null&&files.length>=1){
					String d=new SimpleDateFormat("ddMMyyyy").format(new Date());
					for (String fileName : files) {
						String ff = "/output_"+d+".csv";
						
						System.out.println(ff+"=="+fileName);
						if(ff.equals(fileName)){
							filePath.add(fileName);
							System.out.println(fileName);
							ftp.get(path+fileName, fileName);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			/** 如果文件夹不存在，则创建 */
			log.error("success");
		} catch (Exception e) {
			log.error("Demo failed", e);

		} finally {
			/** 断开连接 */
			try {
				if (null != ftp)
					ftp.quit();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FTPException e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}
}
