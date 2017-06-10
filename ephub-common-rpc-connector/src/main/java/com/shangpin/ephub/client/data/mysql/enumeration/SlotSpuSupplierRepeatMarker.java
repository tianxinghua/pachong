package com.shangpin.ephub.client.data.mysql.enumeration;

/**


 hub_slot_spu_supplier表中 多家供货标记
 *
 */
public enum SlotSpuSupplierRepeatMarker {

	/**
	 * 独家
	 */
    SINGLE(0,"SINGLE"),


	/**
	 * 多家
	 */
	MULTI(1,"MULTI");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    SlotSpuSupplierRepeatMarker(Integer index, String description){
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
