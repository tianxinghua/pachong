package com.shangpin.ep.order;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shangpin.ep.order.module.orderapiservice.impl.*;
import com.shangpin.ep.order.module.orderapiservice.impl.util.HttpClientUtil;
import com.shangpin.ep.order.module.orderapiservice.impl.util.HttpRequestMethedEnum;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.logicalcobwebs.proxool.ProxoolFacade;
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
	private String queryConfUrl;
	Map<String,String> header = new HashMap<String, String>();
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
			//shangpinMail.setTo("lubaijiang@shangpin.com");
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
	BagheeraOrderService bagheeraOrderService;

	@Test
	public void bagheeraPushOrder(){
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setSupplierId("2015100701573");
		orderDTO.setSpOrderId("201709066014679");
    	orderDTO.setDetail("8293047-2103049616411:1,");
    	bagheeraOrderService.handleConfirmOrder(orderDTO );
	}
	@Test
	public void bagheeraRefund(){
		OrderDTO deleteOrder = new OrderDTO();
		deleteOrder.setSupplierId("2015100701573");
		deleteOrder.setSpOrderId("201709066014679");
		bagheeraOrderService.handleRefundlOrder(deleteOrder );
	}

	@Autowired
	CommonService service;
    @Autowired
    GebnegozioServiceImpl gebnegozioService;

	@Test
	public void testSize(){
		try {
			String size = service.getProductSize("2016030701799", "7294507-2010629426790");
			System.out.println(size);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@AfterClass
	public   static   void  tearDown() {
		ProxoolFacade.shutdown(0);
	}
    //测试-------------------------
    @Test
    public void gebTest() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setSpOrderId("181011");
        orderDTO.setDetail("2000000031804:1");
        orderDTO.setSupplierId("2015111001657");
        orderDTO.setSupplierOrderNo("225");
        gebnegozioService.handleConfirmOrder(orderDTO);

       //测试调用ephub接口
        //String supplierId = "2018061101883";
		//String supplierSkuNo = "2000000031804";
		//Long supplierSpuId = Long.valueOf(852204);
		//gebnegozioService.getSizeFromEphub(supplierId,supplierSkuNo);
		//gebnegozioService.getSupplierSpuIdFromEphub(supplierId,supplierSkuNo);
		//gebnegozioService.getSupplierSpuColorFromEphub(supplierSpuId);
    }
    @Test
	public void gebTestSql() {
		/*OutTimeConfig outTimeConf = new OutTimeConfig();
		String url="http://gebnegozio-qas.extranet.alpenite.com/rest/marketplace_shangpin/V1/products/";
		String username="";
		String password="";
		Map<String,String> param = new HashMap<String,String>();
		param.put("searchCriteria[currentPage]","1");
		param.put("searchCriteria[pageSize]","5");
		param.put("searchCriteria[filter_groups][0][filters][0][field]","modello");
		param.put("searchCriteria[filter_groups][0][filters][0][value]","010006 CREAMBLACK");
		param.put("searchCriteria[filter_groups][0][filters][0][condition_type]","eq");
		param.put("searchCriteria[filter_groups][1][filters][0][field]","type_id");
		param.put("searchCriteria[filter_groups][1][filters][0][value]","configurable");
		param.put("searchCriteria[filter_groups][1][filters][0][condition_type]","eq");
		param.put("fields","items[sku]");

		Map<String,String> headMap = new HashMap<String,String>();
		headMap.put("Content-Type", "application/json");
		headMap.put("Authorization", "Bearer " + "h334xq799ibldmrjuencv9d54turnl3b");
		try {
			url = URLEncoder.encode( url , "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		gebnegozioService.gebTestSql(  url,  outTimeConf,  param,  headMap ,  username,  password);
	*/


		String url = "http://gebnegozio-qas.extranet.alpenite.com/rest/marketplace_shangpin/V1/";
	/*String queryConfStr = "";
	String modello = "010006 CREAMBLACK";
		modello = URLEncoder.encode( modello );
	queryConfStr = "products/?searchCriteria[currentPage]=1&searchCriteria[pageSize]=5&searchCriteria[filter_groups][0][filters][0][field]=modello&searchCriteria[filter_groups][0][filters][0][value]="+modello+"&searchCriteria[filter_groups][0][filters][0][condition_type]=eq&searchCriteria[filter_groups][1][filters][0][field]=type_id&searchCriteria[filter_groups][1][filters][0][value]=configurable&searchCriteria[filter_groups][1][filters][0][condition_type]=eq&fields=items[sku]";
		queryConfUrl = url + queryConfStr;
		header.put("Content-Type", "application/json");
		header.put("Authorization", "Bearer " + "h334xq799ibldmrjuencv9d54turnl3b");
		HashMap<String,String> queryConfResp = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet ,queryConfUrl, null, header, null);
		String valuell = queryConfResp.get("resBody");
	}*/
		header.put("Content-Type", "application/json");
		header.put("Authorization", "Bearer " + "akrtfhn7ub87iaqtnrtwft8e0ygs1rir");
		String delCartProUrl = "";
		String quoteId = "3356";
		delCartProUrl = url + "carts/mine/items/" + quoteId;
		HashMap<String, String> delCartProResp = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpDelete, delCartProUrl, null, header, null);
		if(delCartProResp.get("code").equals("200")) {
			String delflag = delCartProResp.get("resBody");
			if (delflag.equals("true")){
				System.out.println("购物车内容已经清空。");
				//logger.info("购物车内容已经清空。");
			}else {
				System.out.println("购物车内容清空失败！");
				//logger.info("购物车内容清空失败！");
			}
		}
	}

}
