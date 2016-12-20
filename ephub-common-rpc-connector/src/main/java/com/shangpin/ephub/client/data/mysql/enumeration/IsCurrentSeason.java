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
	NOT_CURRENT_SEASON((byte)0,"notCurrentSeason"),

	/**
	 * 当前季
	 */
	CURRENT_SEASON((byte)1,"currentSeason");
	
	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    IsCurrentSeason(byte index,String description){
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
