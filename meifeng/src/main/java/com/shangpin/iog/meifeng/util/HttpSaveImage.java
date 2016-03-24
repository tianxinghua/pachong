package com.shangpin.iog.meifeng.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpSaveImage {
	public static void main(String[] args) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		String url = "http://dynamic.forzieri.com/is/image/Forzieri/hp130113-037-01-1xx?scl=1";
		HttpGet get = new HttpGet(url);
		try {
			CloseableHttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {  
                byte[] result = EntityUtils.toByteArray(response.getEntity());  
                BufferedOutputStream bw = null;  
                try {  
                    String path = "E://abcde.png";
					// 创建文件对象  
                    File f = new File(path );  
                    // 创建文件路径  
                    if (!f.getParentFile().exists())  
                        f.getParentFile().mkdirs();  
                    // 写入文件  
                    bw = new BufferedOutputStream(new FileOutputStream(path));  
                    bw.write(result);  
                } catch (Exception e) {  
                	System.out.println("保存图片出错");
                } finally {  
                    try {  
                        if (bw != null)  
                            bw.close();  
                    } catch (Exception e) {  
                    	System.out.println("关闭出错");
                    }  
                }  
            }  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
