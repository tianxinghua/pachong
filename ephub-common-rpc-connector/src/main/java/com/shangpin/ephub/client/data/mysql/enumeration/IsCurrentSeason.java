package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:IsCurrentSeason </p>
 * <p>Description: 是否当前季</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:34:52
 *
 */
public enum IsCurrentSeason {
	/**
	 * 非当前季
	 */
	notCurrentSeason(0,"notCurrentSeason"),

	/**
	 * 当前季
	 */
	currentSeason(1,"currentSeason");
	
	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;
    
    IsCurrentSeason(Integer index,String description){
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
