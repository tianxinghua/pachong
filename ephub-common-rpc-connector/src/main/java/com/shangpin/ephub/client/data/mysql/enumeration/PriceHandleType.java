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
	 * 价格
	 */
	PRICE((byte)1,"价格"),
	/**
	 * 季节
	 */
	SEASON((byte)2,"季节"),
	/**
	 * 价格和季节
	 */
	PRICEANDSEASON((byte)3,"季节和价格");

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
