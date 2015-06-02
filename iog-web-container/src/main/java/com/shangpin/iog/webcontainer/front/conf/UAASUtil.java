/**
 * 
 */
package com.shangpin.iog.webcontainer.front.conf;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月1日
 */
public class UAASUtil {
	/**
	 * 权限token
	 */
	public static final String TOKEN="_token_";
	public static final String APPMENUSET = "_menu_";
	/**
	 * 超时时间
	 */
	public static int TOKEN_OUT_SEC=7200;//120min
	public static String loginURI="login.html?client_url={clientURL}";
	public static String host="http://uaas.shangpin.com:8080/";
	static String appUrl="http://iog.shangpin.com"; 
	
	/**
	 * 验证登录授权的token
	 * @param token uaas返回的令牌
	 * @return
	 */
	public static boolean isValid(String token){
		String loginFace="facade/json/com.shangpin.uaas.api/Authentication/isValid";
		String jsonStr=request(loginFace,token);
		String isV=getVal(jsonStr);
		if(isV==null) return false;
		return Boolean.parseBoolean(isV);
	}
	
	/**
	 * 返回的字符串中的val值
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
	/**
	 * 判断即可返回的是否有错误
	 * @param jsonStr
	 * @return
	 */
	private static boolean hasErr(String jsonStr) {
		JSONObject js = JSONObject.fromObject(jsonStr);
		String err=(String)js.getString("err");
		if(!"null".equals(err))
			return false;
		return true;
	}
	/**
	 * 延时授权登录，token
	 * @param token
	 * @return
	 */
	public static boolean delayTokenTime(String token){
		String touchFace="facade/json/com.shangpin.uaas.api/Authentication/touch";
		String rs=request(touchFace,token);
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
		String rs=request(permFace,token,uri);
		rs=getVal(rs);
		return Boolean.parseBoolean(rs);
	}
	/**
	 * 请求接口，返回响应json串
	 * @param uri 接口相对路径
	 * @param jsonParam json数组字符串,如：['token','uri']
	 * @return
	 */
	private static String request(String uri,String... jsonParam){
		RestTemplate rst = new RestTemplate();
		MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
		StringBuffer sb=new StringBuffer();
		sb.append("[");
		for (int i = 0; i < jsonParam.length-1; i++) {
			sb.append("'");sb.append(jsonParam[i]);sb.append("',");
		}
		sb.append("'");sb.append(jsonParam[jsonParam.length-1]);sb.append("'");
		sb.append("]");
		param.add("params", sb.toString());
		String rs=rst.postForEntity(host+uri, param, String.class).getBody();
		System.out.println(rs);
		return rs;
	}
	/**
	 * 注销登录
	 * @param attribute
	 */
	public static void logout(String token) {
		String outFace="facade/json/com.shangpin.uaas.api/Authentication/logout";
		request(outFace,token);
	}
	/**
	 * 获取token的顶级菜单
	 * @param token
	 * @return 
	 */
	public static List<MenuDTO> findTopMenusByToken(String token){
		String menuFace="facade/json/com.shangpin.uaas.api/User/findTopMenusByToken";
		String json=request(menuFace,token);
		List<MenuDTO> rtnMenu = json2Obj(getVal(json));
		return rtnMenu;
	}

	/**
	 * @param json
	 * @return
	 */
	private static List<MenuDTO> json2Obj(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class,MenuDTO.class);
		List<MenuDTO> rtnMenu= null;
		try {
			rtnMenu = objectMapper.readValue(json,javaType);
		} catch (IOException e) {
		}
		return rtnMenu;
	}
	/**
	 * 获取用户在本系统的权限菜单
	 * @param token
	 * @return
	 */
	public static List<MenuDTO> findAPPMenus(String token){
		String menuFace="facade/json/com.shangpin.uaas.api/User/findMenusByAppCode";
		String json=request(menuFace,token,"CSS");
		List<MenuDTO> rtnMenu = json2Obj(getVal(json));
		System.out.println(JSONArray.fromObject(rtnMenu).toString());
		return rtnMenu;
	}
	/**
	 * 获取redirect到UAAS的url
	 * @param appURL 如：http://iog.shangpin.com,若为null跳转默认主页
	 * @return
	 */
	public static String redirectUAAS(String appURL){
		return (host+loginURI).replaceFirst("{clientURL}", appURL==null?appUrl:appURL);
	}
	public static void main(String[] args) {
		String token="8a0e241d-d6f9-4b6e-b903-e4f40a64d4dc";//login("admin", "123456");
		findAPPMenus(token);
		
		//delayTokenTime(token);
		//isPermitted(token, "mvc-action://tms/tms.shangpin.com/DeliveryInfo");
		//isPermitted(token, "mvc-action://tms/Delivery/OrderManage/DeliveryInfo");
	}

}
