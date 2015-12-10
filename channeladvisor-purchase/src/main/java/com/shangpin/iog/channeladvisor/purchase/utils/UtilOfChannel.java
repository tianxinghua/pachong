package com.shangpin.iog.channeladvisor.purchase.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class UtilOfChannel {

	private static Logger logger = Logger.getLogger("error");
	public static String ERROR = "error";
	public static String SUCCESSFUL = "successful";

	/**
	 * 获取新的访问令牌
	 * 
	 * @param timeConfig
	 *            连接超时时间
	 * @return 新的令牌口令码
	 * 
	 * @throws ServiceException 
	 */
	public static String getNewToken(OutTimeConfig timeConfig){
		String application_id = "qwmmx12wu7ug39a97uter3dz29jbij3j";
		String shared_secret = "TqMSdN6-LkCFA0n7g7DWuQ";
		Map<String, String> map = new HashMap<>();
		map.put("grant_type", "refresh_token");
		map.put("refresh_token", "6Rz4sozjjOFbdazaU_gjnnFwWvfG2VgG9L14kL9tB3w");
		map.put("redirect_uri", "https://49.213.13.167:8443/iog/download/code");
		String result = null;
		try{
			
			String kk = HttpUtil45.operateData("post", "", "https://api.channeladvisor.com/oauth2/token", timeConfig, map, null, application_id, shared_secret);
			
			System.out.println("kk = " + kk);
			result = JSONObject.fromObject(kk).getString("access_token");
			
		}catch(Exception ex){
			logger.error(ex);
			ex.printStackTrace();
			return UtilOfChannel.ERROR;
		}
		
		return result==null?UtilOfChannel.ERROR:result;
	}
	
	public static String getUTCTime(){
    	// 1、取得本地时间：  
        Calendar cal = Calendar.getInstance() ;  
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss" );
        // 2、取得时间偏移量：  
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);  
        // 3、取得夏令时差：  
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);  
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：  
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss." );
        return sdf.format(cal.getTime())+"1Z";
    }

}
