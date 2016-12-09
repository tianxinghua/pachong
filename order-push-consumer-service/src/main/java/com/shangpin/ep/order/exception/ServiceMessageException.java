package com.shangpin.ep.order.exception;

/**
 * User: lizhongren@vansky.cn
 * Date: 12-10-1
 * Time: 下午6:34
 * 自定义消息异常
 * 如果错误信息在外统一定义，可返回错误编号
 */
public class ServiceMessageException extends ServiceException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4443379005069532103L;
	private String errorCode;//错误编号
    private String errorMessge;//展现给前端的错误信息
    private Throwable cause;//错误原因


    public ServiceMessageException(String errorMessge) {
        this.errorMessge = errorMessge;
    }

    public ServiceMessageException(String errorCode, String errorMessge) {
        this.errorCode = errorCode;
        this.errorMessge = errorMessge;
    }

    public ServiceMessageException(String errorCode, String errorMessge, Throwable cause) {

        this.errorCode = errorCode;
        this.errorMessge = errorMessge;
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public String getErrorMessge() {
        return errorMessge;
    }

    public void setErrorMessge(String errorMessge) {
        this.errorMessge = errorMessge;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
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
