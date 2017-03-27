package com.shangpin.picture.product.consumer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import sun.misc.BASE64Encoder;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureProductConsumerServiceApplicationTests {

	@Test
	public void contextLoads() {
	}
	public static void main(String[] args) throws Throwable {
		try {
			String ou = "https://1431182383.rsc.cdn77.org/public/Foto/A15---Valentino%20Garavani%20uomo---IU0EU00QCSES02_0.jpg";
			URL url = new URL(ou.replaceAll(" +", "%20"));
			URLConnection openConnection = url.openConnection();
			HttpURLConnection httpUrlConnection  =  (HttpURLConnection) openConnection;
			httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
			httpUrlConnection.setConnectTimeout(45*60*1000);
			httpUrlConnection.setReadTimeout(45*60*1000);
			httpUrlConnection.connect();
			int code = httpUrlConnection.getResponseCode();
			System.out.println(code);
//			InputStream inputStream = httpUrlConnection.getInputStream();
//			byte[] byteArray = IOUtils.toByteArray(inputStream);
//			String base64 = new BASE64Encoder().encode(byteArray);
//			System.out.println(base64);
			httpUrlConnection.disconnect();
			return;
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("发生异常。。。。。。。");
		} finally {
			System.out.println("==========");
		}
		System.out.println("over.......");
	}
}
