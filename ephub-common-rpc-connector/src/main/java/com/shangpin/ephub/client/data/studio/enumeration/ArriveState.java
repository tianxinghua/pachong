package com.shangpin.ephub.client.data.studio.enumeration;

/**
 * <p>Title: ArriveState</p>
 * <p>Description: STUDIO_SLOT表中 到货状态 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月12日 下午5:22:19
 *
 */
public enum ArriveState {


    /**
     * 未到货
     */
    NOT_ARRIVED(0,"not_arrived"),

    /**
     *已到货
     */
    ARRIVED(1,"arrived");




	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    ArriveState(Integer index, String description){
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
