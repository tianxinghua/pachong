package com.shangpin.iog.meifeng.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Test extends Thread{
	private String urlString;
	private String filename;
	private String savePath;
	
	@Override
	public void run() {
		try {
			download(urlString, filename, savePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	  public  void download(String urlString, String filename,String savePath) throws Exception {  
	        // 构造URL  
	        URL url = new URL(urlString);  
	        // 打开连接  
	        URLConnection con = url.openConnection();  
	        //设置请求超时为5s  
	        con.setConnectTimeout(60*1000*60);  
	        // 输入流  
	        InputStream is = con.getInputStream();  
	      
	        // 1K的数据缓冲  
	        byte[] bs = new byte[1024];  
	        // 读取到的数据长度  
	        int len;  
	        // 输出的文件流  
	       File sf=new File(savePath);  
	       if(!sf.exists()){  
	           sf.mkdirs();  
	       }  
	       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);  
	        // 开始读取  
	        while ((len = is.read(bs)) != -1) {  
	          os.write(bs, 0, len);  
	        }  
	        // 完毕，关闭所有链接  
	        os.close();  
	        is.close();  
	    }
	public Test(String urlString, String filename, String savePath) {
		super();
		this.urlString = urlString;
		this.filename = filename;
		this.savePath = savePath;
	}
	  
}
