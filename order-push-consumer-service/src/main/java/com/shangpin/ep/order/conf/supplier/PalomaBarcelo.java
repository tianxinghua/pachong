package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class PalomaBarcelo extends SupplierCommon implements Serializable {
	
	private static final long serialVersionUID = -8421925054577535586L;
	private String cancelUrl;
	/**
	 * 
	 */
	private String placeUrl;

}
