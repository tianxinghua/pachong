package com.shangpin.iog.levelgroup.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * 简单的ftp下载工具
 * 一天用一次，不需要连接池
 * 14-4-29.
 */
public class LevlelgroupFtpUtil {
    private static Log log = LogFactory.getLog(LevlelgroupFtpUtil.class);
    //private static ResourceBundle bundle =ResourceBundle.getBundle("param", Locale.ENGLISH) ;



    /**
     * 去 服务器的FTP路径下上读取文件
     *
     * @param
     * @return
     */
    public static List<String> readConfigFileForFTP(String fileName) {
        List<String> contentList = new ArrayList<>();
        FileInputStream inFile = null;
        InputStream in = null;
        FTPClient ftpClient = null;
        try {
            ftpClient = FTPUtil.getFTPClient();
            if (ftpClient == null || !ftpClient.isConnected()){
                for(int i=0;i<3;i++){
                    ftpClient = FTPUtil.getFTPClient();
                    if (ftpClient.isConnected()){
                        break;
                    }
                }
            }
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            in = ftpClient.retrieveFileStream(fileName);
        } catch (FileNotFoundException e) {
            if (ftpClient == null || !ftpClient.isConnected()){
                for(int i=0;i<3;i++){
                    try {
                        ftpClient = FTPUtil.getFTPClient();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (ftpClient.isConnected()){
                        break;
                    }
                }
            }
            e.printStackTrace();

        } catch (SocketException e) {
            System.out.println("连接FTP失败.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取错误。");
            e.printStackTrace();

        }

        if (in != null) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String data = null;
            int count = 0;
            try {
                while ((data = br.readLine()) != null) {
                    contentList.add(data);
                    count++;
                }
            } catch (IOException e) {
                System.out.println("文件读取错误。");
                e.printStackTrace();

            }finally{
                try {
                    ftpClient.disconnect();
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }else{
            System.out.println("in为空，不能读取。");
        }
        return contentList;
    }
    public static void  main(String[] args) throws Exception{
        String FILE_PATH = "";
        List<String> list = LevlelgroupFtpUtil.readConfigFileForFTP( FILE_PATH);

        if (list != null){
            System.out.println("size================"+list.size());

            for(String s : list){
                System.out.println(s);
            }
        }
    }
}
