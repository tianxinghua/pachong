package com.shangpin.ephub.product.business.service.studio.slotsendreturn.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * 批次商品信息 </p>

 *
 */
@Getter
@Setter
public class SlotSpuSendAndReturn implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9170699050931185021L;
	/**
	 * 货号
	 */
	private String spuModel;
	/**
	 * 供应商原始货号
	 */
	private String supplierSpuModel;
	/**
	 * 品牌编号
	 */
	private String hubBrandNo;
	/**
	 * 品类编号
	 */
	private String hubCategoryNo;
	/**
	 * 供应商发货
	 */
	private boolean supplierSend;
	/**
	 * 摄影棚确认收货
	 */
	private boolean received;
	/**
	 * 摄影棚返货
	 */
	private boolean returned;
	/**
	 * 供应商收货
	 */
	private boolean supplierReceived;
}
