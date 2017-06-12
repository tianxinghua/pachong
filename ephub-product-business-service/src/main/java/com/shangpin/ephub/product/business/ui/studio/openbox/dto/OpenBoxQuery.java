package com.shangpin.ephub.product.business.ui.studio.openbox.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: OpenBoxQuery</p>
 * <p>Description: OpenBox页面查询参数 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月9日 下午2:03:07
 *
 */
@Getter
@Setter
public class OpenBoxQuery {
	/**
	 * 摄影棚编号
	 */
	private String studioNo;

	/**
	 * 物流单号
	 */
	private String trackingNo;
	/**
	 * 批次名称
	 */
	private String slotName;
	/**
	 * 开始拍摄日期
	 */
	private String operateStartDate;
	/**
	 * 结束拍摄日期
	 */
	private String operateEndDate;
	/**
	 * 分页
	 */
	private Integer pageIndex;
	/**
	 * 分页
	 */
    private Integer pageSize;
}
