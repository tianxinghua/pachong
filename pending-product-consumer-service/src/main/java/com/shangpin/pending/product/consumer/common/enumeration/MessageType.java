package com.shangpin.pending.product.consumer.common.enumeration;

/**
 * Created by loyalty on 16/12/13.
 * 传入pending 消息投中各个信息的状态
 */
public enum MessageType {

    NEW(0,"NEW"),
    UPDATE(1,"UPDATE"),
    MODIFY_PRICE(2,"MODIFY_PRICE"),
    NO_NEED_HANDLE(3,"NO_NEED_HANDLE");


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
    private MessageType(Integer index, String description) {
        this.index = index;
        this.description = description;
    }

    // 获取枚举实例
    public static MessageType getOrderStatus(int index) {
        for (MessageType c : MessageType.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
    }
}
