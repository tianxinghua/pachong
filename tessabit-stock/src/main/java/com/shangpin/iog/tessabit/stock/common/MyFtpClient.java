package com.shangpin.iog.tessabit.stock.common;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by Administrator on 2015/10/2.
 */
public class MyFtpClient {
    private static Logger loggerError = Logger.getLogger("error");
    private int i = 0;
    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        try {
            new MyFtpClient().downLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 下载文件
     */
    public  boolean downLoad() {
        //创建FTPClient
        FTPClient ftp = new com.enterprisedt.net.ftp.FTPClient();
        // 连接服务器
        try {
            ftp.setRemoteHost(Constant.URL);
            ftp.setRemotePort(Integer.parseInt(Constant.PORT));
            ftp.setTimeout(1000*60*30);
            ftp.connect();
            //登陆
            ftp.login(Constant.USER, Constant.PASSWORD);
            //连接模式
            ftp.setConnectMode(FTPConnectMode.PASV);
            //ASCII方式：传输xml文本文件
            ftp.setType(FTPTransferType.ASCII);

            //更改目录
            ftp.chdir(Constant.REMOTE_PATH);
            // 获取 XML文件到本地
            ftp.get(new StringUtil().getLocalFileName(),Constant.REMOTE_PATH + "/"+ Constant.SERVER_FILE);
        } catch (Exception e){
            loggerError.error("Exception:"+e.getMessage());
            System.exit(0);
        } finally {
            close(ftp);
        }
        return true;
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
            loggerError.error("IOException:" + e.getMessage());
        } catch (FTPException e) {
            loggerError.error("FTPException:" + e.getMessage());
        }
    }
}

