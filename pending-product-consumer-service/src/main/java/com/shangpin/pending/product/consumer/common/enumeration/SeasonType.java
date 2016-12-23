package com.shangpin.pending.product.consumer.common.enumeration;

/**
 * Created by loyalty on 16/12/23.
 */
public enum SeasonType {

    SEASON_NOT_CURRENT(0,"非当季"),
    SEASON_CURRENT(1,"当季");

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
    private SeasonType(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
