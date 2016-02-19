package com.shangpin.iog.ctsiLogistics.dao;

import java.io.File;
import java.io.IOException;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;

/**
 * Created by lizhongren on 2015/7/30.
 */
public class Test {
    public static void main(String[] args){
    	String remoteFilePath = "/Output/";
    	String remoteFileName = "";
    	String localFilePath = "C://tes";
    	getFile("","","");
        //
    }
    private static String HOST="114.142.223.126",PORT="21",USER="qbcftp",PASSWORD="QBC1@1@Ftp",FILE_PATH="/Output/";
    public static FTPClient getFtpClient(){

        /** 定义FTPClient便利 */
        FTPClient ftp = null;
        try
        {
            /** 创建FTPClient */
            ftp = new FTPClient();
            /** 连接服务器 */
            ftp.setRemoteHost(HOST);

            ftp.setRemotePort(Integer.parseInt(PORT));
            ftp.setTimeout(3600000);
            ftp.connect();

            /** 登陆 */
            ftp.login(USER, PASSWORD);

            /** 连接模式 */
            ftp.setConnectMode(FTPConnectMode.PASV);      //

            /** ASCII方式：只能传输一些如txt文本文件，
             * zip、jpg等文件需要使用BINARY方式
             * */
            //ftp.setType(FTPTransferType.ASCII);
            ftp.setType(FTPTransferType.BINARY);
        }catch(Exception e){
        	
        }
        
        return ftp;
    }
    public static void getFile(String remoteFilePath,String remoteFileName,String localFilePath){
    	
    	FTPClient ftp = getFtpClient();
    	  in = ftp.retrieveFileStream("/Photo/BALENCIAGA-A6 PAPIER 370926.jpg");
    	try {
			ftp.get("C://","");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FTPException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	try {
//			byte [] s = ftp.get("/Output/01-28-2016-test.txt");
		     String[] files = ftp.dir("/Photo");
		     System.out.println(files.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
