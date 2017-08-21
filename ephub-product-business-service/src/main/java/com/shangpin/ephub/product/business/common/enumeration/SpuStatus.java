package com.shangpin.ephub.product.business.common.enumeration;

/**
 * Created by loyalty on 16/12/21.
 */
public enum SpuStatus {

    SPU_WAIT_HANDLE(0,"信息待完善"),
    SPU_WAIT_AUDIT(1,"待复核"),
    SPU_HANDLED(2,"已处理"),
    SPU_NO_WAY_HANDLE(3,"无法处理"),
    SPU_FILTER(4,"过滤不处理"),
    SPU_HANDLING(5,"审核中"),
    /**
     * HUB已存在
     */
    EXISTED_IN_HUB(6,"SPU部分SKU已生成"),

    /**
     * SPU下所有的SKU都在尚品已生成
     */
    ALL_EXISTED_IN_HUB(7,"SPU所有SKU均已生成");
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
    private SpuStatus(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
    // 获取枚举实例
    public static SpuStatus getOrderStatus(int index) {
        for (SpuStatus c : SpuStatus.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
    }
}
