package com.shangpin.ephub.client.data.mysql.enumeration;

/**
 * Created by loyalty on 16/12/22.
 */
public enum SupplierValueMappingType {

    TYPE_BRAND(1,"品牌类型"),
    TYPE_CATEGORY(2,"品类类型"),
    TYPE_ORIGIN(3,"产地类型"),
    TYPE_SIZE(4,"尺码"),
    TYPE_SUPPLIER(5,"供货商"),
    TYPE_BRAND_SUPPLIER(6,"品牌供货商"),
    TYPE_BRAND_SUPPLIER_URL(7,"品牌供货商拉取地址")
    ;


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