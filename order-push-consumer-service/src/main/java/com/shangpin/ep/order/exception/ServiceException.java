package com.shangpin.ep.order.exception;

/**
 * Base exception of all services API exceptions.
 */
public class ServiceException extends Exception {

	public ServiceException(){
		
	}
	
	public ServiceException(String message){
		super(message);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1390241246851283680L;

}
