package com.shangpin.ephub.product.business.ui.studio.incomingslots.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: IncomingSlotsQuery</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月20日 下午2:18:15
 *
 */
@Getter
@Setter
public class IncomingSlotsQuery {

	/**
	 * 摄影棚编号
	 */
	private String studioNo;

	/**
	 * 物流单号
	 */
	private String trackingNo;
	/**
	 * 预计到货时间-开始时间
	 */
	private String planArriveStartTime;
	/**
	 * 预计到货时间-结束时间
	 */
	private String planArriveEndTime;
}
