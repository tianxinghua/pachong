package com.shangpin.ep.order.enumeration;

/**
 * Created by lizhongren on 2016/11/22.
 * 对接供货商API的级别
 */
public enum SupplierAPIType {
    /**
     * 只有库存接口
     */
    STOCK_API(1,"STOCK_API"),
    /**
     * 订单对接 包含锁库接口
     */
    ORDER_LOCK_API(2,"ORDER_LOCK_API"),
    /**
     * 订单对接 包含非锁库接口
     */
    ORDER_NO_LOCK_API(3,"ORDER_NO_LOCK_API");

    /**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;



    SupplierAPIType(Integer index, String description) {
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
