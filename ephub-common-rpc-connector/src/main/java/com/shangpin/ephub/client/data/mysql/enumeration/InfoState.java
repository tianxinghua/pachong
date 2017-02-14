package com.shangpin.ephub.client.data.mysql.enumeration;

public enum InfoState {

	/**
	 * 信息已完善
	 */
	PERFECT((byte)1,"信息已完善"),
	/**
	 * 信息未完善
	 */
	IMPERFECT((byte)0,"信息未完善");
	
	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    InfoState(byte index,String description){
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
