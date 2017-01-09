package com.shangpin.ephub.client.data.mysql.enumeration;

public enum TaskImportTpye {

	/**
	 * 一般任务
	 */
	NO_TYPE(0,"NO_TYPE"),
	/**
	 * pending-spu导入
	 */
	PENDING_SPU(1,"PENDING_SPU"),
	/**
	 * pending-sku导入
	 */
	PENDING_SKU(2,"PENDING_SKU"),
	/**
	 * hub导入
	 */
	HUB_PRODUCT(3,"HUB_PRODUCT"),
	
	/**
	 * pending-spu导出
	 */
	EXPORT_PENDING_SPU(4,"pending-spu导出"),
	/**
	 * pending-sku导出
	 */
	EXPORT_PENDING_SKU(5,"pending-sku导出");
	
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
