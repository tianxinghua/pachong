package com.shangpin.ephub.client.data.mysql.enumeration;

public enum StockState {

	/**
	 * 没有SKU
	 */
	NOSKU((byte)0,"没有SKU"),

	/**
	 * 有SKU并且SKU的库存总数大于0
	 */
	HANDLED((byte)1,"有SKU并且SKU的库存总数大于0"),
	/**
	 * 有SKU但是SKU库存数都为0
	 */
	NOSTOCK((byte)2,"有SKU但是SKU库存数都为0");

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    StockState(byte index,String description){
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
