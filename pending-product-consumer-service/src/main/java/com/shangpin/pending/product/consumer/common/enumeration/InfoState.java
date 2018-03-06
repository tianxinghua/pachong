package com.shangpin.pending.product.consumer.common.enumeration;

/**
 * Created by lizhongren on 2018/3/5.
 */
public enum InfoState {
    PERFECT(1, "信息已完善"),
    IMPERFECT(0, "信息未完善"),
    RefreshCategory(4, "刷新品类"),
    Union(5, "自动选品"),
    RefreshSize(6, "刷新尺码"),
    RefreshGender(7, "刷新性别"),
    RefreshSpuModel(8, "刷新货号"),
    RefreshBrand(9, "刷新品牌"),
    RefreshOrigin(10, "刷新产地"),
    RefreshColor(11, "刷新颜色"),
    RefreshSeason(12, "刷新季节"),
    RefreshMaterial(13, "刷新材质");

    private Integer  index;
    private String description;

    private InfoState(Integer index, String description) {
        this.index = index;
        this.description = description;
    }

    public Integer getIndex() {
        return this.index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
