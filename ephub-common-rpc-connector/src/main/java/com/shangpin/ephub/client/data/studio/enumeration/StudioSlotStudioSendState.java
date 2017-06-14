package com.shangpin.ephub.client.data.studio.enumeration;

/**


 STUDIO_SLOT_RETURN_DETAIL  发货状态 状态
 *
 */
public enum StudioSlotStudioSendState {


    /**
     * 未发货
     */
       WAIT_SEND(0,"WAIT_SEND"),

    /**
     * 发货
     */
    SEND(1,"SEND");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    StudioSlotStudioSendState(Integer index, String description){
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
