package com.shangpin.iog.Della.purchase.common;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.shangpin.iog.common.utils.SendMail;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2015/10/2.
 */
public class MyFtpUtil {
    //private static Logger loggerError = Logger.getLogger("error");
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
    private static ResourceBundle bdl = null;
    private static String localFile = null,remoteFile;
    private static String supplierId = null;
    private static String HOST,PORT,PATH,USER,PASSWORD;
    
    private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String subject = null;
	private static String messageType = null;

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
            
            smtpHost  = bdl.getString("smtpHost");
    		from = bdl.getString("from");
    		fromUserPassword = bdl.getString("fromUserPassword");
    		to = bdl.getString("to");
    		subject = bdl.getString("subject");
    		messageType = bdl.getString("messageType");
    }
    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        try {
            new MyFtpUtil().upLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 上传文件
     */
    public  void upLoad() {
//    	String fileName = null;
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		String dateStr = format.format(new Date());
//		fileName = dateStr + ".csv";    	
    	int i=0;
    	for(;i<10;i++){
    		logger.info("第"+i+"次开始上传订单到香港ftp");  
    		//创建FTPClient
            FTPClient ftp = new FTPClient();
            // 连接服务器
            try {
            	ftp.setRemoteHost("49.213.13.167");
                ftp.setRemotePort(60021);
                ftp.setTimeout(1000*60*30);
                ftp.connect();
                //登陆
                ftp.login("apiftp", "api@888");
                //logger.info("ftp登录成功!!");
                System.out.println("ftp登录成功!!");
                //连接模式
                ftp.setConnectMode(FTPConnectMode.PASV);
                //跳转到指定路径
                //ftp.chdir("/home/apiftp");
                //ASCII方式：传输xml文本文件
                ftp.setType(FTPTransferType.BINARY);
                // 获取 XML文件到本地
                ftp.put(localFile,"a.csv");
                logger.info("上传成功!!");    
                break;
            } catch (Exception e) {
            	loggerError.error(e);            
            }  finally {
                close(ftp);
            }
    	}
    	logger.info("i======"+i);
    	if(i == 10){
    		Thread t = new Thread(new Runnable() {				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {    			
						SendMail.sendGroupMail(smtpHost, from, fromUserPassword, to, "della订单上传香港ftp失败", "della订单上传香港ftp失败", messageType);
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
    public void close(FTPClient ftp){
        try {
            if (null != ftp)
                ftp.quit();
        } catch (IOException e) {
        	loggerError.error(e);        
        } catch (FTPException e) {
        	loggerError.error(e);        
        }
    }


}

