package com.shangpin.ephub.price.consumer.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class Tessabit extends SupplierCommon implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7604479071809461556L;
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String createOrderInterface;
	/**
	 * 
	 */
	private String setStatusInterface;
	/**
	 * 
	 */
	private String getStatusInterface;
	/**
	 * 
	 */
	private String getItemStockInterface;
	/**
	 * 
	 */
	private String user;
	/**
	 * 
	 */
	private String password;
}
