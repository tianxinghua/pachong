package com.shangpin.ep.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EpOrderServiceApplicationTests {
 
	@Autowired
	private ShangpinMailSender shangpinMailSender;
	@Test
	public void contextLoads() throws Exception {
		
		
//		ShangpinMail shangpinMail = new ShangpinMail();
//		shangpinMail.setText("这是一封测试邮件");
//		shangpinMailSender.sendShangpinMail(shangpinMail );
		
		
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setMessageId("GADFA4545");
//		LogCommon.loggerOrder(orderDTO ,LogLeve.INFO);
		
		
	}

}
