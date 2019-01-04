package com.shangpin.iog.brunarosso20.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.logger.LoggerUtil;


public class FTPUtils {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
	private static LoggerUtil logError = LoggerUtil.getLogger("error");

	/**
	 * 下载当天最新库存文件，返回文件名称
	 * @param ip
	 * @param port
	 * @param usrName
	 * @param password
	 * @param remotePath
	 * @param localPath
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String downFile(String ip,int port,String usrName,String password,String remotePath ,String localPath){
		FTPClient ftp = new FTPClient();
		FileOutputStream is = null;
		try {
			ftp.setConnectTimeout(1000 * 60 * 60);
			ftp.connect(ip, port);
			ftp.login(usrName, password);
			ftp.setControlEncoding("UTF-8");
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			if(StringUtils.isNotBlank(remotePath)){
				ftp.changeWorkingDirectory(remotePath);
			}	
			logger.info("ftp连接成功");
			
			String tobeDownLoadFile = null;
			
			//查找出当天的最新的库存文件
			List<String> todaysFiles = new ArrayList<String>();
			String todayPattern = "Disponibilita_"+DateTimeUtil.getDateTime("yyyyMMdd");
			for(String fileName : ftp.listNames()){
				if(StringUtils.isNotBlank(fileName) && fileName.contains(todayPattern)){
					todaysFiles.add(fileName);
				}
			}
			if(todaysFiles.size()>0){
				Map<Integer,String> hashMap = new HashMap<Integer,String>();
				List<Integer> tmpList = new ArrayList<Integer>();
				for(String fileName : todaysFiles){
					Integer time = Integer.parseInt(fileName.substring(fileName.lastIndexOf("_")+1, fileName.lastIndexOf(".")));
					hashMap.put(time, fileName);
					tmpList.add(time);
				}
				//排序,从小到大
				Collections.sort(tmpList, new Comparator() {
				      @Override
				      public int compare(Object o1, Object o2) {
				        return ((Integer) o1).compareTo((Integer) o2);
				      }
				});
				
				tobeDownLoadFile = hashMap.get(tmpList.get(tmpList.size()-1)); 				
				
			}else{
				//如果没有类似Disponibilita_20160617_120041.xml的文件，则下载总文件Disponibilita.xml
				tobeDownLoadFile = "Disponibilita.xml";
			}
			//下载
			new File(localPath).mkdir();//创建
			File localFile = new File(localPath + File.separator
					+ tobeDownLoadFile);
			is = new FileOutputStream(localFile); 
			ftp.retrieveFile(tobeDownLoadFile, is);	
			
			return tobeDownLoadFile;
			
        }catch(Exception e){
        	e.printStackTrace();
        	logError.error(e); 
        }finally{
        	try {
        		if(null != is){
            		is.flush();
            		is.close();
            	}
			} catch (Exception e2) {
				e2.printStackTrace();
			}        	
        }
		
		return null;
	}
	
	/**
	 * 根据开始时间startDate，下载从startDate开始到最新时段的所有文件，并返回文件名的list
	 * @param ip
	 * @param port
	 * @param usrName
	 * @param password
	 * @param remotePath
	 * @param localPath
	 * @param startDate 格式必须是yyyyMMdd
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> bulkDownload(String ip,int port,String usrName,String password,String remotePath ,String localPath,String startDate){
		if(StringUtils.isBlank(startDate)){
			return null;
		}else{
			FTPClient ftp = new FTPClient();
			FileOutputStream is = null;
			try {
				List<String> tobeDownLoadList = new ArrayList<String>();
				
				ftp.setConnectTimeout(1000 * 60 * 60);
				ftp.connect(ip, port);
				ftp.login(usrName, password);
				ftp.setControlEncoding("UTF-8");
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				if(StringUtils.isNotBlank(remotePath)){
					ftp.changeWorkingDirectory(remotePath);
				}	
				logger.info("ftp连接成功");
				//查找出规定时间及其以后的文件
				List<String> todaysFiles = new ArrayList<String>();
				String todayPattern = "Disponibilita_";
				for(String fileName : ftp.listNames()){
					if(StringUtils.isNotBlank(fileName) && fileName.contains(todayPattern)){
						Integer time = Integer.parseInt(fileName.substring(fileName.indexOf("_")+1, fileName.lastIndexOf("_")));
						if(time >= Integer.parseInt(startDate)){
							todaysFiles.add(fileName);
						}
					}
				}
				if(todaysFiles.size()>0){
					Map<Long,String> hashMap = new HashMap<Long,String>();
					List<Long> tmpList = new ArrayList<Long>();
					for(String fileName : todaysFiles){
						Long time = Long.parseLong(fileName.substring(fileName.indexOf("_")+1, fileName.lastIndexOf("_"))+fileName.substring(fileName.lastIndexOf("_")+1, fileName.lastIndexOf(".")));
						hashMap.put(time, fileName);
						tmpList.add(time);
					}
					//排序,从小到大
					Collections.sort(tmpList, new Comparator() {
					      @Override
					      public int compare(Object o1, Object o2) {
					        return ((Long) o1).compareTo((Long) o2);
					      }
					});
					for(Long key : tmpList){
						tobeDownLoadList.add(hashMap.get(key));
					}
					
				}else{
					//如果没有类似Disponibilita_20160617_120041.xml的文件，则下载总文件Disponibilita.xml
					tobeDownLoadList.add("Disponibilita.xml");
				}
				
				//下载
				new File(localPath).mkdir();//创建
				for(String fileName : tobeDownLoadList){
					try {
						File localFile = new File(localPath + File.separator
								+ fileName);
						is = new FileOutputStream(localFile); 
						ftp.retrieveFile(fileName, is);	
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				return tobeDownLoadList;
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(null != is){
						is.flush();
						is.close();
					} 
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			return null;
		}
	}
	
}
