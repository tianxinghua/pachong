package com.shangpin.supplier.product.consumer.exception;
/**
 * <p>Title:EPHubRuntimeException.java </p>
 * <p>Description: 整个系统中所使用的运行时异常类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月6日 下午7:45:10
 */
public class EpHubSupplierProductConsumerRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3654221139303138270L;

	public EpHubSupplierProductConsumerRuntimeException() {
		super();
	}

	public EpHubSupplierProductConsumerRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EpHubSupplierProductConsumerRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EpHubSupplierProductConsumerRuntimeException(String message) {
		super(message);
	}

	public EpHubSupplierProductConsumerRuntimeException(Throwable cause) {
		super(cause);
	}

}
