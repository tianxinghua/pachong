package com.shangpin.picture.product.consumer.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import javax.net.ssl.*;

/**
 * 这个DEMO主要为了测试爬虫（动态）代理IP的稳定性
 * 完美支持企业信息天眼查、电商Ebay、亚马逊、新浪微博、法院文书、分类信息等
 * 也可以作为爬虫参考项目，如需使用，请自行修改webParseHtml方法
 */
public class TestDynamicIpContinue {

    public static List ipList = new ArrayList<>();
    public static boolean gameOver = false;

    public static void main(String[] args) throws Exception {
        // 每隔几秒提取一次IP
        long fetchIpSeconds = 5;
        int testTime = 3;

        // 请填写无忧代理IP订单号，填写之后才可以提取到IP哦
        String order = "7678c98d9f8b2bea59a027763fd62ada";

        // 你要抓去的目标网址
        // 企业信息天眼查 http://www.tianyancha.com/company/1184508115
        // 企业信息工商系统 http://www.gsxt.gov.cn/%7BLtkX_Us_Uuw_QRrZ9mfv2cbf8ANpkJNT8_EzigHHLIvfwbsXfxY0o15JwumCNmvtm_nv9Wtm2Iy_ptgrdpD7p-dP6C8an4IYel_Bx4EnhQhxk8Q4jptLj9IMw9N0lCP-4i0Q4MN55e0wtKOgDy4GEw-1493711400352%7D
        // 电商Ebay http://www.ebay.com/sch/tenco-tech/m.html?_ipg=200&_sop=12&_rdc=1
        // 电商天猫 https://list.tmall.com/search_product.htm?cat=56594003&brand=97814105&sort=s&style=g&search_condition=23&from=sn_1_cat&industryCatId=50025174#J_crumbs
        // 电商京东 https://search.jd.com/Search?keyword=%E8%8B%8F%E6%89%93%E7%B2%89&enc=utf-8&suggest=1.def.0.T15&wq=s%27d%27f&pvid=1d962d789b81461aa6cce40b26a90429
        // IP检测 http://ip.chinaz.com/getip.aspx
        // 匿名度检测 http://www.xxorg.com/tools/checkproxy/
        // 新浪微博 https://m.weibo.cn/api/container/getIndex?containerid=100103type%3D3%26q%3D%E6%B1%BD%E8%BD%A6&queryVal=%E6%B1%BD%E8%BD%A6&type=user&page=2
        // 法院文书 https://m.itslaw.com/mobile
        // 分类信息百姓网 http://china.baixing.com/cheliang/
        String targetUrl = "https://www.gucci.cn/zh/pr/476541DVUXT9193?nid=27";

        // 设置referer信息，如果抓取淘宝、天猫需要设置
        String referer = "";
        // 开启对https的支持
        boolean https = true;
        // 是否输出Header信息
        boolean outputHeaderInfo = false;
        // 是否加载JS，加载JS会导致速度变慢
        boolean useJS = false;
        // 请求超时时间，单位毫秒，默认5秒
        int timeOut = 10000;

        if (order == null || "".equals(order)) {
            System.err.println("请输入爬虫（动态）代理订单号");
            return;
        }
        System.out.println(">>>>>>>>>>>>>>动态IP测试开始<<<<<<<<<<<<<<");
        System.out.println("***************");
        System.out.println("提取IP间隔 " + fetchIpSeconds + " 秒 ");
        System.out.println("爬虫目标网址  " + targetUrl);
        System.out.println("***************\n");
        TestDynamicIpContinue tester = new TestDynamicIpContinue();
        new Thread(tester.new GetIP(fetchIpSeconds * 1000, testTime, order, targetUrl, useJS, timeOut, referer, https, outputHeaderInfo)).start();

        while(!gameOver){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(">>>>>>>>>>>>>>动态IP测试结束<<<<<<<<<<<<<<");
        System.exit(0);
    }

    // 抓取IP138，检测IP
    public class Crawler extends Thread{
        @Override
        public void run() {
            webParseHtml(targetUrl);
        }

        long sleepMs = 200;
        boolean useJs = false;
        String targetUrl = "";
        int timeOut = 5000;
        String ipport = "";

        String referer;
        boolean https;
        boolean outputHeaderInfo;

        public Crawler(long sleepMs, String targetUrl, boolean useJs, int timeOut, String ipport, String referer, boolean https, boolean outputHeader) {
            this.sleepMs = sleepMs;
            this.targetUrl = targetUrl;
            this.useJs = useJs;
            this.timeOut = timeOut;
            this.ipport = ipport;

            this.referer = referer;
            this.https = https;
            this.outputHeaderInfo = outputHeader;
        }

        /**
         * 覆盖java默认的证书验证
         */
        private  final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
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
        private  final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        /**
         * 信任所有
         * @param connection
         * @return
         */
        private  SSLSocketFactory trustAllHosts(HttpsURLConnection connection) {
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
        String charset = "UTF-8";
        public String webParseHtml(String targetUrl) {

            try {
//                String targetUrl = "http://www.baidu.com";
                HttpURLConnection connection = null;
                HttpsURLConnection https = null;
                URL link = new URL(targetUrl);
                // 这个IP要换 成可用的IP哦，这里案例只是随便写的一个IP


                // 设置代理IP
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress((ipport.split(",")[0]).split(":")[0], Integer.parseInt((ipport.split(",")[0]).split(":")[1])));
                connection = (HttpURLConnection)link.openConnection(proxy);

                // 处理SSL证书问题
                try{
                    SSLSocketFactory oldSocketFactory = null;
                    HostnameVerifier oldHostnameVerifier = null;

                    boolean useHttps = targetUrl.startsWith("https");
                    if (useHttps) {
                        https = (HttpsURLConnection) connection;
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
            return "";
























//            String html = "";
//            BrowserVersion[] versions = { BrowserVersion.CHROME, BrowserVersion.FIREFOX_52, BrowserVersion.INTERNET_EXPLORER, BrowserVersion.CHROME};
//            WebClient client = new WebClient(versions[(int)(versions.length * Math.random())]);
//            try {
//                client.getOptions().setThrowExceptionOnFailingStatusCode(false);
//                client.getOptions().setJavaScriptEnabled(useJs);
//                client.getOptions().setCssEnabled(false);
//                client.getOptions().setThrowExceptionOnScriptError(false);
//                client.getOptions().setTimeout(timeOut);
//                client.getOptions().setAppletEnabled(true);
//                client.getOptions().setGeolocationEnabled(true);
//                client.getOptions().setRedirectEnabled(true);
//
//                // 对于HTTPS网站，加上这行代码可以跳过SSL验证
//                client.getOptions().setUseInsecureSSL(https);
//
//                if (referer != null && !"".equals(referer)) {
//                    client.addRequestHeader("Referer", referer);
//                }
//
//                if (ipport != null) {
//                    ProxyConfig proxyConfig = new ProxyConfig((ipport.split(",")[0]).split(":")[0], Integer.parseInt((ipport.split(",")[0]).split(":")[1]));
//                    client.getOptions().setProxyConfig(proxyConfig);
//                }else {
//                    System.out.print(".");
//                    return "";
//                }
//
//                long startMs = System.currentTimeMillis();
//
//                Page page = client.getPage(url);
//                WebResponse response = page.getWebResponse();
//
//                if (outputHeaderInfo) {
//                    // 输出header信息
//                    List<NameValuePair> headers = response.getResponseHeaders();
//                    for (NameValuePair nameValuePair : headers) {
//                        System.out.println(nameValuePair.getName() + "-->" + nameValuePair.getValue());
//                    }
//                }
//
//                boolean isJson = false ;
//                if (response.getContentType().equals("application/json")) {
//                    html = response.getContentAsString();
//                    isJson = true ;
//                }else if(page.isHtmlPage()){
//                    html = ((HtmlPage)page).asXml();
//                }
//
//                long endMs = System.currentTimeMillis();
//
//                if (url.indexOf("2017.ip138.com") != -1) {
//                    System.out.println(getName() + " " + ipport + " 用时 " + (endMs - startMs) + "毫秒 ：" + Jsoup.parse(html).select("center").text());
//                }else if(url.equals("http://www.xxorg.com/tools/checkproxy/")) {
//                    System.out.println(getName() + " " + ipport + " 用时 " + (endMs - startMs) + "毫秒 ：" + Jsoup.parse(html).select("#result .jiacu").text());
//                }else if(isJson) {
//                    System.out.println(getName() + " " + ipport + " 用时 " + (endMs - startMs) + "毫秒 ：" +html);
//                }else if(url.indexOf("tianyancha.com") != -1) {
//                    Document doc = Jsoup.parse(html);
//                    Elements els = doc.select(".c8");
//                    System.out.println(getName() + "企业基本信息：");
//                    for (Element element : els) {
//                        System.out.println("\t*" + element.text());
//                    }
//                    els = doc.select(".companyInfo-table tr");
//                    System.out.println(getName() + "企业股东信息：");
//                    for (Element element : els) {
//                        System.out.println("\t*" + element.text());
//                    }
//                    els = doc.select("#_container_check tr");
//                    System.out.println(getName() + "企业抽查息：");
//                    for (Element element : els) {
//                        System.out.println("\t*" + element.text());
//                    }
//                }else{
//                    Document doc = Jsoup.parse(html);
//                    System.out.println(getName() + " " + ipport + " 用时 " + (endMs - startMs) + "毫秒 ：" + doc.select("title").text());
//                }
//            } catch (Exception e) {
//                System.err.println(ipport + ":" + e.getMessage());
//            } finally {
//                client.close();
//            }
//            return html;
        }

    }

    // 定时获取动态IP
    public class GetIP implements Runnable{
        long sleepMs = 1000;
        int maxTime = 3;
        String order = "";
        String targetUrl;
        boolean useJs;
        int timeOut;
        String referer;
        boolean https;
        boolean outputHeaderInfo;

        public GetIP(long sleepMs, int maxTime, String order, String targetUrl, boolean useJs, int timeOut, String referer, boolean https, boolean outputHeaderInfo) {
            this.sleepMs = sleepMs;
            this.maxTime = maxTime;
            this.order = order;
            this.targetUrl = targetUrl;
            this.useJs = useJs;
            this.timeOut = timeOut;
            this.referer=referer;
            this.https=https;
            this.outputHeaderInfo=outputHeaderInfo;
        }

        @Override
        public void run() {
            int time = 1;
            while(!gameOver){
                if(time >= 4){
                    gameOver = true;
                    break;
                }
                try {
                    java.net.URL url = new java.net.URL("http://api.ip.data5u.com/dynamic/get.html?order=" + order + "&ttl&random=true");

                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setConnectTimeout(3000);
                    connection = (HttpURLConnection)url.openConnection();

                    InputStream raw = connection.getInputStream();
                    InputStream in = new BufferedInputStream(raw);
                    byte[] data = new byte[in.available()];
                    int bytesRead = 0;
                    int offset = 0;
                    while(offset < data.length) {
                        bytesRead = in.read(data, offset, data.length - offset);
                        if(bytesRead == -1) {
                            break;
                        }
                        offset += bytesRead;
                    }
                    in.close();
                    raw.close();
                    String[] res = new String(data, "UTF-8").split("\n");
                    System.out.println(">>>>>>>>>>>>>>当前返回IP量 " + res.length);
                    for (String ip : res) {
                        System.out.println("ip = "+ip );
                        new Crawler(100, targetUrl, useJs, timeOut, ip, referer, https, outputHeaderInfo).start();
                    }
                } catch (Exception e) {
                    System.err.println(">>>>>>>>>>>>>>获取IP出错, " + e.getMessage());
                }
                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public String joinList(List<String> list){
        StringBuilder re = new StringBuilder();
        for (String string : list) {
            re.append(string).append(",");
        }
        return re.toString();
    }


    public String trim(String html) {
        if (html != null) {
            return html.replaceAll(" ", "").replaceAll("\n", "");
        }
        return null;
    }

}