package com.shangpin.ep.order.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**

 */
@Setter
@Getter
@ToString
public class Redi extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8714769888978504331L;


	private String orderUrl = "https://www.redigroup.it/api/orders/1.9/YOUR_TOK";


}
