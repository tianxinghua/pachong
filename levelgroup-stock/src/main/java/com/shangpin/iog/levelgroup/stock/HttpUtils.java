package com.shangpin.iog.levelgroup.stock;


import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.Consts;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpUtils {


	private static final Integer CONNECTION_TIMEOUT = 3600000;
	private static final Integer SO_TIMEOUT = 3600000;
	public static final String APPLICATION_XML = "application/xml";

	public static String get(String uri,int reconnectNum){
		return get(uri, null, null,reconnectNum);
	}

	public static String get(String uri, String encoding,int reconnectNum) {
		return get(uri, null, encoding,reconnectNum);
	}

	public static String get(String uri, Header[] headers,int reconnectNum) {
		return get(uri, headers, null,reconnectNum);
	}

	public static List<String> getContentListByInputSteam(String uri, Header[] headers, String encoding,int reconnectNum){
		List<String> contentList = new ArrayList<>();

		GetMethod getMethod = new GetMethod(uri);
		if (encoding != null) {
			getMethod.getParams().setHttpElementCharset(encoding);
			getMethod.getParams().setCredentialCharset(encoding);
			getMethod.getParams().setContentCharset(encoding);
		}
		if (headers != null) {
			for (Header header : headers) {
				getMethod.addRequestHeader(header);
			}
		}

		HttpClient httpclient = new HttpClient();
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
		try {
			httpclient.executeMethod(getMethod);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(), Consts.UTF_8));
            String value="";
            int count = 0;
            while((value = reader.readLine()) != null) {
                contentList.add(value);
                count++;
            }
		} catch (Exception e) {
			int i = 0;
            while (i<(reconnectNum == 0 ? 3 : reconnectNum)){
				try {
					httpclient.executeMethod(getMethod);
					if (getMethod.getResponseBodyAsStream() != null){
						final BufferedReader reader = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(), Consts.UTF_8));
			            String value="";
			            int count = 0;
			            while((value = reader.readLine()) != null) {
			                contentList.add(value);
			                count++;
			            }
	                    break;
	                }else {
	                    i++;
	                }
				}catch(Exception e1){
					e1.printStackTrace();
				}finally {
					getMethod.releaseConnection();
					((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
				}
            }
		} finally {
			getMethod.releaseConnection();
			((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
		}

		return contentList;
	}
	
	public static String get(String uri, Header[] headers, String encoding,int reconnectNum) {
		String response = null;
		GetMethod getMethod = new GetMethod(uri);
		if (encoding != null) {
			getMethod.getParams().setHttpElementCharset(encoding);
			getMethod.getParams().setCredentialCharset(encoding);
			getMethod.getParams().setContentCharset(encoding);
		}
		if (headers != null) {
			for (Header header : headers) {
				getMethod.addRequestHeader(header);
			}
		}

		HttpClient httpclient = new HttpClient();
		// timeout
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
		try {
			int status = httpclient.executeMethod(getMethod);
			response = new String(getMethod.getResponseBody(), "utf-8");
		} catch (Exception e) {
			int i = 0;
            while (i<(reconnectNum == 0 ? 3 : reconnectNum)){
            	try {
            		int status = httpclient.executeMethod(getMethod);
            		if (status == 200){
            			response = new String(getMethod.getResponseBody(), "utf-8");
            			break;
            		}else{
            			i++;
            		}
        			
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
		} finally {
			getMethod.releaseConnection();
			((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
		}

		return response;
	}
}
