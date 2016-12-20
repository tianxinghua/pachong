package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:SpuModelState </p>
 * <p>Description: 货号状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:34:05
 *
 */
public enum SpuModelState {
	/**
	 * 验证不通过
	 */
	VERIFY_FAILED((byte)0,"verifyFailed"),
	/**
	 * 验证已通过
	 */
	VERIFY_PASSED((byte)1,"verifyPassed");
	

	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    SpuModelState(byte index,String description){
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
