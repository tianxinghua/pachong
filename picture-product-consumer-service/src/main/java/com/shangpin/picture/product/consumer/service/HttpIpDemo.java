package com.shangpin.picture.product.consumer.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**本DEMO添加了对SSL证书的处理**/
public class HttpIpDemo {
    public static void main(String[] args) {
        try {
            String targetUrl = "http://www.baidu.com";
            HttpURLConnection connection = null;
            URL link = new URL(targetUrl);
            // 这个IP要换 成可用的IP哦，这里案例只是随便写的一个IP
            String ipport = "117.85.57.88:50951";
            String charset = "UTF-8";

            // 设置代理IP
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress((ipport.split(":"))[0], Integer.parseInt((ipport.split(":"))[1])));
            connection = (HttpURLConnection)link.openConnection(proxy);

            // 处理SSL证书问题
            try{
                SSLSocketFactory oldSocketFactory = null;
                HostnameVerifier oldHostnameVerifier = null;

                boolean useHttps = targetUrl.startsWith("https");
                if (useHttps) {
                    HttpsURLConnection https = (HttpsURLConnection) connection;
                    oldSocketFactory = trustAllHosts(https);
                    oldHostnameVerifier = https.getHostnameVerifier();
                    https.setHostnameVerifier(DO_NOT_VERIFY);
                }
            } catch (Exception e) {
                System.err.println("添加证书信任出错：" + e.getMessage());
            }

            connection.setDoOutput(true);
            connection.setRequestProperty("User-agent", "");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Referer", "");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("Cookie", "");
            connection.setUseCaches(false);
            connection.setReadTimeout(10000);
            int rcode = connection.getResponseCode();

            if (rcode != 200) {
                System.out.println("使用代理IP连接网络失败，状态码：" + connection.getResponseCode());
            }else {
                String line = null;
                StringBuilder html = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
                while((line = reader.readLine()) != null){
                    html.append(line);
                }

                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                }

                System.out.println("请求" + targetUrl + ", 得到如下信息：");
                System.out.println(html.toString());
            }
        } catch (Exception e) {
            System.err.println("发生异常：" + e.getMessage());
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