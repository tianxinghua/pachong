package com.shangpin.pending.product.consumer.common.enumeration;

/**
 * Created by loyalty on 16/12/14.
 * 业务状态
 */
public enum PropertyStatus {

   // =0 信息待完善 =1 信息已完善

    MESSAGE_WAIT_HANDLE(0,"信息待完善"),
    MESSAGE_HANDLED(1,"信息已完善"),
    MESSAGE_NO_COMPLETE(2,"部分完善");
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
    private PropertyStatus(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
