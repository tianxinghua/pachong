package com.shangpin.ephub.client.data.mysql.enumeration;

/**
 *
 */
public enum SourceFromEnum {

    TYPE_SUPPLIER_API(0,"供货商API"),
    TYPE_BRAND(1,"品牌方")   ;


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
    private SourceFromEnum(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
