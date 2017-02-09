package com.shangpin.ep.order;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.BaseBluServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EpOrderServiceApplicationTests {
 
//	public static void main(String[] args) {
//		OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60*5, 1000 * 60*5);
//		 Map<String, String> map =new HashMap<String, String>();
//		 map.put("DBContext", "Default");
//		 map.put("purchase_no", "CGDF2016121383184");
//		 map.put("order_no", "201612135133986");
//		 map.put("barcode", "5000948100025");
//		 map.put("ordQty","1");
//		 map.put("key", "doV3IeZ6bz");
//		 map.put("sellPrice", "0");
//		String ss =  HttpUtil45.get("http://80.88.89.209/papiniapi/Myapi/Productslist/GetOrderByPoNumAndOrdNum", defaultConfig ,map);
//		System.out.println(ss);


//	}

//	@Autowired
//	private ShangpinMailSender shangpinMailSender;
//	@Test
//	public void contextLoads() throws Exception {
//		
////		ShangpinMail shangpinMail = new ShangpinMail();
////		shangpinMail.setText("这是一封测试邮件");
////		shangpinMailSender.sendShangpinMail(shangpinMail );
//		
////		OrderDTO orderDTO = new OrderDTO();
////		orderDTO.setMessageId("GADFA4545");
////		LogCommon.loggerOrder(orderDTO ,LogLeve.INFO);
//		
//		
//	}
	@Autowired
	@Qualifier("baseBluServiceImpl")
	IOrderService baseBluServiceImpl;

	@Test
	public void testPushOrder(){
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setDetail("9900041846920:1");
		orderDTO.setSupplierNo("2016080801914");
		orderDTO.setSpMasterOrderNo("20170209001");
		orderDTO.setPurchaseNo("CGDF20170209001");
		baseBluServiceImpl.handleConfirmOrder(orderDTO);
	}


}
