package com.shangpin.iog.ctsiLogistics.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
/**
 * ftpclient下载文件的例子
 * 
 * @author 老紫竹(Java世纪网,java2000.net)
 */
public class Test {
  public static void main(String[] args) throws Exception {
    FTPClient ftpClient = new FTPClient();
    String hostName = "114.142.223.126";
    String userName = "qbcftp";
    String password = "QBC1@1@Ftp";
    String remoteDir = "/Output";
//    private static String HOST="114.142.223.126",PORT="21",USER="qbcftp",PASSWORD="QBC1@1@Ftp",FILE_PATH="/Output/";
    try {
      ftpClient.connect(hostName, 21);
      ftpClient.setControlEncoding("UTF-8");
      ftpClient.login(userName, password);
      ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      FTPFile[] files = ftpClient.listFiles(remoteDir);
      for (int i = 0; i < files.length; i++) {
    	  String fileName = files[i].getName();
    	  System.out.println(files[i].getName());
    	  File file = new File("d://"+fileName);
          FileOutputStream fos = new FileOutputStream(file);
          ftpClient.retrieveFile(remoteDir + "/"+fileName, fos);
          fos.close();
      }
      
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}