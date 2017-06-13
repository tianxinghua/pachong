package com.shangpin.ephub.client.data.studio.enumeration;

/**


 STUDIO_SLOT表中 摄影棚收货状态
 *
 */
public enum StudioSlotArriveState {

    NOT_ARRIVE(0,"NOT_ARRIVE"),

    /**
     * 摄影棚收货
     */
    RECEIVED(1,"RECEIVED"),

    /**
     *
     */
    DELAY(2,"DELAY");




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
