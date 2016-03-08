package com.shangpin.iog.biondini.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.biondini.dao.SOAP;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class HttpUtils {

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
							"http://80.12.82.220:8080/LCVMAGWS_WEB/awws/LcvMagWS.awws?op=LectureDesTables",
							new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
									1000 * 60 * 10), map, xx, null, null);
			System.out.println("tables ："+json.length());
			System.out.println("=================tables fetch end====================================");
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
			System.out.println("Stock ："+json.length());
			System.out.println("=================Stock fetch end====================================");
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
				+ "<LectureDesModeles xmlns=\"urn:LcvMagWS\">"
				+ "<bCData xsd:type=\"xsd:string\">"
				+ "</bCData>"
				+ "<sUser xsd:type=\"xsd:string\">BION456</sUser>"
				+ "<sMdp xsd:type=\"xsd:string\">INI123</sMdp>"
				+ "</LectureDesModeles>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";
		Map<String, String> map = new HashMap<String, String>();
		map.put("SOAPAction", "urn:LcvMagWS/LectureDesModeles");
//		map.put("Content-Type", "text/xml; charset=UTF-8");
		map.put("Content-Type", "application/soap+xml; charset=utf-8");
		String json = null;
		System.out.println("=================product fetch begin====================================");
		try {
			json = HttpUtil45
					.operateData(
							"post",
							"soap",
							"http://80.12.82.220:8080/LCVMAGWS_WEB/awws/LcvMagWS.awws?op=LectureDesModeles",
							new OutTimeConfig(1000 * 60 * 30, 1000 * 60 * 30,
									1000 * 60 * 30), map, xx, null, null);
			System.out.println("==over===");
			System.out.println("=================product fetch end====================================");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/*
	 * 拉取商品list 
	 * */
	public static String getProductSizeAndPriceBySoap(String sArticle) {

		String xx = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
				+ "xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\""
				+ "xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">"
				+ "<soap:Body>"
				+ "<LectureTarifWeb xmlns=\"urn:LcvMagWS\">"
				+ "<bCData xsd:type=\"xsd:string\">"
				+ "</bCData>"
				+ "<sUser xsd:type=\"xsd:string\">BION456</sUser>"
				+ "<sMdp xsd:type=\"xsd:string\">INI123</sMdp>"
				+ "<sArticle xsd:type=\"xsd:string\">"+sArticle+"</sArticle>"
				+ "</LectureTarifWeb>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";
		Map<String, String> map = new HashMap<String, String>();
		map.put("SOAPAction", "urn:LcvMagWS/LectureTarifWeb");
		map.put("Content-Type", "text/xml; charset=UTF-8");
		map.put("Content-Type", "application/soap+xml; charset=utf-8");
		String json = null;
		System.out.println("=================product: "+ sArticle +" size fetch begin====================================");
		try {
			json = HttpUtil45
					.operateData(
							"post",
							"soap",
							"http://80.12.82.220:8080/LCVMAGWS_WEB/awws/LcvMagWS.awws?op=LectureTarifWeb",
							new OutTimeConfig(1000 * 60 * 60, 1000 * 60 * 60,
									1000 * 60 * 60), map, xx, null, null);
			System.out.println("==over===");
			System.out.println("=================product "+ sArticle +" size fetch end====================================");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return json;
	}
}
