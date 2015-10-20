package com.shangpin.iog.levelgroup.service;






import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created by yang on 2015/10/17.
 */
public class FTPUtil {

    private static String HOST="ftp.thelevelgroup-ftp.com",PORT="21",USER="googlepla@thelevelgroup-ftp.com",PASSWORD="tlg2014!",FILE_PATH="TLG_GooglePLA_lncc_IT.txt";

    public static FTPClient getFTPClient() throws SocketException,IOException {


        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setDefaultTimeout(60*80 * 1000);
        ftpClient.setConnectTimeout(0*80 * 1000);
        ftpClient.setDataTimeout(0*80 * 1000);
        ftpClient.connect(HOST, 21);
        ftpClient.login(USER, PASSWORD);

        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            ftpClient.disconnect();
        } else {
            System.out.println("FTP连接失败");
        }

        return ftpClient;
    }

}
