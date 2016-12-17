package com.shangpin.supplier.product.consumer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.message.pending.header.MessageHeaderKey;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.conf.stream.source.sender.PendingProductStreamSender;
import com.shangpin.supplier.product.consumer.enumeration.ProductStatus;
import com.shangpin.supplier.product.consumer.service.dto.Sku;
import com.shangpin.supplier.product.consumer.service.dto.Spu;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupplierProductConsumerServiceApplicationTests {
	
	@Autowired
	private PendingProductStreamSender pendingProductStreamSender;

	@Test
	public void contextLoads() {
		PendingProduct pendingProduct = new PendingProduct();
		pendingProduct.setSupplierId("2015111001657");
		pendingProduct.setSupplierName("geb");
		pendingProduct.setMessageId("123456");
		PendingSpu data = new PendingSpu();
		data.setSupplierId("2015111001657");
		data.setHubSpuNo("5732265b2b33300afbc4444f");
		data.setHubBrandNo("PHILLIP LIM");
		data.setHubCategoryNo("shoot");
		data.setHubColor("red");
		data.setHubGender("women");
		data.setHubMaterial("30%wool 70%wool");
		data.setHubOrigin("china");
		data.setHubSeason("A17");
		List<PendingSku> skus = new ArrayList<PendingSku>();
		PendingSku sku =  new PendingSku();
		sku.setSupplierId("2015111001657");
		sku.setHubSkuNo("5732265b2b33300afbc4444f-XXL");
		sku.setMarketPrice(new BigDecimal("456.00"));
		sku.setSupplyPrice(new BigDecimal("355.00"));
		sku.setStock(1);
		sku.setHubSkuSize("XXL"); 
		skus.add(sku);
		data.setSkus(skus);
		pendingProduct.setData(data);
		Map<String,String> headers = new HashMap<String,String>();
		Spu pendingSpuHeader = new Spu();
		pendingSpuHeader.setSpuNo("5732265b2b33300afbc4444f");
		pendingSpuHeader.setSupplierId("2015111001657");
		pendingSpuHeader.setStatus(ProductStatus.NEW.getIndex());
		Sku pendingSkuHeader = new Sku();
		pendingSkuHeader.setSkuNo("5732265b2b33300afbc4444f-XXL");
		pendingSkuHeader.setSupplierId("2015111001657");
		pendingSkuHeader.setStatus(ProductStatus.NEW.getIndex());
		List<Sku> pendingSkus = new ArrayList<Sku>();
		pendingSkus.add(pendingSkuHeader);
		pendingSpuHeader.setSkus(pendingSkus);
		System.out.println("消息头===="+JsonUtil.serialize(pendingSpuHeader));
		System.out.println("消息体====="+JsonUtil.serialize(pendingProduct));
		headers.put(MessageHeaderKey.PENDING_PRODUCT_MESSAGE_HEADER_KEY, JsonUtil.serialize(pendingSpuHeader));
		
		pendingProductStreamSender.gebPendingProductStream(pendingProduct, headers);
		System.out.println("ok===============");
	}

}
