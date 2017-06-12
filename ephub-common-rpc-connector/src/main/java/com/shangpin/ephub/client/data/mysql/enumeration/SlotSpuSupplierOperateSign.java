package com.shangpin.ephub.client.data.mysql.enumeration;

/**


 hub_slot_spu_supplier表中 供货商操作标记
 *
 */
public enum SlotSpuSupplierOperateSign {

	/**
	 * 自家寄出
	 */
    OWNER_SEND(0,"OWNER_SEND"),


	/**
	 * 其它家寄出
	 */
	OTHER_SEND(1,"OTHER_SEND"),


    /**
     * 取消寄送
     */
    CANCEL_SEND(2,"CANCEL_SEND");







	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    SlotSpuSupplierOperateSign(Integer index, String description){
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
