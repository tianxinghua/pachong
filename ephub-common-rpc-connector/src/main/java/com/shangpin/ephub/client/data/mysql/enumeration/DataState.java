package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:DataState </p>
 * <p>Description: 数据状态 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:39:29
 *
 */
public enum DataState {

	/**
	 * 已删除
	 */
	DELETED((byte)0,"deleted"),
	/**
	 * 未删除
	 */
	NOT_DELETED((byte)1,"notDeleted");
	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    DataState(byte index,String description){
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
