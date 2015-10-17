package com.shangpin.iog.levelgroup.service;






import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created by yang on 2015/10/17.
 */
public class FTPUtil {


    public static FTPClient getFTPClient(String ftpHost, String ftpPassword,
                                         String ftpUserName) throws SocketException,IOException {


        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setDefaultTimeout(60*80 * 1000);
        ftpClient.setConnectTimeout(0*80 * 1000);
        ftpClient.setDataTimeout(0*80 * 1000);
        ftpClient.connect(ftpHost, 21);
        ftpClient.login(ftpUserName, ftpPassword);

        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            ftpClient.disconnect();
        } else {
            System.out.println("FTP连接失败");
        }

        return ftpClient;
    }
}
