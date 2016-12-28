package com.shangpin.ephub.client.data.mysql.enumeration;

/**
 * <p>Title:SpuState </p>
 * <p>Description: spu状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:33:24
 *
 */
public enum SpuSelectState {
	
	/**
	 * 信息待完善
	 */
	WAIT_SELECT(0,"待选品"),
	
	/**
	 * 信息已完善
	 */
	SELECTED(1,"已选品");
	/**
     * 数字索引标识
     */
    private int index;
    /**
     * 描述信息
     */
    private String description;
    
    SpuSelectState(int index,String description){
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
