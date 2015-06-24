/**
 * 
 */
package com.shangpin.iog.ebay.conf;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;

import java.util.ResourceBundle;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月18日
 */
public class EbayConf {

    private static String devKey;
    private static String appKey;
    private static String cerKey;
    private static String ruName;
    private static String token;
    private static String apiUrl;
    private static String epsUrl;
    private static String signUrl;

    static {

        ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("conf");
        devKey = RESOURCE_BUNDLE.getString("DeveloperKey");
        appKey = RESOURCE_BUNDLE.getString("ApplicationKey");
        cerKey = RESOURCE_BUNDLE.getString("CertificateKey");
        ruName = RESOURCE_BUNDLE.getString("ruName");
        token = RESOURCE_BUNDLE.getString("token");
        apiUrl = RESOURCE_BUNDLE.getString("apiUrl");
        epsUrl = RESOURCE_BUNDLE.getString("epsUrl");
        signUrl = RESOURCE_BUNDLE.getString("signUrl");
    }
    public EbayConf() {
    }

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
        context.getApiCredential();
        return context;
    }

    public static String getRuName() {
        return ruName;
    }

    public static void setRuName(String ruName) {
        EbayConf.ruName = ruName;
    }

    public void setDevKey(String devKey) {
        this.devKey = devKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setCerKey(String cerKey) {
        this.cerKey = cerKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCerKey() {
        return cerKey;
    }
}
