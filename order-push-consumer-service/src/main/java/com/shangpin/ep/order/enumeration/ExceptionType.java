package com.shangpin.ep.order.enumeration;

/**
 * Created by lizhongren on 2016/11/23.
 * 异常类型
 */
public enum ExceptionType {

    DATABASE_HANDLE_EXCEPTION(0,"DATABASE_HANDLE_EXCEPTION"),
    API_PUSH_EXCEPTION(1,"API_PUSH_EXCEPTION"),
    DATA_EXCEPTION(2,"DATA_EXCEPTION");

    /**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;



    ExceptionType(Integer index, String description) {
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
