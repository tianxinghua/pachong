package com.shangpin.iog.channeladvisor.service;

import com.channeladvisor.api.webservices.*;
import com.shangpin.iog.channeladvisor.service.axis.AxisClient;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import net.sf.json.JSONObject;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2015/8/10.
 */
public class Test {


    public final static QName SERVICE = new QName("http://api.channeladvisor.com/webservices/", "AdminService");
    
    private static Logger logInfo  = Logger.getLogger("info");
    private static Logger logError = Logger.getLogger("error");
    private static Logger logMongoDB = Logger.getLogger("MongoDB");
    
	public static void main(String[] args){
		Map<String,String> map = new HashMap<>();
		map.put("access_type", "offline");
		map.put("client_id", "qwmmx12wu7ug39a97uter3dz29jbij3j");
		map.put("response_type", "code");
		map.put("scope", "orders%20inventory");
		map.put("redirect_uri", "https://49.213.13.167:8443/iog/download/code");
		String uu = HttpUtil45.get(
				"https://api.channeladvisor.com/oauth2/authorize", null, map,
				"qwmmx12wu7ug39a97uter3dz29jbij3j", "TqMSdN6-LkCFA0n7g7DWuQ");
		
	}
	
	@org.junit.Test
	public void refreshTocken(){
//		String refresh_token = "6Rz4sozjjOFbdazaU_gjnnFwWvfG2VgG9L14kL9tB3w";
//		String kk = HttpUtil45.get("https://api.channeladvisor.com/oauth2/token?grant_type=refresh_token&refresh_token="+refresh_token, null, null);
//		System.out.println(kk);
//		logError.error(kk);
		OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
		String application_id = "qwmmx12wu7ug39a97uter3dz29jbij3j";
        String shared_secret = "TqMSdN6-LkCFA0n7g7DWuQ";
        Map<String,String> map = new HashMap<>();
        map.put("grant_type","refresh_token");
        //map.put("code",code);
        map.put("refresh_token", "6Rz4sozjjOFbdazaU_gjnnFwWvfG2VgG9L14kL9tB3w");
        map.put("redirect_uri","https://49.213.13.167:8443/iog/download/code");
        map.put("access_type", "offline");
        String kk = HttpUtil45.postAuth("https://api.channeladvisor.com/oauth2/token", map, timeConfig,application_id,shared_secret);
        System.out.println("kk = "  + kk);
        String access_token = JSONObject.fromObject(kk).getString("access_token");
        System.out.println(access_token);
	}
	
//	@org.junit.Test
//	public void testLog(){
//		Logger logInfo  = Logger.getLogger("info");
//		Logger logError = Logger.getLogger("error");
//		Logger logMongoDB = Logger.getLogger("MongoDB");
//		logInfo.info("test info--------------");
//		logError.error("error==================");
//		logMongoDB.info("mon-++++++++++++++++++");
//		Logger log = Logger.getLogger(Test.class);
//		log.error("oooooooooooooooooooooooooooooo-p-----------------");
//		try {
//			String path = this.getClass().getClassLoader().getResource("log4j.properties").getPath();
//			System.out.println(path);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	@org.junit.Test
	public void TTT(){
		String url = "https://api.channeladvisor.com/v1/Products?access_token=rYCixQzMZEBEZMOtTUnnqYKu_A2Ef_x9Ch2CeO0m5uA&$skip=200";
		url = url.replaceFirst(url.substring(url.indexOf("=")+1, url.indexOf("&")),"ppppppppppppppppp");
		System.out.println(url);
	}
	
}
