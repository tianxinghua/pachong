package com.shangpin.ep.order;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shangpin.ep.order.conf.mail.attach.AttachBean;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.BaseBluServiceImpl;
import com.shangpin.ep.order.module.orderapiservice.impl.WiseOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.atelier.CommonService;

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
//		ShangpinMail shangpinMail = new ShangpinMail();
//		shangpinMail.setFrom("chengxu@shangpin.com");
//		shangpinMail.setSubject("这是一份测试邮件，请忽略");
//		shangpinMail.setText("这是一份测试邮件，请忽略.This is a test email,please ignore");
//		shangpinMail.setTo("lubaijiang@shangpin.com");
//		List<String> addTo = new ArrayList<>();
////		addTo.add("fabio@giglio.com");
//		addTo.add("wangsaying@shangpin.com");
//		addTo.add("123@shangpin.com");
//		addTo.add("steven.ding@shangpin.com");
//		shangpinMail.setAddTo(addTo );
//		shangpinMailSender.sendShangpinMail(shangpinMail);
//		
//		
//	}
//	@Autowired
//	@Qualifier("baseBluServiceImpl")
//	IOrderService baseBluServiceImpl;
//
//
//	public void testPushOrder(){
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setDetail("9900041846920:1");
//		orderDTO.setSupplierNo("2016080801914");
//		orderDTO.setSpMasterOrderNo("20170209001");
//		orderDTO.setPurchaseNo("CGDF20170209001");
//		baseBluServiceImpl.handleConfirmOrder(orderDTO);
//	}
//
//	@Test
//	public void testMapping(){
//		String result = "{\"CodMsg\": 1, \"Msg\": \"OK Operation Occurred Successfully\"}";
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			OrderResult orderResult =  mapper.readValue(result, OrderResult.class);
//			System.out.println("123");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	@Autowired
	private ShangpinMailSender sm;
	
	@Test
	public void sendMailTest(){
		ShangpinMail shangpinMail = new ShangpinMail();
		try {
			shangpinMail.setFrom("lizhongren@shangpin.com");
			shangpinMail.setSubject("这是一个测试邮件");
			shangpinMail.setText("这是测试邮件的内容");
			shangpinMail.setTo("lubaijiang@shangpin.com");
			List<String> addTo = new ArrayList<>();
			addTo.add("yanxiaobin@shangpin.com");
			shangpinMail.setAddTo(addTo );
			List<AttachBean> attachList = new ArrayList<>();
			AttachBean a = new AttachBean();
			a.setFileName("eee.jpg");
			a.setFile(new File("E:\\eee.jpg"));
			attachList.add(a );
			shangpinMail.setAttachList(attachList );
			sm.sendShangpinMail(shangpinMail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("over");
	}
	
	@Autowired
	BaseBluServiceImpl baseBluServiceImpl;
	
	@Test
	public void pushOrder(){
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setDetail("9900042228107:1,");
		baseBluServiceImpl.handleConfirmOrder(orderDTO);
	}
	@Autowired
	WiseOrderService wise;
	
	@Test
	public void wisePushOrder(){
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setSpOrderId("2017061958515151");
    	orderDTO.setDetail("1163000-2013076855557:1,");
		wise.handleConfirmOrder(orderDTO );
	}
	@Test
	public void wiseRefund(){
		OrderDTO deleteOrder = new OrderDTO();
		deleteOrder.setSpOrderId("2017061958515151");
		deleteOrder.setDetail("1163000-2013076855557:1,");
		wise.handleRefundlOrder(deleteOrder );
	}
	@Autowired
	CommonService service;
	@Test
	public void testSize(){
		try {
			String size = service.getSizeFromEphub("2016030701799", "7294507-2010629426790");
			System.out.println(size);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

}
