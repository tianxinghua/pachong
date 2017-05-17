package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title: Type</p>
 * <p>Description: hub_supplier_price_change_record.type </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月31日 上午11:23:48
 *
 */
public enum PriceHandleType {
	
	/**
	 * 初始化状态
	 */
	NEW_DEFAULT((byte)0,"new_default"),
	/**
	 * 市场价发生变化
	 */
	MARKET_PRICE_CHANGED((byte) 1, "market_price_changed"),
	/**
	 * 供价发生了变化
	 */
	SUPPLY_PRICE_CHANGED((byte) 2, "supply_price_changed"),
	/**
	 * 季节发生了变化
	 */
	SEASON_CHANGED((byte)3,"season_changed"),
	/**
	 * 市场价、供价发生了变化
	 */
	MARKET_SUPPLY_CHANGED((byte)12,"market_supply_changed"),
	/**
	 * 市场价、季节发生了变化
	 */
	MARKET_SEASON_CHANGED((byte)13,"market_season_changed"),
	/**
	 * 供价、季节发生了变化
	 */
	SUPPLY_SEASON_CHANGED((byte)23,"supply_season_changed"),
	/**
	 * 市场价、供价、季节都发生了变化
	 */
	MARKET_SUPPLY_SEASON_CHANGED((byte)123,"market_supply_season_changed");

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    PriceHandleType(byte index, String description){
		this.index = index;
		this.description = description;
	}
    public byte getIndex() {
        return index;
    }
    public void setIndex(byte index) {
        this.index = index;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
