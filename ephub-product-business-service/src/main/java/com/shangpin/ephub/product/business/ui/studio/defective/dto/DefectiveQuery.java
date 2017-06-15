package com.shangpin.ephub.product.business.ui.studio.defective.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: DefectiveQuery</p>
 * <p>Description: 残次品页面参数 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月15日 下午6:17:28
 *
 */
@Getter
@Setter
public class DefectiveQuery {
	
	/**
	 * 摄影棚编号
	 */
	private String studioNo;
	/**
	 * 分页
	 */
	private Integer pageIndex;
	/**
	 * 分页
	 */
    private Integer pageSize; 

}
