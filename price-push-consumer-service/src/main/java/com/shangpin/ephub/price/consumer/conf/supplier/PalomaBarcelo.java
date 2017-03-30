package com.shangpin.ephub.price.consumer.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

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
