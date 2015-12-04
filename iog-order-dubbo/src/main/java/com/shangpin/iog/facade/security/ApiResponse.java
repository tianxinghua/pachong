package com.shangpin.iog.facade.security;

import java.io.Serializable;
import java.util.Objects;

/**
 * Api响应类
 */
public class ApiResponse implements Serializable {

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * 响应代码
     * 0:正常，其他都是错误
     */
    private Integer responseCode;

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    /**
     * 响应的消息
     * ok:正常，其他表示错误信息
     */
    private String responseMsg;

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    /**
     * 响应对象的实体
     */
    private Object response;

}
