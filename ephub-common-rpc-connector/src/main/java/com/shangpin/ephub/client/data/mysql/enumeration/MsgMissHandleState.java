package com.shangpin.ephub.client.data.mysql.enumeration;

public enum MsgMissHandleState {
	
	NOT_HANDLE((byte)0, "未处理"),
	
	HAVE_HANDLED((byte)1, "已处理");

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    MsgMissHandleState(byte index,String description){
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
