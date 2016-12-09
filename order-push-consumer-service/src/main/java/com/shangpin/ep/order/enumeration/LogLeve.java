package com.shangpin.ep.order.enumeration;

/**
 * Created by lizhongren on 2016/11/23.
 */
public enum LogLeve {
    TRACK(0,"TRACK"),
    DEBUG(1,"DEBUG"),
    INFO(2,"INFO"),
    ERROR(3,"ERROR");
    /**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;



    LogLeve(Integer index, String description) {
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
