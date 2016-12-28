package com.shangpin.ephub.data.mysql.service.common;

/**
 * Created by loyalty on 16/12/21.
 * 选品状态
 */
public enum DataSelectStatus {

    NOT_SELECT(0,"未选品"),

    SELECTED(1,"已选品")
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
    private DataSelectStatus(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
