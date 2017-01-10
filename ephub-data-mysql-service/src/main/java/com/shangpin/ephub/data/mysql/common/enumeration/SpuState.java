package com.shangpin.ephub.data.mysql.common.enumeration;

/**
 * <p>Title:SpuState </p>
 * <p>Description: spu状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:33:24
 *
 */
public enum SpuState {
	
	/**
	 * 信息待完善
	 */
	INFO_PECCABLE((byte)0,"infoPeccable"),
	
	/**
	 * 信息已完善
	 */
	INFO_IMPECCABLE((byte)1,"infoImpeccable"),
	/**
	 * 已处理
	 */
	HANDLED((byte)2,"HANDLED"),
	/**
	 * 无法处理
	 */
	UNABLE_TO_PROCESS((byte)3,"无法处理"),
	/**
	 * 过滤不处理
	 */
	FILTER((byte)4,"过滤不处理"),
	/**
	 * 审核中
	 */
	HANDLING((byte)5,"审核中");
	
	
	
	
	/***
	 *  SPU_WAIT_HANDLE(0,"信息待完善"),
    SPU_WAIT_AUDIT(1,"待复核"),
    SPU_HANDLED(2,"已处理"),
    SPU_NO_WAY_HANDLE(3,"无法处理"),
    SPU_FILTER(4,"过滤不处理"),
    SPU_HANDLING(5,"审核中");
	 */




	/**
     * 数字索引标识
     */
    private byte index;
    /**
     * 描述信息
     */
    private String description;
    
    SpuState(byte index, String description){
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
