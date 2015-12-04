package com.shangpin.iog.facade.security;

import java.io.Serializable;

/**
 * Api异常类
 */
public class ApiException extends Exception implements Serializable {

    public ApiException(String message) {
        super(message);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 错误代码
     */
    private Integer errorCode;

}
