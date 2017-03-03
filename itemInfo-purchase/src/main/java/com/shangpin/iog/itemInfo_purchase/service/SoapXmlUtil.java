package com.shangpin.iog.itemInfo_purchase.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

public class SoapXmlUtil {
	
	private static Logger loggerError = Logger.getLogger("error");
	private static Logger logger = Logger.getLogger("info");
	
	
	/**
	 * 获取soap格式的xml信息
	 * @param serviceUrl 要下载的网址
	 * @param sopAction soapaction
	 * @param contentType Content-Type
	 * @param soapRequestData 请求的xml的头信息
	 * @return 如果成功返回信息，否则返回null
	 */
	public static String getSoapXml(String serviceUrl ,String sopAction,String contentType,String soapRequestData){
		
		InputStream in = null;
		try {
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(1000*60*10);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(1000*60*10);
			PostMethod postMethod = new PostMethod(serviceUrl);
	        postMethod.setRequestHeader("SOAPAction", sopAction);
	        postMethod.setRequestHeader("Content-Type", contentType);	        
	        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
	        
	        postMethod.setRequestEntity(requestEntity);	        
	        int returnCode=0;
            returnCode = httpClient.executeMethod(postMethod);
            logger.info("getSoapXml.returnCode=="+returnCode);
            
            if(returnCode == 200){
            	in = postMethod.getResponseBodyAsStream();
            	logger.info("postMethod.getResponseContentLength()=========="+postMethod.getResponseContentLength());
                byte[] ims=new byte[(int)postMethod.getResponseContentLength()];
                in.read(ims);
                return new String(ims);
            }
            
        } catch (Exception e) {
        	loggerError.error("推送时，发生错误============"+e.toString()); 
        }finally{
        	try {
        		if(in != null){
        			in.close(); 
        		}
			} catch (Exception e2) {
				loggerError.error(e2); 
			}
        }
		
		return null;
	}
	
	/**
	 * 获取soap格式的xml信息,返回文件路径
	 * @param serviceUrl 要下载的网址
	 * @param sopAction soapaction
	 * @param contentType Content-Type
	 * @param soapRequestData 请求的xml的头信息
	 * @param localUrl 将要保存xml信息文件的本地目录,如："C:\\soapxml.xml"
	 * @return 如果下载成功返回本地目录，否则返回null
	 */
	public static String downloadSoapXmlAsFile(String serviceUrl ,String sopAction,String contentType,String soapRequestData,String localUrl){
		
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(serviceUrl);
        postMethod.setRequestHeader("SOAPAction", sopAction);
        postMethod.setRequestHeader("Content-Type", contentType);
        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);
        
        int returnCode=0;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {

            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
            
            if(returnCode == 200){
            	bufferedInputStream = 
                		new BufferedInputStream(postMethod.getResponseBodyAsStream());
            	System.out.println(postMethod.getResponseContentLength());
                byte[] ims=new byte[1024*1024*100];                
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(localUrl)) );
                int i = -1;
                while((i=bufferedInputStream.read(ims)) != -1){
                	System.out.println(i+ "正在读取...");
                	bufferedOutputStream.write(ims,0,i);
                	
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                bufferedInputStream.close();
                System.out.println("***************下载完成********************");
                
                return localUrl;
            }
            
        } catch (Exception e) {
        	e.printStackTrace();
        }finally{
        	try {
				bufferedOutputStream.close();
				bufferedInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
        }
		
		return null;
	}
	
	/**
	 * soapxml格式的字符串转化为SOAPMessage对象 <br>
	 * @param soapString soap xml格式字符串
	 * @return 如果转化成功则返回SOAPMessage对象，否则返回null
	 */
	public static SOAPMessage formatSoapString(String soapString) {
		try {
			MessageFactory msgFactory = MessageFactory
					.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			SOAPMessage reqMsg = msgFactory.createMessage(new MimeHeaders(),
					new ByteArrayInputStream(soapString.getBytes("UTF-8")));
			reqMsg.saveChanges();
			return reqMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将soapxml格式的xml文件转化为SOAPMessage对象
	 * @param filePath 文件路径
	 * @return 如果转化成功返回SOAPMessage对象，否则返回null
	 */
	public static SOAPMessage formatSoapStringByFilePath(String filePath){

		try {
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			StringBuilder stringBuilder = new StringBuilder();
			String content = "";
			while ((content = bufferedReader.readLine()) != null) {
				stringBuilder.append(content);
			}
			String result = stringBuilder.toString();
			
			return formatSoapString(result);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

}