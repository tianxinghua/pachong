package com.shangpin.ep.order.exception;
/**
 * <p>Title:EpOrderException.java </p>
 * <p>Description: EP订单系统编译时异常类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月20日 下午8:37:20
 */
public class EpOrderServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7285000090183297524L;

	public EpOrderServiceException() {
		super();
	}

	public EpOrderServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EpOrderServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public EpOrderServiceException(String message) {
		super(message);
	}

	public EpOrderServiceException(Throwable cause) {
		super(cause);
	}

}
