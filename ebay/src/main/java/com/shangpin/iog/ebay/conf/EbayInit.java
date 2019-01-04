/**
 * 
 */
package com.shangpin.iog.ebay.conf;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月18日
 */
public class EbayInit {


	public static  String EBAY;
	//public static final String EBAY="TestEbay";
	private static String tradeApi;
    private static String apiUrl;
    private static String epsUrl;
    private static String signUrl;
    private static String shopingApi;
    private static String findApi;

    private static volatile int idx=0;
	static List<EbayAppConf> confs=new ArrayList<>();
    static ResourceBundle bdl = null;
    static{
    	init();
    }
    private static EbayAppConf initConf(ResourceBundle bdl){
    	EbayAppConf conf = new EbayAppConf();
    	try{
    		String token=bdl.getString("token");
    		if(token==null) return null;
    		conf.setToken(token);
    		conf.setDevKey(bdl.getString("DeveloperKey"));
    		conf.setAppKey(bdl.getString("ApplicationKey"));
    		conf.setCerKey(bdl.getString("CertificateKey"));
    		conf.setRuName(bdl.getString("ruName"));

    	}catch(Exception e){
    		return null;
    	}
        
        return conf;
    }
    /**
     * 可以在conf目录，或者根目录下找 propFileName文件
     * @param propFileName
     * @return 如果没有配置token则为null
     */
    private static EbayAppConf initConf(String propFileName){
    	try{
    		bdl=ResourceBundle.getBundle(propFileName);
    	}catch(Exception e){
    		bdl=ResourceBundle.getBundle("conf/"+propFileName);    		
    	}
        return initConf(bdl);
    }
    /**
	 * 可以在conf目录，或者根目录下找 appkey文件，其中的includeProfile是包含的其他文件<br/>
	 * appkey,appkey-prof1,appkey-prof2则includeProfile=prof1,prof2
	 */
	private static void init() {
		try{
			bdl=ResourceBundle.getBundle("appkey");
		}catch(Exception e){
			bdl=ResourceBundle.getBundle("conf/appkey");
		}
		EbayAppConf conf=initConf(bdl);
		if(conf!=null) confs.add(conf);
		apiUrl=bdl.getString("apiUrl");
        epsUrl=bdl.getString("epsUrl");
        signUrl=bdl.getString("signUrl");
        shopingApi=bdl.getString("shopingApi");
        findApi=bdl.getString("findApi");
        tradeApi=bdl.getString("tradeApi");
		EBAY = bdl.getString("supplierId");
        String profile=null;
		try{
			profile=bdl.getString("includeProfile");
		}catch(Exception e){
		}
		if(StringUtils.isNotBlank(profile)){
			String[] fileName=profile.split(",");
			for (String fn : fileName) {
				conf=initConf("appkey-"+fn);
				if(conf!=null) confs.add(conf);
			}
		}
		if(CollectionUtils.isEmpty(confs)){
			throw new RuntimeException("初始化app key失败！至少需要配置一个App");
		}
	}
	/**
	 * 循环获取配置的app
	 * @return
	 */
	private static EbayAppConf getAppKey(){
		if(idx>=confs.size())
			idx=0;
		idx++;
		return confs.get(idx-1); 
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
        EbayAppConf cnf=getAppKey();
        ApiCredential ac = new ApiCredential();
        ac.seteBayToken(cnf.getToken());
        ApiAccount act = new ApiAccount();
        act.setDeveloper(cnf.getDevKey());act.setApplication(cnf.getAppKey());
        act.setCertificate(cnf.getCerKey());
        ac.setApiAccount(act);
        context.setRuName(cnf.getRuName());
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
    	return String.format(shopingApi, getAppKey().getAppKey(),callName);
    }
    /**
     * 获取调用find service api方法的url<br/>
     * 已经封装appkey,版本
     * @param operaName 服务方法名
     * @return
     */
    public static String getFindCallUrl(String operaName){
    	return String.format(findApi, getAppKey().getAppKey(),operaName);
    }
    
    public static String getTradeCallUrl(String callName){
    	return String.format(tradeApi, getAppKey().getAppKey(),callName);
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
