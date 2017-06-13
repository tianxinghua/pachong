package com.shangpin.ephub.client.data.mysql.enumeration;

/**


 spupending表中 商品拍摄状态
 *
 */
public enum SpuPendingStudioState {

	/**
	 * 等待处理
	 */
	WAIT_HANDLED(0,"WAIT_HANDLED"),


	/**
	 * 已处理
	 */
	HANDLED(1,"HANDLED"),

	/**
	 * 已寄出
	 */

	SEND(2,"SEND");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    SpuPendingStudioState(Integer index, String description){
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
