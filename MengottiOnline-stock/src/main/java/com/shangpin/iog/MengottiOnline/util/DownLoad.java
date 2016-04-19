package com.shangpin.iog.MengottiOnline.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownLoad {

	/**
	 * 
	 * @param url
	 * @param local 文件存放目录
	 * @throws IOException 
	 */
	public static void downFromNet(String uri,String local) throws IOException{

        URL url = new URL(uri);
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();
            fs = new FileOutputStream(local);
            
            byte[] buffer = new byte[1024];
            int byteread = 0;
            while ((byteread = inStream.read(buffer)) != -1) {                
                fs.write(buffer, 0, byteread);
            }
            
            System.out.println("==================download over================");
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e; 
        }finally{
        	if(null != inStream){
        		inStream.close();
        	}
        	if(null != fs){
        		fs.close();
        	}        	
        }
	}
}
