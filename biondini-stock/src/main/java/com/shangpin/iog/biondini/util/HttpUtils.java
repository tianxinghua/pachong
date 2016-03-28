package com.shangpin.iog.biondini.util;

import java.util.HashMap;
import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

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
}