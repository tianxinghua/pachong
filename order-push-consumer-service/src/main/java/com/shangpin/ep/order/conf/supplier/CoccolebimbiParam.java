package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class CoccolebimbiParam  extends SupplierCommon implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
}
