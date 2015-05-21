package com.shangpin.iog.common.utils.httpclient;


import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import java.io.IOException;


/**
 * 模拟http的post请求
 */
public class HttpUtil {
	public static void main(String args[]) {

        String url="http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetAllTypes?DBContext=default&key=8IZk2x5tVN";
        NameValuePair[] data = {
                new NameValuePair("DBContext", "default"),
                new NameValuePair("data", "8IZk2x5tVN")

        };
        try {
          String kk=  HttpUtil.getData(url,false);
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
        String responseStr="";
        if(null==proxy) proxy=false;
        HttpClient httpClient = new HttpClient(proxy);
        // 执行postMethod
        int statusCode = 0;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);


            // 将表单的值放入postMethod中
            postMethod.setRequestBody(data);

            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

            statusCode = httpClient.executeMethod(postMethod);

            // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
            // 301或者302
            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                    || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // 从头中取出转向的地址
                Header locationHeader = postMethod.getResponseHeader("location");
                String location = null;
                if (locationHeader != null) {
                    location = locationHeader.getValue();
                    responseStr="The page was redirected to:" + location;
                } else {
                    responseStr="Location field value is null.";
                }

            } else {


                responseStr = postMethod.getResponseBodyAsString();

            }

        } catch (HttpException e) {
            e.printStackTrace();
            throw new ServiceMessageException("网络连接异常");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceMessageException("网络连接异常");
        } finally {
            postMethod.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }

        return responseStr;
    }



    public static String getData(String url,Boolean proxy) throws ServiceException {
        String responseStr="";
        if(null==proxy) proxy=false;
        HttpClient httpClient = new HttpClient(proxy);
        // 执行postMethod
        int statusCode = 0;
        GetMethod getMethod = null;
        try {
            getMethod = new GetMethod(url);


            // 将表单的值放入postMethod中

            getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

            statusCode = httpClient.executeMethod(getMethod);

            // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
            // 301或者302
            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                    || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // 从头中取出转向的地址
                Header locationHeader = getMethod.getResponseHeader("location");
                String location = null;
                if (locationHeader != null) {
                    location = locationHeader.getValue();
                    responseStr="The page was redirected to:" + location;
                } else {
                    responseStr="Location field value is null.";
                }

            } else {


                responseStr = getMethod.getResponseBodyAsString();

            }

        } catch (HttpException e) {
            e.printStackTrace();
            throw new ServiceMessageException("网络连接异常");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceMessageException("网络连接异常");
        } finally {
            getMethod.releaseConnection();
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
