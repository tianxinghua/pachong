package com.shangpin.ephub.client.data.studio.enumeration;

/**


  发货状态 状态
 *
 */
public enum StudioSlotSendState {



    /**
     * 待发货
     */
    WAIT_SEND(0,"WAIT_SEND"),
    /**
     * 待发货
     */
    ISPRINT(1,"ISPRINT"),
    /**
     * 发货
     */
    SEND(2,"SEND");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    StudioSlotSendState(Integer index, String description){
		this.index = index;
		this.description = description;
	}
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
