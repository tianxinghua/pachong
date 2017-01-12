package com.shangpin.ephub.data.mysql.common.enumeration;
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
	NO_PIC(Byte.valueOf("0"),"NO_PIC"),
    /**
     * 处理异常
     */
    HANDLE_ERROR(Byte.valueOf("1"),"HANDLE_ERROR"),
    /**
     * 处理完成
     */
    HANDLED(Byte.valueOf("2"),"HANDLED");

	/**
     * 数字索引标识
     */
    private Byte index;
    /**
     * 描述信息
     */
    private String description;
    
    PicState(Byte index, String description){
		this.index = index;
		this.description = description;
	}
    public Byte getIndex() {
        return index;
    }
    public void setIndex(Byte index) {
        this.index = index;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
