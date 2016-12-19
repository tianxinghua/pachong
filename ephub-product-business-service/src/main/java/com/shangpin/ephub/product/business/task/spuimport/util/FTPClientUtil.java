package com.shangpin.ephub.product.business.task.spuimport.util;
import java.io.File;  
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;  

public class FTPClientUtil {    
     
    private  FTPClient ftp;    
    /** 
     *  
     * @param path 上传到ftp服务器哪个路径下    
     * @param addr 地址 
     * @param port 端口号 
     * @param username 用户名 
     * @param password 密码 
     * @return 
     * @throws Exception 
     */  
    private  boolean connect(String host,int port,String username,String password) throws Exception {    
        boolean result = false;    
        ftp = new FTPClient();    
        int reply;    
        ftp.connect(host,port);    
        ftp.login(username,password);    
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);    
        reply = ftp.getReplyCode();    
        if (!FTPReply.isPositiveCompletion(reply)) {    
            ftp.disconnect();    
            return result;    
        }    
        result = true;    
        return result;    
    }    
    /** 
     *  
     * @param file 上传的文件或文件夹 
     * @throws Exception 
     */  
    private void upload(File file,String path) throws Exception{    
    	 ftp.changeWorkingDirectory(path);
         FileInputStream input = new FileInputStream(file);    
         ftp.storeFile(file.getName(), input);    
         input.close(); 
         ftp.quit();
    }    
   public static void main(String[] args) throws Exception{  
//	   FTPClientUtil t = new FTPClientUtil();  
//      t.connect("", "localhost", 21, "yhh", "yhhazr");  
//      File file = new File("e:\\uploadify");  
//      t.upload(file);  
   }  
}  