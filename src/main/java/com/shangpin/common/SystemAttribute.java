package com.shangpin.common;


/**
 * Created by suny on 14-12-2.
 */
public class SystemAttribute {
    /**
     * Http签名调用模式
     */
    private String method;//API接口名称
    private String session;//TOP分配给用户的SessionKey，通过登陆授权获取。
    private String timestamp;//时间戳，格式为yyyy-MM-dd HH:mm:ss
    private String format;//可选，指定响应格式。默认xml,目前支持格式为xml,json
    private String app_key;//TOP分配给应用的AppKey
    private String v;//API协议版本，可选值:2.0。
    private String sign;//API输入参数签名结果
    private String sign_method;//参数的加密方法选择，可选值是：md5,hmac。这个参数只存在于2.0中。

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_method() {
        return sign_method;
    }

    public void setSign_method(String sign_method) {
        this.sign_method = sign_method;
    }
    /**
     * Https免签名调用模式
     */
    private String access_token;//TOP分配给用户的access_token，通过oauth协议登陆授权获取。
    // 某个API是否需要传入access_token参数，需参考此API的API用户授权类型
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
