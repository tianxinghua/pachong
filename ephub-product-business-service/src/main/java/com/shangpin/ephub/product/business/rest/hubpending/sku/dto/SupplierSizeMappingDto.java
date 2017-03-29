package com.shangpin.ephub.product.business.rest.hubpending.sku.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by loyalty on 17/1/7.
 */
@Getter
@Setter
@ToString
public class SupplierSizeMappingDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5598257123317149125L;

	private String supplierSize;

    private String spSize;

    private String  spScreenSizeId;
}
