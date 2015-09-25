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
     * Description: �����ļ�
     */
    public static void downFile() {
        // defined FTPClient
        com.enterprisedt.net.ftp.FTPClient ftp = null;
        try {
            //creat FTPClient
            ftp = new com.enterprisedt.net.ftp.FTPClient();
            // set value
            ftp.setRemoteHost(Constant.URL);
            ftp.setRemotePort(Integer.parseInt(Constant.PORT));
            ftp.setTimeout(3600000);
            ftp.connect();

            //login
            ftp.login(Constant.USER, Constant.PASSWORD);

            //set mode
            ftp.setConnectMode(FTPConnectMode.PASV);      //

            //set type
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
       // System.out.println("Constant.URL=" + Constant.URL);
        FtpLoad.downFile();
    }
}
