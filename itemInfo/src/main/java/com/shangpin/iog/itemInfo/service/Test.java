package com.shangpin.iog.itemInfo.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;


public class Test {
	
//	public static String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
//            "  <soap12:Body>\n" +
//            "    <GetAllItems xmlns=\"http://tempuri.org/\" />\n" +
//            "  </soap12:Body>\n" +
//            "</soap12:Envelope>";
	
//	public static String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
//											"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
//											  "<soap:Body>"+
//											    "<GetSku4Platform xmlns=\"http://service.alducadaosta.com/EcSrv\">"+
//											      "<Customer>shangpin</Customer>"+
//											    "</GetSku4Platform>"+
//											"  </soap:Body>"+
//											"</soap:Envelope>";
	public static String soapRequestData ="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
											"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"+
											  "<soap12:Body>"+
											    "<GetSku4Platform xmlns=\"http://service.alducadaosta.com/EcSrv\">"+
											      "<Customer>shangpin</Customer>"+
											    "</GetSku4Platform>"+
											  "</soap12:Body>"+
											"</soap12:Envelope>";
	public static void main(String[] arg){
//		Test t= new Test();
//		t.getResAsStream(
//				"http://service.alducadaosta.com/EcSrv/Services.asmx?op=GetItem4Platform",
//				"http://service.alducadaosta.com/EcSrv/GetItem4Platform",
//				"text/xml; charset=UTF-8");
		/*String result = "<DocumentElement xmlns=\"\">"
				+ "<SkuStok diffgr:id=\"SkuStok1\">"
				+ "</SkuStok>"
				+ "<SkuStok diffgr:id=\"SkuStok1\">"
				+ "<sku_id>202885102407</sku_id>"
				+ "<product_id>2028851024</product_id>"	
				+ "<title>Shirt</title>"
				+ "<item_url>https://www.alducadaosta.com/ita/product/2028851024"
				+ "</item_url>";
		String rtStr = result.substring(result.indexOf("<DocumentElement"), 
				result.lastIndexOf("</SkuStok>"));
		rtStr += "</DocumentElement>";
		System.out.println(rtStr);*/
//		String ss  = "<DocumentElement xmlns=\"\"><SkuStok diffgr:id=\"SkuStok1\" msdata:rowOrder=\"0\"><sku_id>202885102407</sku_id><product_id>2028851024</product_id>";
//		System.out.println(ss.replace("diffgr:id=\"(.*)\"", ""));
//		String teStr = "pic1:https://alducadaostastorage.blob.core.windows.net/product/16902/original/1b043f87-4d35-4b44-aac1-6609241038b7.jpg,pic2:https://alducadaostastorage.blob.core.windows.net/product/16902/original/e6910034-7fd0-48b7-92d3-85bf2ae2e56e.jpg,pic3:https://alducadaostastorage.blob.core.windows.net/product/16902/original/d4e49736-e4a0-4c22-a036-a71349e24876.jpg";
//		String param[] = teStr.split(",");
//		List<String> list = new ArrayList();
//		for(String str : param){
//			String ss = str.replace(str.substring(0, str.indexOf(":")+1), "");
//			list.add(ss);
//		}
//		for(String str : list){
//			System.out.println(str);
//		}
		
	}
	
	public void getResAsStream(String url,String sopAction,String contentType,String localPath){
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("SOAPAction", sopAction);
        postMethod.setRequestHeader("Content-Type", contentType);
        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);
        
        int returnCode=0;
        try {
        	//httpClient.setConnectionTimeout(1000*60*5);
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
            
            BufferedInputStream bufferedInputStream = 
            		new BufferedInputStream(postMethod.getResponseBodyAsStream());
            byte[] ims=new byte[1024*1024*100];
            
            System.out.println(postMethod.getResponseContentLength());
            
            File file = new File(localPath);
            if(!file.exists()){
            	file.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(localPath)) );
            int i = -1;
            while((i=bufferedInputStream.read(ims)) != -1){
            	System.out.println(i+"----------------------------");
            	bufferedOutputStream.write(ims,0,i);
            	
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            bufferedInputStream.close();
            System.out.println("***************完成********************");
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

}
