package com.shangpin.iog.gebnegozio.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaowenjun on 2018/9/6.
 */
public class HttpClientUtil {

    /**
     * httpclient使用步骤
     * 1、创建一个HttpClient对象;
     * 2、创建一个Http请求对象并设置请求的URL，比如GET请求就创建一个HttpGet对象，POST请求就创建一个HttpPost对象;
     * 3、如果需要可以设置请求对象的请求头参数，也可以往请求对象中添加请求参数;
     * 4、调用HttpClient对象的execute方法执行请求;
     * 5、获取请求响应对象和响应Entity;
     * 6、从响应对象中获取响应状态，从响应Entity中获取响应内容;
     * 7、关闭响应对象;
     * 8、关闭HttpClient.
     */

    private static RequestConfig requestConfig = RequestConfig.custom()
            //从连接池中获取连接的超时时间
            // 要用连接时尝试从连接池中获取，若是在等待了一定的时间后还没有获取到可用连接（比如连接池中没有空闲连接了）则会抛出获取连接超时异常。
            .setConnectionRequestTimeout(15000)
            //与服务器连接超时时间：httpclient会创建一个异步线程用以创建socket连接，此处设置该socket的连接超时时间
            //连接目标url的连接超时时间，即客服端发送请求到与目标url建立起连接的最大时间。超时时间3000ms过后，系统报出异常
            .setConnectTimeout(15000)
            //socket读数据超时时间：从服务器获取响应数据的超时时间
            //连接上一个url后，获取response的返回等待时间 ，即在与目标url建立连接后，等待放回response的最大时间，在规定时间内没有返回响应的话就抛出SocketTimeout。
            .setSocketTimeout(15000)
            .build();


    /**
     * 发送http请求
     *
     * @param requestMethod 请求方式（HttpGet、HttpPost、HttpPut、HttpDelete）
     * @param url 请求路径
     * @param params post请求参数
     * @param header 请求头
     * @return 响应文本
     */
    public static HashMap<String,String> sendHttp(HttpRequestMethedEnum requestMethod, String url, Map<String, Object> params, Map<String, String> header, String entity) {
        HashMap<String,String> responseMap = new HashMap<String,String>();
        //1、创建一个HttpClient对象;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        String responseContent = null;
        //2、创建一个Http请求对象并设置请求的URL，比如GET请求就创建一个HttpGet对象，POST请求就创建一个HttpPost对象;
        HttpRequestBase request = requestMethod.createRequest(url);
        request.setConfig(requestConfig);
        //3、如果需要可以设置请求对象的请求头参数，也可以往请求对象中添加请求参数;
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            //添加请求正文
            if (entity != null){
                ((HttpEntityEnclosingRequest) request).setEntity(
                        new StringEntity(entity, ContentType.create("application/json", "UTF-8")));
            }
            // 往对象中添加相关参数
            if (params != null) {
                ((HttpEntityEnclosingRequest) request).setEntity(
                        new StringEntity(JSON.toJSONString(params),
                                ContentType.create("application/json", "UTF-8")));
            }
            //4、调用HttpClient对象的execute方法执行请求;
            httpResponse = httpClient.execute(request);
            //5、获取请求响应对象和响应Entity;
            HttpEntity httpEntity = httpResponse.getEntity();
            //6、从响应对象中获取响应状态，从响应Entity中获取响应内容;
            if (httpEntity != null) {
                responseContent = EntityUtils.toString(httpEntity, "UTF-8");
            }
            //返回内容
            responseMap.put( "code" , String.valueOf(httpResponse.getStatusLine().getStatusCode()) );
            responseMap.put( "message" , httpResponse.getStatusLine().getReasonPhrase() );
            responseMap.put( "resBody" , responseContent );

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //7、关闭响应对象;
                if (httpResponse != null) {
                    httpResponse.close();
                }
                //8、关闭HttpClient.
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseMap;
    }
}
