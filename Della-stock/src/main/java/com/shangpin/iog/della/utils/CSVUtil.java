package com.shangpin.iog.della.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	private static String HOST="92.223.134.2",PORT="21",USER="mosuftp",PASSWORD="inter2015£";

	/**
	 * 解析csv文件，将其转换为对象
     * @param remoteFileName  远端文件名称 SKU_INVENTORY_STOCK.CSV
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
				System.out.println(rowString);
				split = rowString.split(sep);
				colValueList = Arrays.asList(split);
				T t = fillDTO(clazz.newInstance(), colValueList);
				// 过滤重复的dto。。。sku,
				// dtoSet.add(t);
				dtoList.add(t);
			}
			System.out.println(a);
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
	
	/**
     * 下载文件
     * 请注意未使用连接池  另外文件类型已知 要么文件夹要么文件  所以不做复杂判断
     * @param remoteFilePath 远端文件路径
     * @param remoteFileName  远端文件名称 SKU_INVENTORY_STOCK.CSV
     * @param localFilePath  本地文件存放路径
     */

//    public static String download(String remoteFilePath,String remoteFileName,String localFilePath){
//
//        /** 定义FTPClient便利 */
//        FTPClient ftp = null;
//        try
//        {
//            /** 创建FTPClient */
//            ftp = new FTPClient();
//            /** 连接服务器 */
//            ftp.setRemoteHost(HOST);
//            ftp.setRemotePort(Integer.parseInt(PORT));
//            ftp.setTimeout(3600000);
//            ftp.connect();
//            /** 登陆 */
//            ftp.login(USER, PASSWORD);
//            /** 连接模式 */
//            ftp.setConnectMode(FTPConnectMode.PASV);      //
//
//            /** ASCII方式：只能传输一些如txt文本文件，
//             * zip、jpg等文件需要使用BINARY方式
//             * */
//            //ftp.setType(FTPTransferType.ASCII);
//            ftp.setType(FTPTransferType.BINARY);
//            ftp.chdir(remoteFilePath);
//
//            /** 切换到主目录，并枚举主目录的所有文件及文件夹
//             * 包括日期、文件大小等详细信息
//             * files = ftp.dir(".")，则只有文件名
//             */
////            String[] files = ftp.dir(".", true);
//
//
//
//			log.error("file=" + remoteFilePath + "/" + remoteFileName);
//			/** 下载文件 */
//
//			File attachments = new File(localFilePath);
//			/** 如果文件夹不存在，则创建 */
//			if (!attachments.exists()) {
//				attachments.mkdir();
//			}
//			if(localFilePath.endsWith("/")){
//				localFilePath = localFilePath + "/" + remoteFileName;
//			}else{
//				localFilePath = localFilePath+ remoteFileName;
//			}
//			ftp.get(localFilePath,
//					"/".equals(remoteFilePath) ? remoteFilePath
//							+ remoteFileName : remoteFilePath + "/"
//							+ remoteFileName);
//
//			log.error("success");
//
//			return localFilePath;
//
//        } catch (Exception e)
//        {
//            log.error("ftp下载文件失败  "+e.getMessage());
//            e.printStackTrace();
//
//        }finally {
//            /** 断开连接   */
//            try {
//                if(null!=ftp) ftp.quit();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (FTPException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return null;
//    }
	
	
}
