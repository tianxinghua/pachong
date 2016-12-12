package com.shangpin.pending.product.consumer.exception;
/**
 * <p>Title:EpHubPendingProductConsumerRuntimeException.java </p>
 * <p>Description: 系统运行时异常类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午3:31:54
 */
public class EpHubPendingProductConsumerRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6023297308516227747L;

	public EpHubPendingProductConsumerRuntimeException() {
		super();
	}

	public EpHubPendingProductConsumerRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EpHubPendingProductConsumerRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EpHubPendingProductConsumerRuntimeException(String message) {
		super(message);
	}

	public EpHubPendingProductConsumerRuntimeException(Throwable cause) {
		super(cause);
	}

}
