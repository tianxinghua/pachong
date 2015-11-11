package com.shangpin.iog.channeladvisor.purchase.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
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

import net.sf.json.JSONObject;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class UtilOfChannel {

	/**
	 * 获取新的访问令牌
	 * @param timeConfig 连接超时时间
	 * @return 新的令牌口令码
	 */
	public static String getNewToken(OutTimeConfig timeConfig){
		String application_id = "qwmmx12wu7ug39a97uter3dz29jbij3j";
        String shared_secret = "TqMSdN6-LkCFA0n7g7DWuQ";
        Map<String,String> map = new HashMap<>();
        map.put("grant_type","refresh_token");
        map.put("refresh_token", "6Rz4sozjjOFbdazaU_gjnnFwWvfG2VgG9L14kL9tB3w");
        map.put("redirect_uri","https://49.213.13.167:8443/iog/download/code");
        String kk = HttpUtil45.postAuth("https://api.channeladvisor.com/oauth2/token", map, timeConfig,application_id,shared_secret);
        System.out.println("kk = "  + kk);
        return JSONObject.fromObject(kk).getString("access_token");
	}
	
}
