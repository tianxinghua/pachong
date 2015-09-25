package com.shangpin.framework;

/**
 * Created by loyalty on 15/2/3.
 */
public class DataException extends RuntimeException {

    private String errorCode;//错误编号
    private String errorMessge;//展现给前端的错误信息
    private Throwable cause;//错误原因

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessge() {
        return errorMessge;
    }

    public void setErrorMessge(String errorMessge) {
        this.errorMessge = errorMessge;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    /**
     * 获取错误信息
     * @return:错误信息
     */
    public String getExceptionMessge(){
        if(null!=this.errorMessge&&!"".equals(this.errorMessge)){
            return this.errorMessge;
        }else if(null!=cause){
            return cause.toString();
        }else{
            return null;
        }
    }

    @Override
    public String getMessage(){
        return getExceptionMessge();

    }
}
