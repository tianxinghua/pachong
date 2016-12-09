package com.shangpin.ep.order.enumeration;

/**
 * Created by lizhongren on 2016/11/24.
 * 流程操作步骤
 */
public enum HandleStep {
    HANDLE_ORDER(0,"HANDLE_ORDER"),
    HANDLE_LOCK_PUSH(1,"HANDLE_LOCK_PUSH"),
    HANDLE_CONFIRM_PUSH(2,"HANDLE_CONFIRM_PUSH"),
    //兼容老程序
    HANDLE_PUSH(3,"HANDLE_PUSH"),
    HANDLE_PUSH_STATUS(4,"HANDLE_PUSH_STATUS"),
    HANDLE_PURCHASE_EXCEPTION(5,"HANDLE_PURCHASE_EXCEPTION")

    ; //采购异常状态

    /**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;



    HandleStep(Integer index, String description) {
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
