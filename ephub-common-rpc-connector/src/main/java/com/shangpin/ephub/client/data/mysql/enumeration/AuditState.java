package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * 审核状态
 * <p>Title: AuditState</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年2月22日 下午2:32:43
 *
 */
public enum AuditState {
	
	AGREE((byte)1,"同意"),
	
	DISAGREE((byte)0,"不同意");
	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    AuditState(byte index,String description){
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
