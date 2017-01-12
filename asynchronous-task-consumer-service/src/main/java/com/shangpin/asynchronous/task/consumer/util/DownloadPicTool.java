package com.shangpin.asynchronous.task.consumer.util;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:DownloadPicTool </p>
 * <p>Description: 下载图片工具类 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月12日 下午5:17:29
 *
 */
@Slf4j
public class DownloadPicTool {

	/**
	 * 传入http协议图片url，返回byte数组
	 * @param url
	 * @return
	 */
    public static byte[] downImage(String url){
    	try {
    		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
	        CloseableHttpClient httpClient = httpClientBuilder.build();
	        HttpClientContext context = HttpClientContext.create();
	        context.setRequestConfig(RequestConfig.custom()
	                .setConnectionRequestTimeout(120000)
	                .setConnectTimeout(120000)
	                .setSocketTimeout(120000)
	                .build());
	        HttpGet get = new HttpGet(replaceSpecialChar(url));
            CloseableHttpResponse response = httpClient.execute(get, context);
            if (response.getStatusLine().getStatusCode() == 200) {
                 return EntityUtils.toByteArray(response.getEntity());
            }
        }catch (Exception e) {
           log.error("下载出错："+e.getMessage()); 
        }
        return null;
    }
    private static String replaceSpecialChar(String url) {
        return url.replace(" ", "%20");
    }

}
