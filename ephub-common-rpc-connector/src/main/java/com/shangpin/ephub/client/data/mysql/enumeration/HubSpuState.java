package com.shangpin.ephub.client.data.mysql.enumeration;

/**
 * <p>Title:HubSpuState </p>
 * <p>Description: Hub 商品状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月23日 上午11:14:15
 *
 */
public enum HubSpuState {
	
	/**
	 * 在销售
	 */
	ON_SALE((byte)0,"在销售");

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    HubSpuState(byte index,String description){
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
