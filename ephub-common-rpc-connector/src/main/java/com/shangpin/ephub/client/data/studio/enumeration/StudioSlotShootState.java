package com.shangpin.ephub.client.data.studio.enumeration;

/**


 STUDIO_SLOT表中 拍摄状态
 *
 */
public enum StudioSlotShootState {

	/**
	 * 正常拍摄
	 */
	NORMAL(0,"NORMAL"),


	/**
	 * 提前拍摄
	 */
	AHEAD_TIME(1,"AHEAD_TIME"),



    /**
     * 未按计划拍摄
     */
    DELAY_SHOOT(2,"DELAY_SHOOT");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    StudioSlotShootState(Integer index, String description){
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
