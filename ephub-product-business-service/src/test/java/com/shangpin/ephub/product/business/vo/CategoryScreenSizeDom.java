package com.shangpin.ephub.product.business.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryScreenSizeDom {

	@JsonIgnore
	public String categoryNo;
	@JsonIgnore
    public List<SizeStandardItem> sizeStandardItemList;
	@JsonIgnore
    public String fourLevelCategoryName;
	
	@JsonProperty("CategoryNo")
	public String getCategoryNo() {
		return categoryNo;
	}
	@JsonProperty("CategoryNo")
	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}
	@JsonProperty("SizeStandardItemList")
	public List<SizeStandardItem> getSizeStandardItemList() {
		return sizeStandardItemList;
	}
	@JsonProperty("SizeStandardItemList") 
	public void setSizeStandardItemList(List<SizeStandardItem> sizeStandardItemList) {
		this.sizeStandardItemList = sizeStandardItemList;
	}
	@JsonProperty("FourLevelCategoryName")
	public String getFourLevelCategoryName() {
		return fourLevelCategoryName;
	}
	@JsonProperty("FourLevelCategoryName")
	public void setFourLevelCategoryName(String fourLevelCategoryName) {
		this.fourLevelCategoryName = fourLevelCategoryName;
	}
    
    
}
