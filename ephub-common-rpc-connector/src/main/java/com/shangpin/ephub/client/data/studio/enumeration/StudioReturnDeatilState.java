package com.shangpin.ephub.client.data.studio.enumeration;

/**


 studio_slot_return_detail返还物品状态
 *
 */
public enum StudioReturnDeatilState {

	/**
	 * 未扫码
	 */
	WAIT(0,"WAIT"),


	/**
	 * 商品扫码正常
	 */
	GOOD(1,"GOOD"),

	/**
     * 商品损坏
	 */

	DAMAGED(2,"DAMAGED"),

    /**
     * 商品丢失
     */
    MISS(3,"MISS");

	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    StudioReturnDeatilState(Integer index, String description){
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
