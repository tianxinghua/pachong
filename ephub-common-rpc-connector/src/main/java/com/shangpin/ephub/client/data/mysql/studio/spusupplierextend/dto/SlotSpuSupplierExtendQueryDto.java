package com.shangpin.ephub.client.data.mysql.studio.spusupplierextend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>Title: PriceQueryDto</p>
 * <p>Description: 价格查询参数 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月31日 下午3:12:39
 *
 */
@Getter
@Setter
public class SlotSpuSupplierExtendQueryDto {

	private Integer pageIndex = 1;
	private Integer pageSize = 10;

	protected Integer startRow;

	/**
	 * 供应商Id
	 */
	private String supplierId;
	/**
	 * 供应商编号
	 */
	private String supplierNo;
	/**
	 * 货号
	 */
	private String spuModel;

	private String brandName;

	private String categoryName;

	private String slotSpuNo;
	/**
	 * 状态查询
	 */
	private Byte state;

	/**
	 * 供货商商品编号
	 */
	private String supplierSpuId;

	/**
	 * 供货商商品编号
	 */
	private String supplierSpuNo;

	/**
	 * 尚品季节名称
	 */
	private String seasonName;

	private Date startTime;

	private Date endTime;

	private String supplierSpuName;

	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
		this.startRow = (pageIndex-1)*this.pageSize;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		this.startRow = (pageIndex-1)*this.pageSize;
	}


}
