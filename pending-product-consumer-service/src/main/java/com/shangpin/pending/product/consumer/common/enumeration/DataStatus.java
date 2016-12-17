package com.shangpin.pending.product.consumer.common.enumeration;

/**
 * Created by loyalty on 16/12/16.
 * 数据库中各个表中的数据状态
 *
 */
public enum DataStatus {
    DATA_STATUS_DELETE(0,"已删除"),
    DATA_STATUS_NORMAL(1,"未删除");

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
    private DataStatus(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
