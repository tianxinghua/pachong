/**
 * 
 */
package com.shangpin.iog.webcontainer.front.conf;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月1日
 */
public class UAASUtil {
	public static final String TOKEN="_token_";
	private static String host="http://uaas.shangpin.com:8080/";
	
	public static String login(String userName,String password){
		String loginFace="facade/json/com.shangpin.uaas.api/Authentication/login";
		String token=request(loginFace,"['"+userName+"','"+password+"']");
		token=getVal(token);
		if("null".equals(token) || StringUtils.isBlank(token))
			return null;
		return token;
	}
	
	/**
	 * @param jsonStr
	 * @return
	 */
	private static String getVal(String jsonStr) {
		JSONObject js = JSONObject.fromObject(jsonStr);
		String err=(String)js.getString("err");
		if(!"null".equals(err))
			return null;
		return js.getString("val");
	}
	
	private static boolean hasErr(String jsonStr) {
		JSONObject js = JSONObject.fromObject(jsonStr);
		String err=(String)js.getString("err");
		if(!"null".equals(err))
			return false;
		return true;
	}
	
	public static boolean delayTokenTime(String token){
		String touchFace="facade/json/com.shangpin.uaas.api/Authentication/touch";
		String rs=request(touchFace,"['"+token+"']");
		return hasErr(rs);
	}
	/**
	 * 判断登录的token是否有访问某资源的权限
	 * @param token token
	 * @param uri 资源路径 保持与权限系统的一致 如：mvc-action://tms/Delivery/OrderManage/DeliveryInfo
	 * @return
	 */
	public static boolean isPermitted(String token,String uri){
		String permFace="facade/json/com.shangpin.uaas.api/Authorization/isPermitted";
		String rs=request(permFace,"['"+token+"','"+uri+"']");
		rs=getVal(rs);
		return Boolean.parseBoolean(rs);
	}
	/**
	 * 
	 * @param uri 接口相对路径
	 * @param jsonParam json数组字符串['token','uri']
	 * @return
	 */
	private static String request(String uri,String jsonParam){
		RestTemplate rst = new RestTemplate();
		MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
		param.add("params", jsonParam);
		String rs=rst.postForEntity(host+uri, param, String.class).getBody();
		System.out.println(rs);
		return rs;
	}
	
	public static void main(String[] args) {
		//login("admin", "123456");
		String token="9690286a-9f04-4a62-82be-eb58dabc26ee";
		//delayTokenTime(token);
		isPermitted(token, "mvc-action://tms/tms.shangpin.com/DeliveryInfo");
		isPermitted(token, "mvc-action://tms/Delivery/OrderManage/DeliveryInfo");
	}

	/**
	 * @param attribute
	 */
	public static void logout(String token) {
		String outFace="/facade/json/com.shangpin.uaas.api/Authentication/logout";
		request(outFace,"['"+token+"']");
	}
}
