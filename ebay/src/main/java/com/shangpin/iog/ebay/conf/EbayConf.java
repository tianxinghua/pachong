/**
 * 
 */
package com.shangpin.iog.ebay.conf;

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
	public static final String EBAY="ebay#"; 
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
       // Locale locale = new Locale("zh","CN");
        ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("conf-pro");
        devKey = RESOURCE_BUNDLE.getString("DeveloperKey");
        appKey = RESOURCE_BUNDLE.getString("ApplicationKey");
        cerKey = RESOURCE_BUNDLE.getString("CertificateKey");
        ruName = RESOURCE_BUNDLE.getString("ruName");
        token = RESOURCE_BUNDLE.getString("token");
        apiUrl = RESOURCE_BUNDLE.getString("apiUrl");
        epsUrl = RESOURCE_BUNDLE.getString("epsUrl");
        signUrl = RESOURCE_BUNDLE.getString("signUrl");
        shopingApi = RESOURCE_BUNDLE.getString("shopingApi");
        findApi = RESOURCE_BUNDLE.getString("findApi");
        tradeApi =RESOURCE_BUNDLE.getString("tradeApi");
        
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
}
