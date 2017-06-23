package com.shangpin.ephub.product.business.ui.studio.openbox.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudioSlotVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9218649176193099122L;
	/**
	 * 批次编号
	 */
	private String slotNo;
	/**
	 * 数量
	 */
	private int qty;
	/**
	 * 时间
	 */
	private Date operateDate;
	/**
	 * 运输单号
	 */
	private String trackingNo;

}
