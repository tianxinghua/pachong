package com.shangpin.iog.Della.purchase.common;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        }  finally {
            close(ftp);
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

