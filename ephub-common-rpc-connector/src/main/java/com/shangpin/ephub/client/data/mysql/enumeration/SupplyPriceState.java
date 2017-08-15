package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title: SupplyPriceState</p>
 * <p>Description: 供价状态 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年8月15日 下午2:58:51
 *
 */
public enum SupplyPriceState {

	NO_SUPPLYPRICE((byte)0,"无供价"),
	
	HAVE_SUPPLYPRICE((byte)1,"有供价");

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    SupplyPriceState(byte index,String description){
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
