package com.shangpin.ephub.product.business.common.enumeration;

/**
 * Created by lizhongren on 2016/12/30.
 */
public enum ScmGenderType {
    WOMAN(0,"女士"),
    MAN(1,"男士"),
    LADY_BOY(2,"中性"),
    UNKNOWN(3,"未知");
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
    private ScmGenderType(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
    // 获取枚举实例
    public static ScmGenderType getGenderType(int index) {
        for (ScmGenderType c : ScmGenderType.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
    }
}
