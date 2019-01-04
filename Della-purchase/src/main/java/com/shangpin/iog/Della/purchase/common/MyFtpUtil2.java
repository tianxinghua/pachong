package com.shangpin.iog.Della.purchase.common;

import org.apache.log4j.Logger;
//import org.apache.poi.util.IOUtils;
//import org.apache.commons.io.IOUtils; 
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2015/10/2.
 */
public class MyFtpUtil2 {
    //private static Logger loggerError = Logger.getLogger("error");
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
    private static ResourceBundle bdl = null;
    private static String localFile = null,remoteFile;
    private static String supplierId = null;
    private static String HOST,PORT,PATH,USER,PASSWORD;

    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
            localFile = bdl.getString("localFile");
            remoteFile = bdl.getString("remoteFile");
            supplierId = bdl.getString("supplierId");
            HOST = bdl.getString("host");
            PORT = bdl.getString("port");
            PATH = bdl.getString("path");
            USER = bdl.getString("user");
            PASSWORD = bdl.getString("password");
    }
    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        try {
            new MyFtpUtil2().upLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 上传文件
     */
    public static void upLoad() {
    	
    	String fileName = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = format.format(new Date());
		fileName = dateStr + ".csv";
		
    	FTPClient ftp = new FTPClient(); 
    	FileInputStream fis = null ; 
    	 
    	 try {
    		 ftp.setConnectTimeout(1000*60*60);
    		 ftp.connect("92.223.134.2", 21);
    		 ftp.login("mosuftp", "inter2015£");
    		 
    		 File srcFile = new File(localFile); 
    		 logger.info("localFile =" + localFile);
    		 fis = new FileInputStream(srcFile);
    		 logger.info("ftp连接成功");
//    		 logger.info("fis = " + fis);
    		 
    		 ftp.changeWorkingDirectory(PATH); 
    		 //ftp.setBufferSize(1024); 
    		 ftp.setControlEncoding("UTF-8" ); 
    		 
    		 ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
    		 ftp.storeFile(fileName , fis); 
    		 logger.info("上传成功");
			
		} catch (Exception e) {
			loggerError.error("发生错误" + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！" , e); 
		}finally{
			 //IOUtils.closeQuietly(fis); 
			try { 
				if(null!=fis) fis.close();
			    if(null!=ftp)  ftp.disconnect(); 
	            } catch (IOException e) { 
	            	loggerError.error("关闭错误" + e.getMessage());
	                e.printStackTrace(); 
	                throw new RuntimeException("关闭FTP连接发生异常！" , e); 
	            } 
		}
    	
    }

//    /**
//     *断开连接
//     * @param ftp 客户端
//     */
//    public void close(FTPClient ftp){
//        try {
//            if (null != ftp)
//                ftp.quit();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}

