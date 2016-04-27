package com.shangpin.iog.webcontainer.front.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.iog.webcontainer.front.controller.FileDownloadController;

public class DowmImage extends Thread{
	private String urlString;
	private String filename;
	private String savePath;
	private Logger log = LoggerFactory.getLogger(FileDownloadController.class) ;
	@Override
	public void run() {
		try {
			downImage(urlString, savePath, filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	  public void downImage(String url,String filepath,String filename){
	    	log.info("下载"+filepath+"/"+filename);
	    	// 创建文件对象  
	        File f = new File(filepath+"/"+filename);  
	        if (f.exists()) {
	        	log.error("image has been download");
	        	return;
			}
	    	HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			CloseableHttpClient httpClient = httpClientBuilder.build();
			HttpClientContext context = HttpClientContext.create();
			context.setRequestConfig(RequestConfig.custom()
					.setConnectionRequestTimeout(1000*60*2)
					.setConnectTimeout(1000*60*2)
					.setSocketTimeout(1000*60*2)
					.build());
			HttpGet get = new HttpGet(replaceSpecialChar(url));
			try {
				CloseableHttpResponse response = httpClient.execute(get,context);
				if (response.getStatusLine().getStatusCode() == 200) {  
	                byte[] result = EntityUtils.toByteArray(response.getEntity());  
	                BufferedOutputStream bw = null;  
	                try {  
	                    // 创建文件路径  
	                    if (!f.getParentFile().exists())  
	                        f.getParentFile().mkdirs();  
	                    // 写入文件  
	                    bw = new BufferedOutputStream(new FileOutputStream(filepath+"/"+filename));  
	                    bw.write(result);  
	                } catch (Exception e) {  
	                	log.error("保存图片出错");
	                } finally {  
	                    try {  
	                        if (bw != null)  
	                            bw.close();  
	                    } catch (Exception e) {  
	                    	log.error("关闭出错");
	                    }  
	                }  
	            }  
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	 private String replaceSpecialChar(String url){
    	return url.replace(" ", "%20");
    }
	public DowmImage(String urlString, String filename, String savePath) {
		super();
		this.urlString = urlString;
		this.filename = filename;
		this.savePath = savePath;
	}
}
