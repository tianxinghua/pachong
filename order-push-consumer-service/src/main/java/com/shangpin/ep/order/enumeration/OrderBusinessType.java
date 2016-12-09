package com.shangpin.ep.order.enumeration;

/**
 * Created by lizhongren on 2016/11/21.
 * 订单业务类型
 */
public enum OrderBusinessType {
    SYNC_TYPE_CREATE_ORDER(0,"CreateOrder"),
    SYNC_TYPE_CANCEL_ORDER(1,"CancelOrder"),
    SYNC_TYPE_PAYED_ORDER(2,"PayedOrder"),
    SYNC_TYPE_REFUND_ORDER(3,"RefundOrder"),
    SYNC_TYPE_REPURCHASE_ORDER(4,"RePurchaseOrder"),
    SYNC_TYPE_SHIPPED(5,"Shipped")
    ;
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


    OrderBusinessType(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
