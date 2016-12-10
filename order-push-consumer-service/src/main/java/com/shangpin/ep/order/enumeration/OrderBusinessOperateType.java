package com.shangpin.ep.order.enumeration;

/**
 * Created by lizhongren on 2016/11/28.
 * 业务中发生的操作
 */
public enum OrderBusinessOperateType {
    OPERATE_CREATE_NEW_ORDER(0,"OPERATE_CREATE_NEW_ORDER"), //创建订单
    OPERATE_PURCHASE_EXCEPTION(1,"OPERATE_PURCHASE_EXCEPTION"); //采购异常

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


    OrderBusinessOperateType(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
