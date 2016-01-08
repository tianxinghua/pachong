package com.shangpin.iog.inviqa.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.inviqa.dto.Product;
import com.shangpin.iog.pavinGroup.util.TestAPI;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MyJsonUtil {
	
	private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
        	bdl=ResourceBundle.getBundle("conf");
    }
    
    final static String MAGENTO_API_KEY = "anr63gksisphqnkcd671nh88egnfqtu3";
	final static String MAGENTO_API_SECRET = "vhpk60p6srj4qtgbyq7wc3wxe7ybyzv4";
	final String MAGENTO_REST_API_URL = "https://glamorous-uat.phplab.co.uk/api/rest";
	static List<Product> list = new ArrayList<Product>();
	public static List<Product> getProductList() {
			
		OAuthService service = new ServiceBuilder().provider(TestAPI.class)
				.apiKey(MAGENTO_API_KEY).apiSecret(MAGENTO_API_SECRET).build();
		Token accessToken = new Token("tcm525shdvbw0jg68ju87njxpmwot41v",
				"hugcwsx5e8yrze1z7c11hgdaxe8aeits");
		
		getProductList(service,accessToken,0);
		System.out.println(list.size());
		return list;
	}
	static int i = 1;
	public static void getProductList(OAuthService service,Token accessToken,int page){
	
		
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://glamorous-uat.phplab.co.uk/api/rest/shangpin/product?limit=100&page="+i,
				service);
		service.signRequest(accessToken, request);
		Response response = request.send();
		String json = response.getBody();
		if(!json.isEmpty()){
			List<Product> retList = new Gson().fromJson(json,  
	                new TypeToken<List<Product>>() {  
	                }.getType());  
			System.out.println(json);
			System.out.println(retList.size());
			list.addAll(retList);
//			if(retList.size()>=10){
				i++;
				getProductList(service,accessToken,i);
//			}
		}
		
	}
}
