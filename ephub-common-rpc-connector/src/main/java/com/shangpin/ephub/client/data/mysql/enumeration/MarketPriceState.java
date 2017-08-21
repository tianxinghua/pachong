package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title: MarketPriceState</p>
 * <p>Description: 市场价状态 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年8月15日 下午2:53:35
 *
 */
public enum MarketPriceState {
	
	NO_MARKETPRICE((byte)0,"无市场价"),
	
	HAVE_MARKETPRICE((byte)1,"有市场价");

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    MarketPriceState(byte index,String description){
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
