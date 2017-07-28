package com.shangpin.ephub.product.business.ui.studio.slot.vo.detail;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class StudioDicSupplierInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Long studioDicSupplierId;
    private String supplierId;
    private String studioNameFirst;
    private String studioNameSecond;
    private String studioNameThree;
    private Date updateTime;
    private String updateUser;
}