package com.shangpin.ephub.client.data.mysql.enumeration;

/**
 * Created by lizhongren on 2017/1/15.
 * 通用的处理状态1
 */
public enum CommonHandleState {

    /**
     * 未处理
     */
    UNHANDLED(0,"未处理"),

    /**
     * 处理已完成
     */
    HANDLED(1,"处理已完成");

    /**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    CommonHandleState(Integer index,String description){
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
