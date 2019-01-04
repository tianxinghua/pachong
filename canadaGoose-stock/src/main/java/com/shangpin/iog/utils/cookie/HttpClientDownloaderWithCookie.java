package com.shangpin.iog.utils.cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 
 * @author njt
 * @date 2018年12月6日 下午7:09:53
 * @desc
 * HttpClientDownloaderWithCookie
 */
public class HttpClientDownloaderWithCookie {

    private static HttpClientGeneratorWithCookie httpClientGenerator = null;
    private static CloseableHttpClient httpClient = null;
    private static CookieStore cookieStore = null;

    private static CloseableHttpClient getHttpClient(String ipProxy) {
    	if(cookieStore == null) {
    		cookieStore = new BasicCookieStore();
    	}
    	if(httpClientGenerator == null) {
    		httpClientGenerator = new HttpClientGeneratorWithCookie();
    	}
        return httpClientGenerator.getClient(cookieStore, ipProxy);
    }

    public static String download(String url, String ipProxy) {
    	String cookieStr = "";
    	URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
        CloseableHttpResponse httpResponse = null;
        if(httpClient == null) {
        	httpClient = getHttpClient(ipProxy);
        }
        HttpGet httpGet = new HttpGet(uri);
        try {
            httpResponse = httpClient.execute(httpGet, new HttpClientContext());
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            System.err.println("-------响应码："+statusCode);
            if(statusCode==200) {
            	StringBuffer cookieBuffer = new StringBuffer();
	            List<Cookie> cookies = cookieStore.getCookies();
	            System.err.println("-------get cookie page success: "+url);
	            System.err.println("-------链接Cookie信息：");
	            for (Cookie cookie : cookies) {
	            	System.err.println("-------key:"+cookie.getName()+", value:"+cookie.getValue());
	            	cookieBuffer.append(cookie.getName()+"="+cookie.getValue()+" ;");
				}
	            if(StringUtils.isNotBlank(cookieBuffer)) {
	            	cookieStr = cookieBuffer.substring(0, cookieBuffer.length()-1).toString();
	            }
            }
        } catch (IOException e) {
        	System.err.println("get cookie page error"+url+e.getMessage());
        } finally {
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }
        return cookieStr;
    }
    

}
