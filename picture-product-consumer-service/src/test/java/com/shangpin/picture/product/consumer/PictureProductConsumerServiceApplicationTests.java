package com.shangpin.picture.product.consumer;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
	public static void main(String[] args) throws Throwable {
		try {
			String ou = "http://192.168.9.71:80/group2/M00/00/27/wKgJR1idQZiAJrlSAAAIyjvSRgw508.jpg";
			URL url = new URL(ou.replaceAll(" +", "%20"));
			URLConnection openConnection = url.openConnection();
			openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
			openConnection.setConnectTimeout(45*60*1000);
			openConnection.setReadTimeout(45*60*1000);
			InputStream inputStream = openConnection.getInputStream();
			int length = IOUtils.toByteArray(inputStream).length;
			if (length < 10240) {
				throw new RuntimeException("读取到的图片内容不完整,无法获取图片");
			}
			System.out.println(length);
			String base64 = new BASE64Encoder().encode(IOUtils.toByteArray(inputStream));
			System.out.println(base64);
			if (StringUtils.isBlank(base64)) {
				throw new RuntimeException("读取到的图片内容为空,无法获取图片");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("发生异常。。。。。。。");
		}
		System.out.println("over.......");
	}
}
