package com.shangpin.ephub.client.data.studio.enumeration;

/**


  发货状态 状态
 *
 */
public enum StudioSlotSendState {



    /**
     * 发货
     */
    SEND(0,"SEND");




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
