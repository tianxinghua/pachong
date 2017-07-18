package com.shangpin.ephub.product.business.ui.studio.slot.vo.detail;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class StudioSlotsHistories implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String SlotNo;
	private String Status;
	private String Sender;
	private String SendingDate	;
	private String QA;
	private String Return;
	private String ReturnConFirmDate;
}