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
	noPic(0,"noPic"),
	/**
	 * 图片信息已完成
	 */
	picInfoIsGood(1,"picInfoIsGood"),
	/**
	 * 图片信息未完成
	 */
	picinfoNotGood(2,"picinfoNotGood");

	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;
    
    PicState(Integer index,String description){
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
