package com.shangpin.ephub.client.data.studio.enumeration;

/**


 STUDIO_SLOT_SPU_SEND_DETAIL  收货状态
 *
 */
public enum StudioSlotStudioArriveState {


    /**
     * 摄影棚收货
     */
    RECEIVED(0,"RECEIVED"),

    /**
     *  不接受
     */
    NOT_ACCEPTANCE(1,"NOT_ACCEPTANCE"),

    /**
     * 未收到
     */

    NOT_ARRIVE(2,"NOT_ARRIVE");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    StudioSlotStudioArriveState(Integer index, String description){
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
