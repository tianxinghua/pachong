package com.shangpin.iog.Della.purchase;

import java.io.IOException;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;

public class Test2 {
	
	public static void main(String[] args) {
		upLoad();
	}
	
	/**
     * Description: 上传文件
     */
    public static  void upLoad() {
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
            ftp.put("E:/2016-01-11.csv","b.csv");
            //logger.info("上传成功!!");
            System.out.println("上传成功!!");
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
    public static void close(FTPClient ftp){
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
