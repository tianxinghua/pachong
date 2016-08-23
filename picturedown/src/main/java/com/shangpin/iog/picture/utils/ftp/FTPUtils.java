package com.shangpin.iog.picture.utils.ftp;



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
import java.util.Map.Entry;

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
	 * @param toBeDowns
	 * @param downLoadFailedMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void downFile(String ip,int port,String usrName,String password,String remotePath ,Map<String,String> toBeDowns,Map<String,String> downLoadFailedMap){
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
			//下载
			if(null == toBeDowns || toBeDowns.size()<=0){
				return;
			}
			for(Entry<String, String> enty : toBeDowns.entrySet()){
				try {
					File localFile = new File(enty.getKey());
					is = new FileOutputStream(localFile); 
					ftp.retrieveFile(enty.getValue(), is);	
					logger.info(enty.getKey()+" 下载成功----------"); 
				} catch (Exception e) {
					e.printStackTrace();
					logError.error(e.toString());
					downLoadFailedMap.put(enty.getKey(), enty.getValue());
				}
				
			}
						
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
		
	}
	
}
