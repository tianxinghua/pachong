package com.shangpin.ephub.client.data.studio.enumeration;

/**


 STUDIO_SLOT表中 批次状态
 *
 */
public enum StudioSlotState {

	/**
	 * 等待申请
	 */
	WAIT_APPLY(0,"WAIT_APPLY"),


	/**
	 * 已申请
	 */
	APPLYED(1,"APPLYED"),

    /**
     * 供货商发货
     */
    SEND(2,"SEND"),
    /**
     * 摄影棚收货
     */
    RECEIVED(3,"RECEIVED"),

    /**
     * 摄影棚返货
     */
    STUDIO_RETURN(4,"STUDIO_RETURN");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    StudioSlotState(Integer index, String description){
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
