package com.shangpin.ephub.data.mysql.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 * spu pending
 */

@Getter
@Setter
public class SpuPendingCommonDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3893575972721048736L;

	private Long spuPendingId;//spuPending表主键

    private String spuModel;//货号

    private String color;

    private String brandNo;

    private String categoryNo;

    private String supplierNo;

    private String supplierId;

    private String material;//材质

    private String origin;//产地

    private String updateTime ;
}
