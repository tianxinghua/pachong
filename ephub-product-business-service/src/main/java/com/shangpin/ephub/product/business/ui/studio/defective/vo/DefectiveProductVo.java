package com.shangpin.ephub.product.business.ui.studio.defective.vo;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: DefectiveProductVo</p>
 * <p>Description: 残次品记录 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月13日 下午4:07:14
 *
 */
@Getter
@Setter
public class DefectiveProductVo {

	/**
	 * 逐主键
	 */
	private Long studioSlotDefectiveSpuId;
	/**
	 * 品牌
	 */
	private String brand;
	/**
	 * 产品名称
	 */
	private String itemName;
	/**
	 * 产品的货号
	 */
	private String itemCode;
	/**
	 * 扫码的码
	 */
	private String studioCode;
}
