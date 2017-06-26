package com.shangpin.ephub.client.data.mysql.enumeration;

/**


 hub_slot_spu_supplier表中 供货商操作标记
 *
 */
public enum SlotSpuSupplierOperateSign {

    /**
     * 不需要操作
     */

    NO_NEED_HANDLE(0,"NO_NEED_HANDLE"),



    /**
     * 未处理
     */
    NO_HANDLE(1,"NO_HANDLE"),



	/**
	 * 自家寄出
	 */
    OWNER_SEND(2,"OWNER_SEND"),


	/**
	 * 其它家寄出
	 */
	OTHER_SEND(3,"OTHER_SEND"),


    /**
     * 取消寄送
     */
    CANCEL_SEND(4,"CANCEL_SEND")



    ;







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
