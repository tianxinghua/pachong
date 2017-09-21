package com.shangpin.ephub.product.business.ui.studio.slot.vo.detail;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class StudioSlotSendDetailInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String slotSpuNo;
	private String supplierSpuName;
	private String supplierSpuModel;
	private String supplierBrandName;
	private String barcode;
}