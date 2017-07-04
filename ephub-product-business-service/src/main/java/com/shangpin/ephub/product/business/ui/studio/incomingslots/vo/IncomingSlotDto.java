package com.shangpin.ephub.product.business.ui.studio.incomingslots.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomingSlotDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2662892646294293845L;
	/**
	 * id
	 */
	private Long studioSlotId;
	
	/**
	 * 批次编号
	 */
	private String slotNo;
	/**
	 * 数量
	 */
	private int qty;
	/**
	 * 发件人
	 */
	private String sender;
	/**
	 * 发送时间
	 */
	private Date sendingDate;
	/**
	 * 计划到达时间
	 */
	private Date ETA; 
	/**
	 * 运输单号
	 */
	private String trackingNo;

	
	
}
