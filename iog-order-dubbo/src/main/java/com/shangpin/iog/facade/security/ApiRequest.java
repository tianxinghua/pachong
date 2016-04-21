package com.shangpin.iog.facade.security;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Api请求类
 */
public class ApiRequest implements Serializable {

    /**
     * <font clolo='red'>系统级参数</font>
     * 申请的应用key，对应供应商实体的UserName
     */
    private String app_key;

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    /**
     * <font clolo='red'>系统级参数</font>
     * 申请的应用secret，对应供应商实体的PrivateKey
     */
    private String app_secret;

    public String getApp_secret() {
        return app_secret;
    }

    public void setApp_secret(String app_secret) {
        this.app_secret = app_secret;
    }

    /**
     * <font clolo='red'>系统级参数</font>
     * 时间戳，防钓鱼，统一为北京时间，格林威治时间+8小时
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * <font clolo='red'>系统级参数</font>
     * 签名
     */
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * 请求对象的实体
     */
    private String request;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

}
