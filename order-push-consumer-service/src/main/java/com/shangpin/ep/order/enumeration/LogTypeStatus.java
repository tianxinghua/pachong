package com.shangpin.ep.order.enumeration;

/**
 * Created by lizhongren on 2016/11/18.
 * 日志类型
 */
public enum LogTypeStatus {
    LOCK_LOG(0,"锁库日志"),
    CONFIRM_LOG(1,"订单确认推送日志"),
	LOCK_CANCELLED_LOG(2,"锁库取消日志"),
	REFUNDED_LOG(3,"退款日志"),
    REPEAT_PURCAHSE_LOG(4,"重采日志"),
    REPEAT_PURCAHSE_ERROR_LOG(5,"重采错误日志"),
	SHIPPED(6,"发货日志");
    /**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;



    LogTypeStatus(Integer index, String description) {
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
