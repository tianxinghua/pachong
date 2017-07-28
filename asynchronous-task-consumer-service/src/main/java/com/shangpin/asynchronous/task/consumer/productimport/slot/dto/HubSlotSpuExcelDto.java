package com.shangpin.asynchronous.task.consumer.productimport.slot.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: HubSlotSpuExcelDto</p>
 * <p>Description: 待拍照导入模板 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月29日 下午3:49:05
 *
 */
@Getter
@Setter
public class HubSlotSpuExcelDto {
	/**
	 * 供应商原始表的主键
	 */
	private String supplierSpuId;
	/**
	 * 
	 */
	private String spuPendingId;
	/**
	 * 门户
	 */
	private String supplierId;
	/**
	 * 编号
	 */
	private String supplierNo;
	/**
	 * 名称
	 */
	private String supplierName;
	/**
	 * 供应商品类
	 */
	private String supplierCategoryname;
	/**
	 * 对应的尚品品类
	 */
	private String hubCategoryName;
	/**
	 * 对应的尚品品类编号
	 */
	private String hubCategoryNo;
	/**
	 * 品牌编号
	 */
	private String hubBrandNo;
	/**
	 * 品牌名称
	 */
	private String hubBrandName;
	/**
	 * 货号
	 */
	private String spuModel;

	
}
