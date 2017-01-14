package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:SpSkuSizeState </p>
 * <p>Description: 尺码处理状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:37:55
 *
 */
public enum SpSkuSizeState {

	/**
	 * 未处理
	 */
	UNHANDLED((byte)0,"未处理"),

	/**
	 * 处理已完成
	 */
	HANDLED((byte)1,"处理已完成");

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    SpSkuSizeState(byte index,String description){
		this.index = index;
		this.description = description;
	}
    public byte getIndex() {
        return index;
    }
    public void setIndex(byte index) {
        this.index = index;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
