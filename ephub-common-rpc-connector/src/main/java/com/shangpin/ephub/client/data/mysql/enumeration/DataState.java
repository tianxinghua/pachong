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
	deleted(0,"deleted"),
	/**
	 * 未删除
	 */
	notDeleted(1,"notDeleted");
	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;
    
    DataState(Integer index,String description){
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
