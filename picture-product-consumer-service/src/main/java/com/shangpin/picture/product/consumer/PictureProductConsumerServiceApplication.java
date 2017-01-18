package com.shangpin.picture.product.consumer;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.ephub.client.fdfs.gateway.UploadPicGateway;
import com.shangpin.picture.product.consumer.e.PicHandleState;

import sun.misc.BASE64Encoder;
/**
 * <p>Title:PictureProductConsumerServiceApplication.java </p>
 * <p>Description: 图片处理消费者</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月1日 下午7:32:45
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class PictureProductConsumerServiceApplication implements ApplicationRunner{

	
//	@Autowired
//	private UploadPicGateway gateway;
	public static void main(String[] args) {
		SpringApplication.run(PictureProductConsumerServiceApplication.class, args);
	}
	@Override
	public void run(ApplicationArguments args) throws Exception {
//		URL url = new URL("http://213.144.71.1/FOTO/P17/DOLCEGABBANA/CD0101 AL19880304.JPG");
//		URLConnection openConnection = url.openConnection();
//		openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
//		openConnection.setConnectTimeout(10*60*1000);
//		openConnection.setReadTimeout(10*60*1000);
//		InputStream inputStream = openConnection.getInputStream();
//		String base64 = new BASE64Encoder().encode(IOUtils.toByteArray(inputStream));
//		UploadPicDto uploadPicDto = new UploadPicDto();
//		uploadPicDto.setBase64(base64);
//		uploadPicDto.setExtension("JPG");
//		String spPicUrl = gateway.upload(uploadPicDto);
//		System.out.println(spPicUrl);
		
	}
}
