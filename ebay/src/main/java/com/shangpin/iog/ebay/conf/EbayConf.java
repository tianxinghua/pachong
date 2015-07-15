/**
 * 
 */
package com.shangpin.iog.ebay.conf;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月18日
 */
public class EbayConf {
	public static final String EBAY="2015071301325";
	//private static Map<String,String> brandStore = null; 
    private static String tradeApi = null;
	private static String devKey;
    private static String appKey;
    private static String cerKey;
    private static String ruName;
    private static String token;
    private static String apiUrl;
    private static String epsUrl;
    private static String signUrl;
    private static String shopingApi;
    private static String findApi;
    static {
        ResourceBundle bdl = ResourceBundle.getBundle("conf-pro3");
        devKey = bdl.getString("DeveloperKey");
        appKey = bdl.getString("ApplicationKey");
        cerKey = bdl.getString("CertificateKey");
        ruName = bdl.getString("ruName");
        token = bdl.getString("token");
        apiUrl = bdl.getString("apiUrl");
        epsUrl = bdl.getString("epsUrl");
        signUrl = bdl.getString("signUrl");
        shopingApi = bdl.getString("shopingApi");
        findApi = bdl.getString("findApi");
        tradeApi =bdl.getString("tradeApi");
        
    }
    /**
     * 获取trading service api的上下文
     * @return
     */
    public static ApiContext getApiContext(){
        ApiContext context = new ApiContext();
        context.setApiServerUrl(apiUrl);
        context.setEpsServerUrl(epsUrl);
        context.setSignInUrl(signUrl);
        ApiCredential ac = new ApiCredential();
        ac.seteBayToken(token);
        ApiAccount act = new ApiAccount();
        act.setDeveloper(devKey);act.setApplication(appKey);
        act.setCertificate(cerKey);
        ac.setApiAccount(act);
        context.setRuName(ruName);
        context.setApiCredential(ac);
        return context;
    }
    /**
     * 获取调用shopping Service api方法的url<br/>
     * 已经封装appkey,版本
     * @param callName 服务调用名
     * @return
     */
    public static String getShopingCallUrl(String callName){
    	return String.format(shopingApi, appKey,callName);
    }
    /**
     * 获取调用find service api方法的url<br/>
     * 已经封装appkey,版本
     * @param operaName 服务方法名
     * @return
     */
    public static String getFindCallUrl(String operaName){
    	return String.format(findApi, appKey,operaName);
    }
    
    public static String getTradeCallUrl(String callName){
    	return String.format(tradeApi, appKey,callName);
    }
    /**
     * 获取store-brand.properties文件中配置的brand，store<br/>
     * 用于find指定商铺的品牌的item
     * @return key：brand，value：`后分隔的storeName
     */
    public static Map<String,String> getStoreBrand(String fileName){
    	Map<String,String> brandStore=new HashMap<>();    		
		ResourceBundle bdl=ResourceBundle.getBundle(fileName);
		Enumeration<String> brands=bdl.getKeys();
		while(brands.hasMoreElements()){
			String brand=brands.nextElement();
			String stores=bdl.getString(brand);
			brandStore.put(brand, stores);
		}
    	return brandStore;
    }
    
}
