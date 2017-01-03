package com.shangpin.ephub.fdfs.client.service.exception;
/**
 * <p>Title:EphubFdfsClientRuntimeException.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:23:58
 */
public class EphubFdfsClientRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5719121818904556422L;

	public EphubFdfsClientRuntimeException() {
		super();
	}

	public EphubFdfsClientRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EphubFdfsClientRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EphubFdfsClientRuntimeException(String message) {
		super(message);
	}

	public EphubFdfsClientRuntimeException(Throwable cause) {
		super(cause);
	}
}
