package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:MaterialState </p>
 * <p>Description: 材质状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月14日 下午7:38:17
 *
 */
public enum MaterialState {

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
    
    MaterialState(byte index,String description){
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
