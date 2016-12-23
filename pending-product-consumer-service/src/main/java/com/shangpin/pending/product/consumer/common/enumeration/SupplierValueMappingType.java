package com.shangpin.pending.product.consumer.common.enumeration;

/**
 * Created by loyalty on 16/12/22.
 */
public enum SupplierValueMappingType {

    TYPE_BRAND(1,"品牌类型"),
    TYPE_CATEGORY(2,"品类类型"),
    TYPE_ORIGIN(3,"产地类型");


    /**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;
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
    private SupplierValueMappingType(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
