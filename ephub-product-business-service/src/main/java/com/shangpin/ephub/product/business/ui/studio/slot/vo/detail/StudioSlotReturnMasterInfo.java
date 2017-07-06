package com.shangpin.ephub.product.business.ui.studio.slot.vo.detail;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class StudioSlotReturnMasterInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String SlotNo;
	private String Qty;
	private String ActualQty;
	private String MissingQty;
	private String DamagedQty;
	private String AddedQty;
	private String Destination;
	private String MasterId;
}