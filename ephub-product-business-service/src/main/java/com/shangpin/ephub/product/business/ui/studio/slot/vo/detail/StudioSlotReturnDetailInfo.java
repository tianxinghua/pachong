package com.shangpin.ephub.product.business.ui.studio.slot.vo.detail;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class StudioSlotReturnDetailInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Status;
	private String Brand;
	private String ItemName;
	private String ItemCode;
	private String BarCode;
	private String SlotNo;
	private String MasterId;
	private String isSlot;
}