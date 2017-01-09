package com.shangpin.picture.product.consumer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.message.picture.ProductPicture;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureProductConsumerServiceApplicationTests {

	@Test
	public void contextLoads() {
	}
public static void main(String[] args) throws JsonProcessingException {
	SupplierPicture s = new SupplierPicture();
	s.setMessageDate(new Date().toString());
	s.setMessageId(UUID.randomUUID().toString());
	s.setSupplierId("SDD34");
	s.setSupplierName("test");
	List<Image> images = new ArrayList<>();
	Image e = new Image("http://img4.cache.netease.com/photo/0026/2012-11-29/8HGDQHHG3QV10026.JPG");
	images.add(e );
	ProductPicture productPicture = new ProductPicture("supplierSpuNo", images );
	s.setProductPicture(productPicture );
	ObjectMapper o = new ObjectMapper();
	String t = o.writeValueAsString(s);
	System.out.println(t);
}
}
