package com.shangpin.supplier.product.consumer;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.supplier.product.consumer.conf.stream.source.sender.PendingProductStreamSender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupplierProductConsumerServiceApplicationTests {
	
	@Autowired
	private PendingProductStreamSender pendingProductStreamSender;

	@Test
	public void contextLoads() {
		PendingProduct pendingProduct = new PendingProduct();
		Map<String,String> headers = new HashMap<String,String>();
		
		pendingProductStreamSender.gebPendingProductStream(pendingProduct, headers);
	}

}
