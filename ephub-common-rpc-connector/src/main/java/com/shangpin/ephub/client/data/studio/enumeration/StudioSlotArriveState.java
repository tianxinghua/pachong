package com.shangpin.ephub.client.data.studio.enumeration;

/**


 STUDIO_SLOT表中 批次状态
 *
 */
public enum StudioSlotArriveState {


    /**
     * 收货
     */
    RECEIVED(0,"RECEIVED"),

    /**
     *
     */
    DELAY(1,"DELAY");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    StudioSlotArriveState(Integer index, String description){
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
