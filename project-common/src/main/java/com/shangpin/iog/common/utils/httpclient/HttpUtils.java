package com.shangpin.iog.common.utils.httpclient;

import com.shangpin.framework.ServiceException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class HttpUtils {
    /**
     *
     * @param url 地址
     * @param params 参数
     * @param proxy 是否代理
     * @param isAuthentication  是否验证
     * @param user   用户名
     * @param password 密码
     * @return
     */
	public static String post(String url,
			java.util.List<NameValuePair> params,Boolean proxy,Boolean isAuthentication,String user,String password)
				{
		DefaultHttpClient httpClient = new DefaultHttpClient(proxy);

        if(isAuthentication) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            UsernamePasswordCredentials usernamePasswordCredentials =
                    new UsernamePasswordCredentials(user, password);
            credentialsProvider.setCredentials(AuthScope.ANY, usernamePasswordCredentials);
            httpClient.setCredentialsProvider(credentialsProvider);
        }
		HttpPost post= new HttpPost(url);


		String result = "{\"error\":\"发生异常错误\"}";

		try{
            if(null != params){
                post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
            }
            initSchemeRegistry(httpClient);
	        
	        HttpResponse response = httpClient.execute(post);
	        HttpEntity entity = response.getEntity();
	        result = EntityUtils.toString(entity);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) { 
			e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }


		return result;
	}

    public static String  get(String url)
           {
        DefaultHttpClient httpClient = new DefaultHttpClient(false);




        HttpGet getMethod= new HttpGet(url);
        String result = "{\"error\":\"发生异常错误\"}";

        try{
            initSchemeRegistry(httpClient);

            HttpResponse response = httpClient.execute(getMethod);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;

    }

    private static void  initSchemeRegistry(HttpClient httpClient) throws  IOException, KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslcontext = SSLContext.getInstance("TLS");

        sslcontext.init(null, new TrustManager[]{new X509TrustManager() {

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates, String s)
                    throws java.security.cert.CertificateException {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates, String s)
                    throws java.security.cert.CertificateException {
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        }}, null);
        SSLSocketFactory sf = new SSLSocketFactory(sslcontext,
                SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        Scheme http = new Scheme("http", 80, PlainSocketFactory.getSocketFactory());
        Scheme https = new Scheme("https", 443, sf);
        httpClient.getConnectionManager().getSchemeRegistry().register(http);
        httpClient.getConnectionManager().getSchemeRegistry().register(https);
    }

    public  static void main(String[] args){
        String url="https://api.orderlink.it/v1/user/token";

        try {
//          String kk= HttpUtil.getData("https://api.orderlink.it/v1/products?access_token=6c9ade4c5fea79a5c0b060c67b55f4a2a59316dff3a18f047990484b8cc74d8c6ecddbbbb03139211f017ee9ea983f908ae5a46cf087294ccfdb46a78107fd010da5437c42e13b17de93a90c3fa2bee5e11d1723eb68026b1bc26f37152c8a38&page=10&limit=100",false);// HttpUtil.getData(url,false,true,"SHANGPIN","12345678");
            String kk = HttpUtils.post(url, null,false,true,"SHANGPIN","12345678");
//            String kk=HttpUtil.getData("https://api.orderlink.it/v1/user/token?username=SHANGPIN&password=12345678",false);
            System.out.println("content = "  + kk);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
