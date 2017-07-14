package com.shangpin.ephub.client.data.mysql.enumeration;

public enum InfoState {

	/**
	 * 信息已完善
	 */
	PERFECT((byte)1,"信息已完善"),
	/**
	 * 信息未完善
	 */
	IMPERFECT((byte)0,"信息未完善"),
	RefreshCategory((byte)4,"刷新品类"),
	Union((byte)5,"自动选品"),
	RefreshSize((byte)6,"刷新尺码"),
	RefreshGender((byte)7,"刷新性别"),
	RefreshSpuModel((byte)8,"刷新货号"),
	RefreshBrand((byte)9,"刷新品牌"),
	RefreshColor((byte)11,"刷新颜色"),
	RefreshSeason((byte)12,"刷新季节"),
	RefreshOrigin((byte)10,"刷新产地");
	
	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    InfoState(byte index,String description){
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
