package com.shangpin.pending.product.consumer;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shangpin.pending.product.consumer.conf.clients.mysql.brand.HubBrandDicClient;
import com.shangpin.pending.product.consumer.conf.clients.mysql.brand.bean.HubBrandDic;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PendingProductConsumerServiceApplicationTests {

	@Autowired
	private HubBrandDicClient client;
	
	@Test
	public void contextLoads() {
		
//		HubBrandDic hubBrandDic = new HubBrandDic();
//		hubBrandDic.setCreateTime(new Date());
//		hubBrandDic.setCreateUser("yanxiaobin");
//		client.insert(hubBrandDic);
	}

}
