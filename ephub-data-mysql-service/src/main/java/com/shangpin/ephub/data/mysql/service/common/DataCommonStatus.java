package com.shangpin.ephub.data.mysql.service.common;

/**
 * Created by loyalty on 16/12/21.
 */
public enum DataCommonStatus {

    WAIT_HANDLE(0,"信息待完善"),

    HANDLED(1,"信息已完善")
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
    private DataCommonStatus(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
