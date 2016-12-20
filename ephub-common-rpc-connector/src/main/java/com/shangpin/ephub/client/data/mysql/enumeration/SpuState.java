package com.shangpin.ephub.client.data.mysql.enumeration;

/**
 * <p>Title:SpuState </p>
 * <p>Description: spu状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:33:24
 *
 */
public enum SpuState {
	
	/**
	 * 信息待完善
	 */
	infoPeccable(0,"infoPeccable"),
	
	/**
	 * 信息已完善
	 */
	infoImpeccable(1,"infoImpeccable");

	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;
    
    SpuState(Integer index,String description){
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
