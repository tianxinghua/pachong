package com.shangpin.ephub.client.data.mysql.enumeration;

public enum TaskImportTpye {

	/**
	 * 图片状态
	 */
	NO_TYPE(0,"NO_TYPE"),
	/**
	 * 货号状态
	 */
	PENDING_SPU(1,"PENDING_SPU"),
	/**
	 * 品类状态
	 */
	PENDING_SKU(2,"PENDING_SKU"),
	/**
	 * 品牌状态
	 */
	HUB_PRODUCT(3,"HUB_PRODUCT");
	
	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;
    
    TaskImportTpye(Integer index,String description){
		this.index = index;
		this.description = description;
	}
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
