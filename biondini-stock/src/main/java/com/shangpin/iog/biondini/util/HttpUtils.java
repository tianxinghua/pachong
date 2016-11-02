package com.shangpin.iog.biondini.util;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.commons.collections.map.HashedMap;

public class HttpUtils {

	/*
	 *  拉取商品库存
	 * */
	public static String getProductStocksBySoap() {

		String xx = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
				+ "xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\""
				+ "xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">"
				+ "<soap:Body>"
				+ "<LectureDuStock xmlns=\"urn:LcvMagWS\">"
				+ "<bCData xsd:type=\"xsd:string\">"
				+ "</bCData>"
				+ "<sUser xsd:type=\"xsd:string\">BION456</sUser>"
				+ "<sMdp xsd:type=\"xsd:string\">INI123</sMdp>"
				+ "</LectureDuStock>" + "</soap:Body>" + "</soap:Envelope>";
		Map<String, String> map = new HashMap<String, String>();
		map.put("SOAPAction", "urn:LcvMagWS/LectureDuStock");
		map.put("Content-Type", "text/xml; charset=UTF-8");
		map.put("Content-Type", "application/soap+xml; charset=utf-8");
		String json = null;
		System.out.println("=================Stock fetch begin====================================");
		try {
			json = HttpUtil45
					.operateData(
							"post",
							"soap",
							"http://80.12.82.220:8080/LCVMAGWS_WEB/awws/LcvMagWS.awws?op=LectureDuStock",
							new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
									1000 * 60 * 10), map, xx, null, null);
			System.out.println("=================Stock fetch end====================================");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return json;
	}


	public static String  getOrderBySoap(){
		String xx = "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
				+ "<soap12:Body>"
				+ "<CreateNewOrder xmlns=\"http://tempuri.org/\">"

				+ "<orderID>20160720121212</orderID>"
				+ "<BuyerInfo>"
				+ "<Name>test</Name>"
				+ "<Address>Via Edgardo Macrelli 107</Address>"
				+ "<zipcode>47521</zipcode>"
				+ "<Corriere>100000</Corriere>"
				+ "<Notes></Notes>"
				+ "</BuyerInfo>"
				+ "<GoodsList>"
				+ "<Good>"
				+ "<ID>94697</ID>"
				+ "<Size>2XL</Size>"
				+ "<Qty>1</Qty>"
				+ "<Price>1265</Price>"
				+ "</Good>"
				+ "</GoodsList>"
				+ "</CreateNewOrder>" + "</soap12:Body>" + "</soap12:Envelope>";
		System.out.println(xx);
		String json = null;
		Map<String, String> map = new HashMap<String, String>();
//		map.put("SOAPAction", "http://tempuri.org/CreateNewOrder");
		map.put("Content-Type", "application/soap+xml; charset=utf-8");
		System.out.println("=====================================================");
		try {
			json = HttpUtil45
					.operateData(
							"post",
							"soap",
							"http://studio69.atelier98.net/api_studio69/api_studio69.asmx?op=CreateNewOrder",
							new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
									1000 * 60 * 10), map, xx, "SHANGPIN", "2MWWKgNSxgf");
			System.out.println(json);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String  cancelOrderBySoap(){
		String xx = "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
				+ "<soap12:Body>"
				+ "<CancelOrder xmlns=\"http://tempuri.org/\">"

				+ "<ID>20160720121212</ID>"

				+ "</CancelOrder>" + "</soap12:Body>" + "</soap12:Envelope>";
		System.out.println(xx);
		String json = null;
		Map<String, String> map = new HashMap<String, String>();
//		map.put("SOAPAction", "http://tempuri.org/CancelOrder");
		map.put("Content-Type", "application/soap+xml; charset=utf-8");
		System.out.println("=====================================================");
		try {
			json = HttpUtil45
					.operateData(
							"post",
							"soap",
							"http://studio69.atelier98.net/api_studio69/api_studio69.asmx?op=CancelOrder",
							new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
									1000 * 60 * 10), map, xx, "SHANGPIN", "2MWWKgNSxgf");
			System.out.println(json);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String  getCategory(){
		String json="";
		try {
//			json = HttpUtil45
//					.operateData(
//							"post",
//							"",
//							"http://studio69.atelier98.net/api_studio69/api_studio69.asmx?op=GetGoodsCatg",
//							new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
//									1000 * 60 * 10), null, null, "SHANGPIN", "2MWWKgNSxgf");

			OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);


			json = HttpUtil45.postAuth("http://studio69.atelier98.net/api_studio69/api_studio69.asmx/GetGoodsCatg", null, outTimeConf, "SHANGPIN", "2MWWKgNSxgf");

			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String  getProductByCategory(){
		String json="";
		try {
//			json = HttpUtil45
//					.operateData(
//							"post",
//							"",
//							"http://studio69.atelier98.net/api_studio69/api_studio69.asmx?op=GetGoodsCatg",
//							new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
//									1000 * 60 * 10), null, null, "SHANGPIN", "2MWWKgNSxgf");

			OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
			Map<String,String > map = new HashedMap();
			map.put("CategoryID","16");


			json = HttpUtil45.postAuth("http://studio69.atelier98.net/api_studio69/api_studio69.asmx/GetGoodsDetailListByCategoryID", map, outTimeConf, "SHANGPIN", "2MWWKgNSxgf");

			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static void main(String[] args) {

//		HttpUtils.getProductByCategory();

		System.out.println("----------create order --------------");

		HttpUtils.getOrderBySoap();

		System.out.println("----------cancelorder--------------");

		HttpUtils.getProductByCategory();
	}



	public static String  getOrderStatusSoap(){
		String xx = "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
				+ "<soap12:Body>"
				+ " <GetStatusOrder xmlns=\"http://tempuri.org/\">\n" +
				"      <CODE>201607244071807</CODE>\n" +
				"      <ID_CUSTOMER>1461</ID_CUSTOMER>\n" +
				"    </GetStatusOrder>" + "</soap12:Body>" +
				"</soap12:Envelope>";
		System.out.println(xx);
		String json = null;
		Map<String, String> map = new HashMap<String, String>();
//		map.put("SOAPAction", "http://tempuri.org/CancelOrder");
		map.put("Content-Type", "application/soap+xml; charset=utf-8");
		System.out.println("=====================================================");
		try {
			json = HttpUtil45
					.operateData(
							"post",
							"soap",
							"http://188.11.248.175/WS_SITO_p15/WS_SITO_P15.ASMX?op=GetStatusOrder",
							new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
									1000 * 60 * 10), map, xx, "shangpin", "shang0807");
			System.out.println(json);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return json;
	}


}
