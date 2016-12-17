package com.shangpin.pending.product.consumer.common.enumeration;

/**
 * Created by loyalty on 16/12/16.
 */
public enum DataBusinessStatus {

    NO_PUSH(0,"未发布"),
    PUSH(1,"发布");

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
    private DataBusinessStatus(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
