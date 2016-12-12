package com.shangpin.pending.product.consumer.exception;
/**
 * <p>Title:EpHubPendingProductConsumerException.java </p>
 * <p>Description: 编译时异常类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午3:31:17
 */
public class EpHubPendingProductConsumerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3001024862888741412L;

	public EpHubPendingProductConsumerException() {
		super();
	}

	public EpHubPendingProductConsumerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EpHubPendingProductConsumerException(String message, Throwable cause) {
		super(message, cause);
	}

	public EpHubPendingProductConsumerException(String message) {
		super(message);
	}

	public EpHubPendingProductConsumerException(Throwable cause) {
		super(cause);
	}

}
