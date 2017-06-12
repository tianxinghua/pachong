package com.shangpin.ephub.client.data.mysql.enumeration;

/**


 hub_slot_spu表中 商品状态
 *
 */
public enum SlotSpuState {

	/**
	 * 待发货
	 */
	WAIT_SEND(0,"WAIT_SEND"),


	/**
	 * 加入发货单
	 */
	ADD_INVOICE(1,"ADD_INVOICE"),

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

    SlotSpuState(Integer index, String description){
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
