package com.shangpin.ephub.client.data.studio.enumeration;

/**


 Milestone
 *
 */
public enum StudioSlotMilestone {

	/**
	 * 供应商 寄出日期
	 */
	SEND_DATE(0,"SEND_DATE"),


	/**
	 * 预计到达日期
	 */
	ETA(1,"ETA"),

    /**
     * 收货日期
     */
	RECEIVE_DATE(2,"RECEIVE_DATE"),
    /**
     * 拍摄日期
     */
	OPERATE_DATE(3,"OPERATE_DATE"),
    /**
     * 寄回日期
     */
	RETURN_DATE(4,"RETURN_DATE"),
    /**
     * 供应商收到寄货日期
     */
	RETURN_CONFIRM_DATE(5,"RETURN_CONFIRM_DATE");

	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    StudioSlotMilestone(Integer index, String description){
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
