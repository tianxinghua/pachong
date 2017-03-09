package com.shangpin.ephub.client.data.mysql.enumeration;

public enum HandleFromState {

	/**
	 * 信息已完善
	 */
	AUTOMATIC_HANDLE((byte)0,"程序自动处理"),
	/**
	 * 信息未完善
	 */
	HAND_HANDLE((byte)1,"手工处理");
	
	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    HandleFromState(byte index,String description){
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
