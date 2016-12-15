package com.shangpin.supplier.product.consumer.enumeration;

public enum ProductStatus {

	NEW (0,"NEW"),
	UPDATE (1,"UPDATE"),
	MODIFY_PRICE(2,"MODIFY_PRICE"),
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
