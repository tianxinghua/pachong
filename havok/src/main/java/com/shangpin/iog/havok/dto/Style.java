package com.shangpin.iog.havok.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by huxia on 2015/10/15.
 */
public class Style {
    private String SPUID;//skuId
    private String spuName;//尺寸
    private String categoryGender;
    private String categoryID;//市场价
    private String categoryName;//销售价
    private String subcategoryID;
    private String subcategoryName;
    private String brandID;
    private String seasonID;
    private String brandName;
    private String seasonName;
    private String picUrl;
    private String material;
    private String productOrigin;

    public String getSPUID() { return SPUID; }

    public void setSPUID(String SPUID) { this.SPUID = SPUID; }

    public String getSpuName() { return spuName; }

    public void setSpuName(String spuName) { this.spuName = spuName; }

    public String getCategoryGender() { return categoryGender; }

    public void setCategoryGender(String categoryGender) { this.categoryGender = categoryGender; }

    public String getCategoryID() { return categoryID; }

    public void setCategoryID(String categoryID) { this.categoryID = categoryID; }

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getSubcategoryID() { return subcategoryID; }

    public void setSubcategoryID(String subcategoryID) { this.subcategoryID = subcategoryID; }

    public String getSubcategoryName() { return subcategoryName; }

    public void setSubcategoryName(String subcategoryName) { this.subcategoryName = subcategoryName; }

    public String getBrandID() { return brandID; }

    public void setBrandID(String brandID) { this.brandID = brandID; }

    public String getSeasonID() { return seasonID; }

    public void setSeasonID(String seasonID) { this.seasonID = seasonID; }

    public String getBrandName() { return brandName; }

    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getSeasonName() { return seasonName; }

    public void setSeasonName(String seasonName) { this.seasonName = seasonName; }

    public String getPicUrl() { return picUrl; }

    public void setPicUrl(String picUrl) { this.picUrl = picUrl; }

    public String getMaterial() { return material; }

    public void setMaterial(String material) { this.material = material; }

    public String getProductOrigin() { return productOrigin; }

    public void setProductOrigin(String productOrigin) { this.productOrigin = productOrigin; }

}
