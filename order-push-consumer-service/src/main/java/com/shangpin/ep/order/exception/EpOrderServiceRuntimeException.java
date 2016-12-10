package com.shangpin.ep.order.exception;
/**
 * <p>Title:EpOrderRuntimeException.java </p>
 * <p>Description: Ep订单系统运行时异常类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月20日 下午8:38:04
 */
public class EpOrderServiceRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6801841262039960829L;

	public EpOrderServiceRuntimeException() {
		super();
	}

	public EpOrderServiceRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EpOrderServiceRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EpOrderServiceRuntimeException(String message) {
		super(message);
	}

	public EpOrderServiceRuntimeException(Throwable cause) {
		super(cause);
	}
}
