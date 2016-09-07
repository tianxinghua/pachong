package com.shangpin.iog.biondini.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.mongodb.util.JSON;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class Test {

	public static void main(String[] args) {
		Map map = new HashMap();
		map.put("usr","mktpl_shankpin");
		map.put("psw","er230LK");
//	
//		
//		
//		
		String js = HttpUtil45.get("https://marketplace.chalco.net/apiboutique/catalog.svc/GetToken",new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2),map);
		JSONObject json =JSONObject.fromObject(JSON.parse(js));
		System.out.println(js);
		
		map = new HashMap();
		map.put("token:",json.get("Result"));
		map.put("Barcode:","ss");
		String jss = HttpUtil45.get("https://marketplace.chalco.net/apiboutique/catalog.svc/GetStock",new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2),map);
		System.out.println(jss);
		
		
		map = new HashMap();
		map.put("token:",json.get("Result"));
		map.put("year","2016");
		map.put("season","PE");
		map.put("RequestType","INCREMENTAL");
		String jas = HttpUtil45.post("https://marketplace.chalco.net/apiboutique/catalog.svc/GetProductList",map,new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2));
		System.out.println(jas);
		
		
//		getLectureDesTablesBySoap();AUTHE94944E59E8D4EE48E390A03659562C720160830093531
		
	}
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
}
