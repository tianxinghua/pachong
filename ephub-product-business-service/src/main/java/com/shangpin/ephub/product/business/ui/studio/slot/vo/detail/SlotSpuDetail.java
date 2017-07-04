package com.shangpin.ephub.product.business.ui.studio.slot.vo.detail;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: SlotSpuDetail</p>
 * <p>Description: 批次商品信息 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月4日 下午2:18:40
 *
 */
@Getter
@Setter
public class SlotSpuDetail {

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
