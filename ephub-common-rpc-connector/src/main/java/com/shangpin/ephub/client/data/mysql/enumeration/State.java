package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title: State</p>
 * <p>Description: hub_supplier_price_change_record.state </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月31日 上午11:29:13
 *
 */
public enum State {
	/**
	 * 未处理
	 */
	UNHANDLED((byte)0,"未处理"),
	/**
	 * 消息推送完成
	 */
	PUSHED((byte)1,"消息推送完成"),
	/**
	 * 处理完成
	 */
	HANDLED((byte)2,"处理完成"),
	/**
	 * 处理失败
	 */
	HANDLE_ERROR((byte)3,"处理失败");
	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    State(byte index,String description){
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
