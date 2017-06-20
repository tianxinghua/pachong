package com.shangpin.ephub.client.data.studio.enumeration;

/**


 STUDIO_SLOT表中 申请状态
 *
 */
public enum StudioSlotApplyState {

	/**
	 * 等待申请
	 */
	WAIT_APPLY(0,"WAIT_APPLY"),


	/**
	 * 已申请
	 */
	APPLYED(1,"APPLYED"),

	/**
     * 已过期
	 */

	EXPIRED(2,"EXPIRED"),

    /**
     * 内部占用
     */
    INTERNAL_OCCUPANCY(3,"INTERNAL_OCCUPANCY"),
	/**
     * 已补申请
     */
	HAS_APPLYED(4,"HAS_APPLYED"),
	/**
     * 已申请并生成了新的批次
     */
	HAS_APPLYED_AND_CREATE_STUDIO(5,"HAS_APPLYED_AND_CREATE_STUDIO");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    StudioSlotApplyState(Integer index, String description){
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
