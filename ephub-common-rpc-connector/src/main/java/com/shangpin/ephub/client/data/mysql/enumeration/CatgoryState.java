package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:CatgoryState </p>
 * <p>Description: 品类状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:35:11
 *
 */
public enum CatgoryState {

	/**
	 * 完全不匹配
	 */
	entirelyMismatching(0,"entirelyMismatching"),
	/**
	 * 匹配到4级
	 */
	perfectMatched(1,"perfectMatched"),
	/**
	 * 不能匹配到末级
	 */
	mismatching(2,"mismatching");
	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;
    
    CatgoryState(Integer index,String description){
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
