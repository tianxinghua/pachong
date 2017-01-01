package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:SpuSeasonState </p>
 * <p>Description: 供应商选品状态 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:36:24
 *
 */
public enum SupplierSelectState {
	
	WAIT_SELECT(0,"待选品"),
	SELECTING(1,"选品中"),
	SELECTED(2,"已选品"),
	NO_SELECTED(3,"未选品");
	/**
     * 数字索引标识
     */
    private int index;
    /**
     * 描述信息
     */
    private String description;
    
    SupplierSelectState(int index,String description){
		this.index = index;
		this.description = description;
	}
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
