package com.shangpin.ephub.product.business.ui.studio.slot.vo.detail;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: SlotInfo</p>
 * <p>Description: 批次基本信息 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月4日 下午2:17:38
 *
 */
@Getter
@Setter
public class SlotInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 503111900873716060L;
	/**
	 * 批次号
	 */
	private String slotNo;
	/**
	 * 供应商名称
	 */
	private String supplierName;
	/**
	 * 摄影棚
	 */
	private String studioName;
	/**
	 * 批次状态
	 */
	private String slotStatus;
	/**
	 * 发货数量
	 */
	private int sendQty;
	/**
	 * 收获数量
	 */
	private int arriveQty;
	/**
	 * 摄影棚返货数量
	 */
	private int returnQty;
	/**
	 * 供应商收货数量
	 */
	private int actualQuantity;
	/**
	 * 批次最晚到货时间
	 */
	private Date slotDate;
	/**
	 * 批次到货时间
	 */
	private Date arriveTime;
	/**
	 * 批次预计拍摄时间
	 */
	private Date planShootTime;
	/**
	 * 拍摄时间
	 */
	private Date shootTime;
	
}
