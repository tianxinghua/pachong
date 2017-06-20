package com.shangpin.ephub.data.mysql.slot.spusupplierunion.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**

 * slotsupplier 查询对象
 */
@Getter
@Setter
public class SpuSupplierQueryDto {
	
	private Integer pageIndex = 1;
    private Integer pageSize = 10;
    
    protected Integer startRow;
    /**
     * 供应商编号
     */
    private String supplierNo;
	/**
	 * 货号
	 */
	private String spuModel;

	private String brandNo;

	private String category;
	/**
	 * 状态查询
	 */
	private String state;



    /**
     * 尚品季节名称
     */
    private String marketSeason;
    /**
     * 尚品上市时间
     */
    private String marketYear;
    /**
     * 尚品skuid
     */
    private List<String> spSkuIds;
    
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
