package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:SkuState </p>
 * <p>Description: sku状态 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月29日 下午8:36:45
 *
 */
public enum SkuState {

	/**
	 * 信息待完善
	 */
	INFO_PECCABLE((byte)0,"infoPeccable"),
	
	/**
	 * 信息已完善
	 */
	INFO_IMPECCABLE((byte)1,"infoImpeccable"),
	/**
	 * 无法处理
	 */
	UNABLE_TO_PROCESS((byte)2,"unableToProcess");

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    SkuState(byte index,String description){
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
