package com.shangpin.ephub.data.mysql.service.common;

/**
 * Created by loyalty on 16/12/21.
 * 数据的逻辑状态
 */
public enum DataStatus {

    DELETE(0,"逻辑删除"),

    NOT_DELETE(1,"未删除")
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
    private DataStatus(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
