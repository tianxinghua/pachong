package com.shangpin.iog.linoricci.stock.common;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by wangyuzhi on 2015/10/2.
 */
public class MyFtpClient {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
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
        boolean isOK = true;
        //创建FTPClient
        FTPClient ftp = new com.enterprisedt.net.ftp.FTPClient();
        // 连接服务器
        try {
            System.out.println(Constant.URL);
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
            //定位
            ftp.chdir(Constant.REMOTE_PATH);
            String[] fileArr = ftp.dir("Disponibilita_*",true);
            // 获取 XML库存文件到本地
            String serFile = lastName(lastName(fileArr).split(" "));
            logger.info("down load server file "+ serFile +" to local:"+Constant.LOCAL_STOCK_FILE);
            ftp.get(Constant.LOCAL_STOCK_FILE, serFile);
        } catch (IOException e) {
            loggerError.error("load linoricci data fail,IOException："+e.getMessage());
            isOK = false;
        } catch (FTPException e) {
            loggerError.error("load linoriccii data fail,FTPException："+e.getMessage());
            isOK = false;
        }  catch (Exception e) {
            loggerError.error("load linoriccii data fail,Exception："+e.getMessage());
            isOK = false;
        }  finally {
            close(ftp);
        }
        return isOK;
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
            loggerError.error("关闭连接失败IOException："+e.getMessage());
        } catch (FTPException e) {
            loggerError.error("关闭连接失败FTPException："+e.getMessage());
        }
    }
    /**
     *获取数组最后一位元素
     */
    private String lastName(String[] strArr){
        return strArr[strArr.length - 1];
    }
}

