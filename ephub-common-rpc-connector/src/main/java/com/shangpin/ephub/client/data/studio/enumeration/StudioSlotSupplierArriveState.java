package com.shangpin.ephub.client.data.studio.enumeration;

/**


 STUDIO_SLOT_RETURN_DETAIL  收货状态
 *
 */
public enum StudioSlotSupplierArriveState {

    /**
     * 未收货
     */
    NOT_RECEIVE(0,"NOT_RECEIVE"),


    /**
     * 收货
     */
    RECEIVED(1,"RECEIVED"),

    /**
     *  不接受
     */
    NOT_ACCEPTANCE(2,"NOT_ACCEPTANCE"),

    /**
     * 未收到
     */

    NOT_ARRIVE(3,"NOT_ARRIVE");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    StudioSlotSupplierArriveState(Integer index, String description){
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
