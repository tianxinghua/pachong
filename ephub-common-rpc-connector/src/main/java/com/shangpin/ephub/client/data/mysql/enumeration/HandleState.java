package com.shangpin.ephub.client.data.mysql.enumeration;

public enum HandleState {

	/**
	 * 信息已完善
	 */
	HUB_EXIST((byte)1,"hub中存在，sku需要处理"),
	/**
	 * 信息未完善
	 */
	PENDING_HANDING_EXIST((byte)2,"审核中存在，sku需要处理");
	
	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    HandleState(byte index,String description){
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
