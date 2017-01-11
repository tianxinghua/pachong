package com.shangpin.ephub.product.business.common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryRequestDto {
	
	public List<String> categoryNos;
	
    public String categoryName;
	
    public String categoryNo;
	
    public String categoryType;
	
    public String isValid;
	
	@JsonProperty("CategoryNos")
	public List<String> getCategoryNos() {
		return categoryNos;
	}
	@JsonProperty("CategoryNos")
	public void setCategoryNos(List<String> categoryNos) {
		this.categoryNos = categoryNos;
	}
	@JsonProperty("CategoryName")
	public String getCategoryName() {
		return categoryName;
	}
	@JsonProperty("CategoryName")
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	@JsonProperty("CategoryNo")
	public String getCategoryNo() {
		return categoryNo;
	}
	@JsonProperty("CategoryNo")
	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}
	@JsonProperty("CategoryType")
	public String getCategoryType() {
		return categoryType;
	}
	@JsonProperty("CategoryType")
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	@JsonProperty("IsValid")
	public String getIsValid() {
		return isValid;
	}
	@JsonProperty("IsValid")
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
	

}
