package com.shangpin.supplier.product.consumer.exception;
/**
 * <p>Title:EPHubException.java </p>
 * <p>Description: 整个系统中使用的编译时异常类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月6日 下午7:44:06
 */
public class EpHubSupplierProductConsumerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2800875797601132706L;

	public EpHubSupplierProductConsumerException() {
		super();
	}

	public EpHubSupplierProductConsumerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EpHubSupplierProductConsumerException(String message, Throwable cause) {
		super(message, cause);
	}

	public EpHubSupplierProductConsumerException(String message) {
		super(message);
	}

	public EpHubSupplierProductConsumerException(Throwable cause) {
		super(cause);
	}

}
