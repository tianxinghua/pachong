package com.shangpin.picture.product.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
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
	private HubSpuPendingGateWay gateWay;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//InputStream resourceAsStream = this.getClass().getResourceAsStream("/banner/gtl.jpg");
//		URL url = new URL("http://www.tonyboutique.com/media/catalog/product/b/e/be08955185_960-1_1.jpg");
//		URLConnection connection = url.openConnection();
//		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
//		InputStream stream = connection.getInputStream();
//		String base64 = new BASE64Encoder().encode(IOUtils.toByteArray(stream));
//		stream.close();
//		UploadPicDto file = new UploadPicDto();
//		file.setBase64(base64);
//		file.setExtension("jpg");
//		String upload = uploadPicGateway.upload(file);
//		System.out.println(upload);
	}
}
