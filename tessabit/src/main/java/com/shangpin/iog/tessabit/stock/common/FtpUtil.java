package com.shangpin.iog.tessabit.stock.common;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by wangyuzhi on 2015/9/11.
 */
public class FtpUtil {
    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Constant.URL=" + Constant.URL);
        try {
            FtpUtil.downLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 下载文件
     */
    public static void downLoad() {
        //创建FTPClient
        FTPClient ftp = new com.enterprisedt.net.ftp.FTPClient();
        // 连接服务器
        try {
            ftp.setRemoteHost(Constant.URL);
            ftp.setRemotePort(Integer.parseInt(Constant.PORT));
            ftp.setTimeout(3600000);
            ftp.connect();
            //登陆
            ftp.login(Constant.USER, Constant.PASSWORD);
            //连接模式
            ftp.setConnectMode(FTPConnectMode.PASV);
            //ASCII方式：传输xml文本文件
            ftp.setType(FTPTransferType.ASCII);
            // 获取 XML文件到本地
            ftp.get(StringUtil.getLocalFileName(), Constant.SERVER_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        }  finally {
            FtpUtil.close(ftp);
            return;
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
