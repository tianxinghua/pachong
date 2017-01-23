package com.shangpin.picture.product.consumer;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import sun.misc.BASE64Encoder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureProductConsumerServiceApplicationTests {

	@Test
	public void contextLoads() {
	}
//	public static void main(String[] args) throws Throwable {
//		String ou = "http://213.144.71.1/FOTO/P17/PHILLIP LIM/AE17 B132NPPTA260 TAN_2_P.JPG";
//		URL url = new URL(ou.replaceAll(" +", "%20"));
//		URLConnection openConnection = url.openConnection();
//		openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
//		openConnection.setConnectTimeout(45*60*1000);
//		openConnection.setReadTimeout(45*60*1000);
//		InputStream inputStream = openConnection.getInputStream();
//		String base64 = new BASE64Encoder().encode(IOUtils.toByteArray(inputStream));
//		System.out.println(base64);
//	}
}
