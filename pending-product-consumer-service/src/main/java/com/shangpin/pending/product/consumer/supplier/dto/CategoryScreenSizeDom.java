package com.shangpin.pending.product.consumer.supplier.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongren on 2016/12/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryScreenSizeDom implements Serializable{
    private String categoryNo;
    private List<SizeStandardItem> sizeStandardItemList;
    private String fourLevelCategoryName;

    @JsonProperty("CategoryNo")
    public String getCategoryNo() {
        return categoryNo;
    }

    @JsonProperty("CategoryNo")
    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo;
    }

    @JsonProperty("FourLevelCategoryName")
    public String getFourLevelCategoryName() {
        return fourLevelCategoryName;
    }
    @JsonProperty("FourLevelCategoryName")
    public void setFourLevelCategoryName(String fourLevelCategoryName) {
        this.fourLevelCategoryName = fourLevelCategoryName;
    }
    @JsonProperty("SizeStandardItemList")
    public List<SizeStandardItem> getSizeStandardItemList() {
        return sizeStandardItemList;
    }
    @JsonProperty("SizeStandardItemList")
    public void setSizeStandardItemList(List<SizeStandardItem> sizeStandardItemList) {
        this.sizeStandardItemList = sizeStandardItemList;
    }
}
