package com.shangpin.iog.levelgroup.purchase.common;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.shangpin.iog.common.utils.DateTimeUtil;
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
    private static Logger loggerError = Logger.getLogger("error");
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static ResourceBundle bdl = null;
    private static String localFile = null,remoteFile;
    private static String localFile2 = null;
    private static String supplierId = null;
    private static String HOST,PORT,PATH,PATH2,USER,PASSWORD;
    
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
            localFile2 = bdl.getString("localFile2");
            remoteFile = bdl.getString("remoteFile");
            supplierId = bdl.getString("supplierId");
            HOST = bdl.getString("host");
            PORT = bdl.getString("port");
            PATH = bdl.getString("path");
            PATH2 = bdl.getString("path2");
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
    	String fileName = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = format.format(new Date());
		fileName = dateStr + ".csv";
        
		int i = 0;
		while(i<10){
			//创建FTPClient
	        FTPClient ftp = new FTPClient();
	        // 连接服务器
	        try {
	            ftp.setRemoteHost(HOST);
	            ftp.setRemotePort(Integer.parseInt(PORT));
	            ftp.setTimeout(1000*60*30);
	            ftp.connect();
	            //登陆
	            ftp.login(USER, PASSWORD);
	            //连接模式
	            ftp.setConnectMode(FTPConnectMode.PASV);
	            ftp.chdir(PATH);
	            //ASCII方式：传输xml文本文件
	            ftp.setType(FTPTransferType.ASCII);
	            // 获取 XML文件到本地
	            ftp.put(localFile,fileName);
	            logger.info("文件"+fileName+"上传成功!");
	            break;
	        } catch (Exception e) {
//	            try {
//	            	this.upLoad();
//					SendMail.sendGroupMail(smtpHost, from, fromUserPassword, to, subject, "levelgroup订单上传失败", messageType);
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				} 
	        	loggerError.error(e);	            
	        }  finally {
	        	i++;
	            close(ftp);
	        }
		}
		
		if(i == 10){
			try {
				SendMail.sendGroupMail(smtpHost, from, fromUserPassword, to, subject, "levelgroup订单上传ftp("+HOST+") 10遍都没有成功。。。。。请查找原因。", messageType);
			} catch (Exception e) {
				loggerError.error(e);
			}
		}
		
    }
    
    public void uploadCancel(){

    	String fileName = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = format.format(new Date());
		fileName = dateStr + ".csv";
        
		int i=0;
		while(i<10){
			//创建FTPClient
	        FTPClient ftp = new FTPClient();
	        // 连接服务器
	        try {
	            ftp.setRemoteHost(HOST);
	            ftp.setRemotePort(Integer.parseInt(PORT));
	            ftp.setTimeout(1000*60*30);
	            ftp.connect();
	            //登陆
	            ftp.login(USER, PASSWORD);
	            //连接模式
	            ftp.setConnectMode(FTPConnectMode.PASV);
	            ftp.chdir(PATH2);
	            //ASCII方式：传输xml文本文件
	            ftp.setType(FTPTransferType.ASCII);
	            // 获取 XML文件到本地
	            ftp.put(localFile2,fileName);
	            break;
	        } catch (IOException e) {
	        	loggerError.error(e);
	        } catch (FTPException e) {
	        	loggerError.error(e);
	        }  finally {
	        	i++;
	            close(ftp);
	        }
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
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        }
    }


}

