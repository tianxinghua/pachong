package com.shangpin.ephub.fdfs.client.service.exception;
/**
 * <p>Title:EphubFdfsClientException.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:23:05
 */
public class EphubFdfsClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7522454778537440986L;

	public EphubFdfsClientException() {
		super();
	}

	public EphubFdfsClientException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EphubFdfsClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public EphubFdfsClientException(String message) {
		super(message);
	}

	public EphubFdfsClientException(Throwable cause) {
		super(cause);
	}
}
