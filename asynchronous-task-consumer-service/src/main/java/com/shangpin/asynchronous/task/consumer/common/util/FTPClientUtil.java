package com.shangpin.asynchronous.task.consumer.common.util;
import java.io.BufferedOutputStream;
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.shangpin.ephub.response.HubResponse;  

public class FTPClientUtil {    
     
    private  static FTPClient ftpClient;    
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
        ftpClient = new FTPClient();    
        int reply;    
        ftpClient.connect(host,port);    
        ftpClient.login(username,password);    
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);    
        reply = ftpClient.getReplyCode();    
        if (!FTPReply.isPositiveCompletion(reply)) {    
        	ftpClient.disconnect();    
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
    public static InputStream downFile(String remotePath) throws Exception{
		
    	InputStream in = null;
//    	in = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\采购异常\\Hub商品导入模板 - 副本.xlsx"));
		if(null != ftpClient){
	        if(StringUtils.isNotBlank(remotePath)){
	        	in = ftpClient.retrieveFileStream(remotePath);
				ftpClient.quit();
	        }
		}
		return in;
	}
    public static void uploadFile(File file, String path, String fileName) throws Exception {
  		InputStream sbs = new FileInputStream(file);
  		ftpClient.changeWorkingDirectory(path);
  		ftpClient.storeFile(fileName, sbs);
  		ftpClient.quit();
  	}
	
   public static void main(String[] args) throws Exception{  
//	   FTPClientUtil t = new FTPClientUtil();  
//      t.connect("", "localhost", 21, "yhh", "yhhazr");  
//      File file = new File("e:\\uploadify");  
//      t.upload(file);  
   }  
}  