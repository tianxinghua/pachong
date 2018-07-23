package com.shangpin.pending.product.consumer.common.enumeration;

/**
 * 材质来源
 */
public enum MaterialSourceEnum {

    GOOGLE_TRANSLATION(0,"谷歌翻译"),
    HUMAN_TRANSLATION(1,"人工翻译");

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
    private MaterialSourceEnum(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
