package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:PicState </p>
 * <p>Description: 图片状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:34:37
 *
 */
public enum PicState {
	
	/**
	 * 无图片
	 */
	NO_PIC((byte)0,"noPic"),
	/**
	 * 图片信息已完成
	 */
	PIC_INFO_COMPLETED((byte)1,"picInfoCompleted"),
	/**
	 * 图片信息未完成
	 */
	PIC_INFO_NOT_COMPLETED((byte)2,"picInfoNotCompleted");

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    PicState(byte index,String description){
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
