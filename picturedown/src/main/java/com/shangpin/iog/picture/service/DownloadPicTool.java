package com.shangpin.iog.picture.service;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by lizhongren on 2016/8/29.
 */
public class DownloadPicTool {

    public static String downImage(String url, String filepath, String filename)
    {


        File f = new File(filepath + "/" + filename);
        if (f.exists()) {
            System.out.println("image has been download");
            return "";
        }
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpClientContext context = HttpClientContext.create();
        context.setRequestConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(120000)
                .setConnectTimeout(120000)
                .setSocketTimeout(120000)
                .build());
        HttpGet get = new HttpGet(replaceSpecialChar(url));
        try {
            CloseableHttpResponse response = httpClient.execute(get, context);
            if (response.getStatusLine().getStatusCode() == 200) {
                byte[] result = EntityUtils.toByteArray(response.getEntity());
                BufferedOutputStream bw = null;
                try
                {
                    if (!f.getParentFile().exists()) {
                        f.getParentFile().mkdirs();
                    }
                    bw = new BufferedOutputStream(new FileOutputStream(filepath + "/" + filename));
                    bw.write(result);
                } catch (Exception e) {
                    System.out.println("保存图片出错");
                    return "";
                } finally {
                    try {
                        if (bw != null)
                            bw.close();
                    } catch (Exception e) {
                        System.out.println("关闭出错");
                    }
                }
                try
                {
                    if (bw != null)
                        bw.close();
                } catch (Exception e) {
                    System.out.println("关闭出错");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return filepath + filename;
    }
    private static String replaceSpecialChar(String url) {
        return url.replace(" ", "%20");
    }
}
