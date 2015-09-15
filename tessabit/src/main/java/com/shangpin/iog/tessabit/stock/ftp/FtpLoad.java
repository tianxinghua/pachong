package com.shangpin.iog.tessabit.stock.ftp;

import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.shangpin.iog.tessabit.stock.common.Constant;
import com.shangpin.iog.tessabit.stock.common.FtpUtil;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2015/9/10.
 */
public class FtpLoad {

    final Logger logger = Logger.getLogger(this.getClass());
    /**
     * Description: 下载文件
     */
    public static void downFile() {
        // 定义FTPClient便利
        com.enterprisedt.net.ftp.FTPClient ftp = null;
        try {
            //创建FTPClient
            ftp = new com.enterprisedt.net.ftp.FTPClient();
            // 连接服务器
            ftp.setRemoteHost(Constant.URL);
            ftp.setRemotePort(Integer.parseInt(Constant.PORT));
            ftp.setTimeout(3600000);
            ftp.connect();

            //登陆
            ftp.login(Constant.USER, Constant.PASSWORD);

            //连接模式
            ftp.setConnectMode(FTPConnectMode.PASV);      //

            //ASCII方式：传输xml文本文件
            ftp.setType(FTPTransferType.ASCII);

            ftp.get(Constant.LOCAL_FILE, Constant.SERVER_FILE);


        } catch (Exception e) {

        } finally {
            FtpUtil.close(ftp);
        }
    }



    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Constant.URL=" + Constant.URL);
        FtpLoad.downFile();
    }
}
