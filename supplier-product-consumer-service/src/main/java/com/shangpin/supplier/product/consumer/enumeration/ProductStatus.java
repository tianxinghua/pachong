package com.shangpin.supplier.product.consumer.enumeration;

public enum ProductStatus {
	/**
	 * 新品
	 */
	NEW (0,"NEW"),
	/**
	 * 有更新
	 */
	UPDATE (1,"UPDATE"),
	/**
	 * 价格发生变化
	 */
	MODIFY_PRICE(2,"MODIFY_PRICE"),
	/**
	 * 无变化
	 */
	NO_NEED_HANDLE(3,"NO_NEED_HANDLE");
	
	
	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;



    ProductStatus(Integer index, String description) {
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
