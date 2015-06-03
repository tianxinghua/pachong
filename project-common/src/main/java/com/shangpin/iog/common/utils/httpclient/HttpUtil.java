package com.shangpin.iog.common.utils.httpclient;


import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 模拟http的post请求
 */
public class HttpUtil {
	public static void main(String args[]) {

        String url="https://api.orderlink.it/v1/user/token";
        NameValuePair[] data = {
                new NameValuePair("access_token", "6c9ade4c5fea79a5c0b060c67b55f4a2a59316dff3a18f047990484b8cc74d8c6ecddbbbb03139211f017ee9ea983f908ae5a46cf087294ccfdb46a78107fd01c51b13b2dc624f8496fc85de3a7f6ce72554196bc78f1e0a0c78dfe433c1ace4"),
                new NameValuePair("page","10"),
                new NameValuePair("limit","100")


        };
        try {
//          String kk= HttpUtil.getData("https://api.orderlink.it/v1/products?access_token=6c9ade4c5fea79a5c0b060c67b55f4a2a59316dff3a18f047990484b8cc74d8c6ecddbbbb03139211f017ee9ea983f908ae5a46cf087294ccfdb46a78107fd010da5437c42e13b17de93a90c3fa2bee5e11d1723eb68026b1bc26f37152c8a38&page=10&limit=100",false);// HttpUtil.getData(url,false,true,"SHANGPIN","12345678");
            String kk = HttpUtil.postData(url,null, false, true, "SHANGPIN", "12345678");
//            String kk=HttpUtil.getData("https://api.orderlink.it/v1/user/token?username=SHANGPIN&password=12345678",false);
            System.out.println("content = "  + kk);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }


    static
    {

          //注册刚才创建的https 协议对象
          Protocol.registerProtocol("https", new Protocol("https", new MySecureProtocolSocketFactory(), 443));
          Protocol.registerProtocol("http",new Protocol("http", new  DefaultProtocolSocketFactory(), 80));

    }



    /**
     * post传递参数
     * @param url  地址
     * @param data  参数
     * @param proxy  是否使用代理
     * @return
     * @throws ServiceException
     */
    public static String postData(String url,NameValuePair[] data,Boolean proxy) throws ServiceException {
        if(null==proxy) proxy=false;
        HttpClient httpClient = new HttpClient(proxy);

        try {
            PostMethod postMethod =  new PostMethod(url);

            // 将表单的值放入postMethod中
            if(null!=data) {
                postMethod.setRequestBody(data);
            }


            return getResponse(httpClient,postMethod);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "{\"error\":\"失败\"}";
    }

    /**
     *
     * @param url 地址
     * @param data 参数
     * @param proxy 是否代理
     * @param isAuthentication 是否认证
     * @param user  认证用户
     * @param password  认证密码
     * @return
     * @throws ServiceException 自定义异常
     */
    public static String postData(String url,NameValuePair[] data,Boolean proxy,Boolean isAuthentication,String user,String password) throws ServiceException {
        if(null==proxy) proxy=false;
        HttpClient httpClient = new HttpClient(proxy);
        try {
            PostMethod postMethod =  new PostMethod(url);

            // 将表单的值放入postMethod中
            if(null!=data) {
                postMethod.setRequestBody(data);
            }
            if(isAuthentication){//需要验证


//           CredentialsProvider provider = new Basic
                httpClient.getParams().setAuthenticationPreemptive(true);

                UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user, password);

                httpClient.getState().setCredentials(new AuthScope(null,443,AuthScope.ANY_REALM), creds);
                postMethod.setDoAuthentication(true);
            }


            return getResponse(httpClient,postMethod);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "{\"error\":\"失败\"}";
    }



    public static String getData(String url,Boolean proxy) throws ServiceException {
        String responseStr="";
        if(null==proxy) proxy=false;
        HttpClient httpClient = new HttpClient(proxy);

        GetMethod getMethod = new GetMethod(url);

        return getResponse(httpClient, getMethod);
    }









    public static String getData(String url,Boolean proxy,Boolean isAuthentication,String user,String password) throws ServiceException {
        String responseStr="";
        if(null==proxy) proxy=false;
        HttpClient httpClient = new HttpClient(proxy);
        GetMethod getMethod = new GetMethod(url);

        if(isAuthentication){//需要验证


//           CredentialsProvider provider = new Basic

            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user, password);

            httpClient.getState().setCredentials(AuthScope.ANY, creds);
            getMethod.setDoAuthentication(true);
        }

//       //设置http头
//        List<Header> headers = new ArrayList<Header>();
//        headers.add(new Header("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"));
//        httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);



        return getResponse(httpClient, getMethod);
    }


    private static String getResponse(HttpClient httpClient,HttpMethod method) throws ServiceException{
        String responseStr="";
        try {


            // 将表单的值放入postMethod中

            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));

            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

            int statusCode = httpClient.executeMethod(method);

            // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
            // 301或者302
            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                    || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // 从头中取出转向的地址
                Header locationHeader = method.getResponseHeader("location");
                String location = null;
                if (locationHeader != null) {
                    location = locationHeader.getValue();
                    responseStr="The page was redirected to:" + location;
                } else {
                    responseStr="Location field value is null.";
                }

            } else {


                responseStr = method.getResponseBodyAsString();

            }

        } catch (HttpException e) {
            e.printStackTrace();
            throw new ServiceMessageException("网络连接异常");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceMessageException("网络连接异常");
        } finally {
            method.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
        return responseStr;
    }



    private static PostMethod getPostMethod(String url) {
         PostMethod pmethod = new PostMethod(url);
         //设置响应头信息
         pmethod.addRequestHeader("Connection", "keep-alive");
         pmethod.addRequestHeader("Cache-Control", "max-age=0");
         pmethod.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
         return pmethod;
    }

 }
