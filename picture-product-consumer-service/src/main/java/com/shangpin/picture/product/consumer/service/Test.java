package com.shangpin.picture.product.consumer.service;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.picture.product.consumer.bean.AuthenticationInformation;
import com.shangpin.picture.product.consumer.e.PicHandleState;
import org.apache.commons.io.IOUtils;

import sun.misc.BASE64Encoder;
import sun.net.www.protocol.ftp.FtpURLConnection;

import javax.net.ssl.*;
import java.io.InputStream;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class Test {


    private static final String PASSWORD = "password";

    private static final String USERNAME = "username";

    private static final int TIMEOUT = 45*60*1000;

    private static final int CONNECT_TIMEOUT = 10*1000;

    public static void main(String[] args){
        Test test = new Test();
        test.pullPicAndPushToPic("https://ip.cn",null);
//        HubSpuPendingPicDto dto  = new HubSpuPendingPicDto();
//        AuthenticationInformation auth = new AuthenticationInformation();
//        auth.setUsername("Shangpin");
//        auth.setPassword("Shangpin17!");
//        test.pullPicAndPushToPicServer("ftp://Shangpin:Shangpin17!@88.149.230.95:24/Marketplace_Photo/740955002181001 (1).jpg",dto,auth);
    }


    private int pullPicAndPushToPicServer(String picUrl, HubSpuPendingPicDto dto, AuthenticationInformation
            authenticationInformation){
        InputStream inputStream = null;
        FtpURLConnection httpUrlConnection = null;
        int flag = 0;
        try {
            if (authenticationInformation != null) {//需要认证
                Authenticator.setDefault(new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(authenticationInformation.getUsername(),
                                new String(authenticationInformation.getPassword()).toCharArray());
                    }
                });
            }
            URL url = new URL(picUrl.replaceAll(" +", "%20"));
            URLConnection openConnection = url.openConnection();
            httpUrlConnection  =  (FtpURLConnection) openConnection;
            httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
            httpUrlConnection.setConnectTimeout(10*1000);
            httpUrlConnection.setReadTimeout(45*60*1000);
            httpUrlConnection.connect();

//            if (flag == 404 || flag == 400) {
//                return flag;
//            }else if(flag == 301 || flag == 302){
//
//                String newPicUrl = picUrl.replaceFirst("http", "https");
//                return pullPicAndPushToPicServer(newPicUrl,dto,authenticationInformation);
//            }
            inputStream = openConnection.getInputStream();
            byte[] byteArray = IOUtils.toByteArray(inputStream);
            if (byteArray == null || byteArray.length == 0) {
                throw new RuntimeException("读取到的图片字节为空,无法获取图片");
            }
            String base64 = new BASE64Encoder().encode(byteArray);


            dto.setPicHandleState(PicHandleState.HANDLED.getIndex());
            dto.setMemo("图片拉取成功");

        }catch (Throwable e) {

            e.printStackTrace();
            dto.setPicHandleState(PicHandleState.HANDLE_ERROR.getIndex());
            dto.setMemo("图片拉取失败:"+flag);
        } finally {
            closeFtpURL(inputStream,httpUrlConnection);
        }
        dto.setUpdateTime(new Date());
        return flag;
    }



    private int pullPicAndPushToPic(String picUrl,  AuthenticationInformation authenticationInformation){
        InputStream inputStream = null;
        HttpURLConnection httpUrlConnection = null;
        int flag = 0;
        try {
            if (authenticationInformation != null) {//需要认证
                Authenticator.setDefault(new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(authenticationInformation.getUsername(),
                                new String(authenticationInformation.getPassword()).toCharArray());
                    }
                });
            }
            URL url = new URL(picUrl.replaceAll(" +", "%20"));

            // 这个IP要换 成可用的IP哦，这里案例只是随便写的一个IP
            String ipport = "42.5.170.166:12463";
            String charset = "UTF-8";

            // 设置代理IP
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress((ipport.split(":"))[0], Integer.parseInt((ipport.split(":"))[1])));

            URLConnection openConnection = url.openConnection();
            httpUrlConnection  =  (HttpURLConnection) url.openConnection(proxy);

            // 处理SSL证书问题
            try{
                SSLSocketFactory oldSocketFactory = null;
                HostnameVerifier oldHostnameVerifier = null;

                boolean useHttps = picUrl.startsWith("https");
                if (useHttps) {
                    HttpsURLConnection https = (HttpsURLConnection) httpUrlConnection;
                    oldSocketFactory = trustAllHosts(https);
                    oldHostnameVerifier = https.getHostnameVerifier();
                    https.setHostnameVerifier(DO_NOT_VERIFY);
                }
            } catch (Exception e) {
                System.err.println("添加证书信任出错：" + e.getMessage());
            }



            httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
            httpUrlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpUrlConnection.setReadTimeout(TIMEOUT);
            httpUrlConnection.connect();
            flag = httpUrlConnection.getResponseCode();
            if (flag == 404 || flag == 400) {
                return flag;
            }else if(flag == 301 || flag == 302){

                String newPicUrl = picUrl.replaceFirst("http", "https");

            }
            inputStream = openConnection.getInputStream();
            byte[] byteArray = IOUtils.toByteArray(inputStream);
            if (byteArray == null || byteArray.length == 0) {
                throw new RuntimeException("读取到的图片字节为空,无法获取图片");
            }
            String base64 = new BASE64Encoder().encode(byteArray);


        }catch (Throwable e) {
//            log.error("系统拉取图片时发生异常,url ="+picUrl,e);
            e.printStackTrace();

        } finally {

        }

        return flag;
    }




    private void closeFtpURL(InputStream inputStream, FtpURLConnection httpUrlConnection) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Throwable e) {
//                log.error("关闭资源流发生异常", e);
                e.printStackTrace();
                throw new RuntimeException("关闭资源流发生异常");
            }
        }
        if (httpUrlConnection != null) {
            try {
                httpUrlConnection.close();
            } catch (Throwable e) {
//                log.error("关闭链接发生异常", e);
                e.printStackTrace();
                throw new RuntimeException("关闭链接发生异常");
            }
        }
    }


    /**
     * 覆盖java默认的证书验证
     */
    private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }};

    /**
     * 设置不验证主机
     */
    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * 信任所有
     * @param connection
     * @return
     */
    private static SSLSocketFactory trustAllHosts(HttpsURLConnection connection) {
        SSLSocketFactory oldFactory = connection.getSSLSocketFactory();
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory newFactory = sc.getSocketFactory();
            connection.setSSLSocketFactory(newFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oldFactory;
    }
}
