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
		logger.info("ip: "+ip+",port: "+port+",usr: "+usrName+",password: "+password+",remote: "+remotePath);
		try {
			
			ftp.connect(ip, port);
			ftp.login(usrName, password);
			ftp.setControlEncoding("UTF-8");
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();  
			ftp.setConnectTimeout(1000*60*60*60); 
			ftp.setDataTimeout(1000*60*60*60); 
			ftp.setControlKeepAliveReplyTimeout(1000*60*60*60);
			ftp.setControlKeepAliveTimeout(1000*60*60*60);  
			if(StringUtils.isNotBlank(remotePath)){
				ftp.changeWorkingDirectory(remotePath);
			}				
			logger.info("ftp连接成功");			
			//下载
			if(null == toBeDowns || toBeDowns.size()<=0){
				return;
			}
			FileOutputStream is = null;
			for(Entry<String, String> enty : toBeDowns.entrySet()){
				try {
					
					File localFile = new File(enty.getKey());
					is = new FileOutputStream(localFile); 
					System.out.println(" file path " + remotePath+enty.getValue());
					boolean bl= ftp.retrieveFile(enty.getValue(), is);
					System.out.println("download " + bl);
					if(null!=is ) is.close();
					if(bl){
						logger.info(enty.getValue()+" 下载成功----------");
					}else{
						logger.info(enty.getValue()+" 下载失败++++++++++");
					}
					 
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
//        	try {
//        		if(null != is){
//            		is.flush();
//            		is.close();
//            	}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}        	
        }
		
	}
	
	public static void main(String[] args) {
		Map<String,String> toBeDownPicMap  = new HashMap<String,String>();
		Map<String,String> downLoadFailedMap = new HashMap<String,String>();
		toBeDownPicMap.put("F:\\tmp\\temp1.jpg", "31_SN402_551_M935_9999901882260_010_532A9289.JPG");
		toBeDownPicMap.put("F:\\tmp\\temp2.jpg", "31_PBMA467V_B603C_1422_9999901824253__532A1660__97__.JPG");
		toBeDownPicMap.put("F:\\tmp\\temp3.jpg", "31_G8GH1T_G7HQJ_S8292_9999901962436__SENZA_TITOLO__794_DI_1___49__.JPG");
		toBeDownPicMap.put("F:\\tmp\\temp4.jpg", "31_2TO641_844_93_9999902016350__SENZA_TITOLO__1009_DI_1___94__.JPG");
		toBeDownPicMap.put("F:\\tmp\\temp5.jpg", "31_PI0321U_19336_8000_9999902151662__532A1564__8__.JPG");
		FTPUtils.downFile("92.223.134.2", 21, "mosuftp", "nter2015£", "", toBeDownPicMap, downLoadFailedMap);
	}
	
}
