package com.shangpin.iog.itemInfo_purchase.test;

import com.shangpin.iog.itemInfo_purchase.service.SoapXmlUtil;


public class testMain {

	public static void main(String[] args){
		String serviceUrl = "http://service.alducadaosta.com/EcSrv/Services.asmx?op=InsertOrder";
		String sopAction = "http://service.alducadaosta.com/EcSrv/InsertOrder";
		String contentType = "text/xml; charset=utf-8"; //或者    application/soap+xml; charset=utf-8
		String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
								"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"+
								"  <soap12:Body>"+
								"    <InsertOrder xmlns=\"http://service.alducadaosta.com/EcSrv\">"+
								"      <Identifier>201510040001</Identifier>"+
								"      <Order>201510040001</Order>"+
								"      <SkuID>202885102407</SkuID>"+
								"      <Quantity>1</Quantity>"+
								"    </InsertOrder>"+
								"  </soap12:Body>"+
								"</soap12:Envelope>";
		
		String str = SoapXmlUtil.getSoapXml(serviceUrl, sopAction, contentType, soapRequestData);
		System.out.println(str);
	}
}
