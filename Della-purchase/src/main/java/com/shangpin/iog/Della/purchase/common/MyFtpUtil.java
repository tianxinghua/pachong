package com.shangpin.iog.Della.purchase.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.commons.net.ftp.FTPClient;

import com.shangpin.iog.common.utils.SendMail;

/**
 * Created by Administrator on 2015/10/2.
 */
public class MyFtpUtil {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
    private static ResourceBundle bdl = null;
    
    private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String messageType = null;

    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
            smtpHost  = bdl.getString("smtpHost");
    		from = bdl.getString("from");
    		fromUserPassword = bdl.getString("fromUserPassword");
    		to = bdl.getString("to");
    		messageType = bdl.getString("messageType");
    }
  
    /**
     * Description: 上传文件
     */
    public void upload(String localFile) { 
		int i=0;
		for(;i<10;i++){
			logger.info("第"+i+"次开始上传92.223.134.2开始");
			FTPClient ftp = new FTPClient(); 
			FileInputStream fis = null;
	        try {
	        	
//	        	ftp.setRemoteHost("92.223.134.2");
//                ftp.setRemotePort(21);
//                ftp.setTimeout(1000*60*30);
//                ftp.connect();
//                //登陆
//                ftp.login("mosuftp", "inter2015£");
//                logger.info("ftp登录成功!!");
//                //连接模式
//                ftp.setConnectMode(FTPConnectMode.PASV);
//                //跳转到指定路径
//                ftp.chdir("/MOSU/Orders");
//                //ASCII方式：传输xml文本文件
//                ftp.setType(FTPTransferType.BINARY);
//                // 获取 XML文件到本地
//                ftp.put(localFile,new File(localFile).getName());
//                logger.info(localFile+" 上传92.223.134.2成功!!!!!!!!!!!!!!!!!");    
//                break;
	        	
	        	ftp.setDefaultTimeout(1000 * 60 * 5);
				ftp.setConnectTimeout(1000 * 60 * 5);
				ftp.enterLocalActiveMode();
				ftp.connect("92.223.134.2", 21);
				ftp.login("mosuftp", "inter2015£");
				ftp.setControlEncoding("UTF-8");
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftp.changeWorkingDirectory("/MOSU/Orders");
				ftp.enterLocalPassiveMode();
				File srcFile = new File(localFile);
				fis = new FileInputStream(srcFile);
				logger.info("ftp连接成功");
				ftp.storeFile(srcFile.getName(), fis);
				logger.info(srcFile.getName()+"上传92.223.134.2成功!!!!!!!!!!!!!!!!!");
				break;
			} catch (Exception e) {	
				logger.info(e.getMessage(),e); 
				loggerError.error(e.getMessage(),e); 
			}finally{
//				close(ftp);
				try {
					if(null != fis){
						fis.close();
					}
					ftp.disconnect();
				} catch (IOException e) {
					loggerError.error(e);
				}
			}
		}
    	logger.info("i======"+i);
    	if(i == 10){
    		new Thread(new Runnable() {				
				@Override
				public void run() {
					try {    			
						SendMail.sendGroupMail(smtpHost, from, fromUserPassword, to, "della订单上传ftp失败", "della订单上传ftp失败", messageType);
					} catch (Exception e) {
						loggerError.error(e);
					}
				}
			});
    	}        
    }
    
    /**
     *断开连接
     * @param ftp 客户端
     */
//    public void close(FTPClient ftp){
//        try {
//            if (null != ftp)
//                ftp.quit();
//        } catch (IOException e) {
//        	loggerError.error(e);        
//        } catch (FTPException e) {
//        	loggerError.error(e);        
//        }
//    }

}

