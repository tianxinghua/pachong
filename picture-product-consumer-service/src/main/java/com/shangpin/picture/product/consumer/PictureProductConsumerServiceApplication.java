package com.shangpin.picture.product.consumer;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import sun.misc.BASE64Encoder;

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
import com.sun.mail.util.BASE64EncoderStream;
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
public class PictureProductConsumerServiceApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(PictureProductConsumerServiceApplication.class, args);
	}

	@Autowired
	private UploadPicGateway uploadPicGateway;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		InputStream resourceAsStream = this.getClass().getResourceAsStream("/banner/logo.jpg");
		String base64 = new BASE64Encoder().encode(IOUtils.toByteArray(resourceAsStream));
		UploadPicDto file = new UploadPicDto();
		file.setBase64(base64);
		String upload = uploadPicGateway.upload(file);
		System.out.println(upload);
	}
}
