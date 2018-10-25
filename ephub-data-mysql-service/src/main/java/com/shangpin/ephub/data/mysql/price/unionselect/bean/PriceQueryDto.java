package com.shangpin.ephub.data.mysql.price.unionselect.bean;

import java.util.List;
/**
 * <p>Title: PriceQueryDto</p>
 * <p>Description: 价格查询参数 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月31日 下午3:12:39
 *
 */
public class PriceQueryDto {
	
	private Integer pageIndex = 1;
    private Integer pageSize = 10;
    
    protected Integer startRow;
    /**
     * 供应商门户编号
     */
    private String supplierId;
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

	/**
	 * 供应商渠道
	 */
	private String channelName;
    
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
	public Integer getStartRow() {
		return startRow;
	}
	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getMarketSeason() {
		return marketSeason;
	}
	public void setMarketSeason(String marketSeason) {
		this.marketSeason = marketSeason;
	}
	public String getMarketYear() {
		return marketYear;
	}
	public void setMarketYear(String marketYear) {
		this.marketYear = marketYear;
	}
	public List<String> getSpSkuIds() {
		return spSkuIds;
	}
	public void setSpSkuIds(List<String> spSkuIds) {
		this.spSkuIds = spSkuIds;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
