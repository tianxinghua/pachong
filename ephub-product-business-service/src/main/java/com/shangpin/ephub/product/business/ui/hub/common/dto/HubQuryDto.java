package com.shangpin.ephub.product.business.ui.hub.common.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title:HubQuryDto </p>
 * <p>Description: hub页面查询参数</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月21日 下午5:14:16
 *
 */
@Setter
@Getter
public class HubQuryDto {

	private Integer pageIndex;
    private Integer pageSize;
    /**
     * 货号
     */
	private String spuModel;
	/**
	 * 品牌编号
	 */
	private String brandNo;
	/**
	 * 品类编号
	 */
	private String categoryNo;
	/**
	 * 更新开始时间
	 */
	private String startUpdateTime;
	/**
	 * 更新结束时间
	 */
	private String endUpdateTime;
	/**
	 * 生成状态
	 */
	private byte spuState;
}
