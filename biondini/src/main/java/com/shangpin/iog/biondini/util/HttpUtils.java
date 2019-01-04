package com.shangpin.iog.biondini.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class HttpUtils {
	private static Logger logger = Logger.getLogger("info");
	/*
	 * 拉取代码表
	 * */
	public static String getLectureDesTablesBySoap() {
		String xx = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
				+ "xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\""
				+ "xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">"
				+ "<soap:Body>"
				+ "<LectureDesTables xmlns=\"urn:LcvMagWS\">"
				+ "<bCData xsd:type=\"xsd:string\">"
				+ "</bCData>"
				+ "<sUser xsd:type=\"xsd:string\">BION456</sUser>"
				+ "<sMdp xsd:type=\"xsd:string\">INI123</sMdp>"
				+ "</LectureDesTables>" + "</soap:Body>" + "</soap:Envelope>";
		String json = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("SOAPAction", "urn:LcvMagWS/LectureDesTables");
		map.put("Content-Type", "text/xml; charset=UTF-8");
		map.put("Content-Type", "application/soap+xml; charset=utf-8");
		System.out.println("=================tables fetch begin====================================");
		try {
			json = HttpUtil45
					.operateData(
							"post",
							"soap",
							"http://81.161.58.250:8080/LCVMAGWSV8_WEB/awws/LcvMagWS.awws",

							new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
									1000 * 60 * 10), map, xx, null, null);
			System.out.println("tables ："+json);
			System.out.println("=================tables fetch end====================================");
			logger.info("tables ："+json.length());
			logger.info("=================tables fetch end====================================");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return json;
	}

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
		map.put("SOAPAction", "urn:LcvMagWS/LectureDuStock1");
		map.put("Content-Type", "text/xml; charset=UTF-8");
		map.put("Content-Type", "application/soap+xml; charset=utf-8");
		String json = null;
		System.out.println("=================Stock fetch begin====================================");
		try {
			json = HttpUtil45
					.operateData(
							"post",
							"soap",
							"http://81.161.58.250:8080/LCVMAGWSV8_WEB/awws/LcvMagWS.awws",
							new OutTimeConfig(1000 * 60 * 180, 1000 * 60 * 180,
									1000 * 60 * 180), map, xx, null, null);
			System.out.println("Stock ："+json);
			System.out.println("=================Stock fetch end====================================");
			logger.info("Stock ："+json.length());
			logger.info("=================Stock fetch end====================================");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/*
	 * 拉取商品list 
	 * */
	public static String getProductsBySoap() {

		String xx = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
				+ "xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\""
				+ "xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">"
				+ "<soap:Body>"
				+ "<LectureDesModelesAvecPrix xmlns=\"urn:LcvMagWS\">"
				+ "<bCData xsd:type=\"xsd:string\">"
				+ "</bCData>"
				+ "<sUser xsd:type=\"xsd:string\">BION456</sUser>"
				+ "<sMdp xsd:type=\"xsd:string\">INI123</sMdp>"
				+ "</LectureDesModelesAvecPrix>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";
		Map<String, String> map = new HashMap<String, String>();
		map.put("SOAPAction", "urn:LcvMagWS/LectureDesModelesAvecPrix");
		map.put("Content-Type", "text/xml; charset=UTF-8");
		map.put("Content-Type", "application/soap+xml; charset=utf-8");
		String json = null;
		System.out.println("=================product fetch begin====================================");
		try {
			//http://81.161.58.250:8080/LCVMAGWS_WEB/awws/LcvMagWS.awws?op=LectureDesModelesAvecPrix
			json = HttpUtil45
					.operateData(
							"post",
							"soap",
							"http://81.161.58.250:8080/LCVMAGWSV8_WEB/awws/LcvMagWS.awws",
							new OutTimeConfig(1000 * 60 * 60, 1000 * 60 * 60,
									1000 * 60 * 60), map, xx, null, null);
			System.out.println("product length:"+json);

			System.out.println("=================product fetch end====================================");
			logger.info("product length:"+json.length());
			logger.info("product message: "+json);
			logger.info("=================product fetch end====================================");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static void  main(String[] args){

		getProductsBySoap();
	}
}
