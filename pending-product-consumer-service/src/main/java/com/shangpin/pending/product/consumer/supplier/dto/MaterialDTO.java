package com.shangpin.pending.product.consumer.supplier.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 16/12/16.
 */
@Setter
@Getter
public class MaterialDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7251517498181329087L;

	private Long materialItemId;

    private Long  materialDicId;

    private String supplierMaterial;

    private String hubMaterial;
}
