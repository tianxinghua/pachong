package com.shangpin.iog.facade.security;

/**
 * Created by Administrator on 2015/3/10.
 */
public class ExceptionResponse {

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

}
