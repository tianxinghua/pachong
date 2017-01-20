package com.shangpin.ephub.data.mysql.product.common.enumeration;

/**
 * <p>Title:FilterFlag </p>
 * <p>Description: 过滤标的值 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月29日 下午7:24:03
 *
 */
public enum FilterFlag {

	/**
	 * 标志该条数据是无效的
	 */
	INVALID((byte)0,"invalid"),
	/**
	 * 标志该条数据是有效的
	 */
	EFFECTIVE((byte)1,"effective");
	
	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    FilterFlag(byte index, String description){
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
