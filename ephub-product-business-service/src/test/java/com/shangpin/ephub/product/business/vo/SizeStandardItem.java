package com.shangpin.ephub.product.business.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SizeStandardItem {

	@JsonIgnore
	public int sortIndex;
	@JsonIgnore
    public String sizeStandardValue;
	@JsonIgnore
    public long sizeStandardValueId;
	@JsonIgnore
    public long sizeStandardId;
	@JsonIgnore
    public String sizeStandardName;
	@JsonIgnore
    public byte isScreening;
	@JsonIgnore
    public String brandNo;
	@JsonIgnore
    public long screenSizeStandardValueId;
	@JsonIgnore
    public String sizeStandardDesc;
	
	@JsonProperty("SortIndex")
	public int getSortIndex() {
		return sortIndex;
	}
	@JsonProperty("SortIndex")
	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}
	@JsonProperty("SizeStandardValue")
	public String getSizeStandardValue() {
		return sizeStandardValue;
	}
	@JsonProperty("SizeStandardValue")
	public void setSizeStandardValue(String sizeStandardValue) {
		this.sizeStandardValue = sizeStandardValue;
	}
	@JsonProperty("SizeStandardValueId")
	public long getSizeStandardValueId() {
		return sizeStandardValueId;
	}
	@JsonProperty("SizeStandardValueId")
	public void setSizeStandardValueId(long sizeStandardValueId) {
		this.sizeStandardValueId = sizeStandardValueId;
	}
	@JsonProperty("SizeStandardId")
	public long getSizeStandardId() {
		return sizeStandardId;
	}
	@JsonProperty("SizeStandardId")
	public void setSizeStandardId(long sizeStandardId) {
		this.sizeStandardId = sizeStandardId;
	}
	@JsonProperty("SizeStandardName")
	public String getSizeStandardName() {
		return sizeStandardName;
	}
	@JsonProperty("SizeStandardName")
	public void setSizeStandardName(String sizeStandardName) {
		this.sizeStandardName = sizeStandardName;
	}
	@JsonProperty("IsScreening")
	public byte getIsScreening() {
		return isScreening;
	}
	@JsonProperty("IsScreening")
	public void setIsScreening(byte isScreening) {
		this.isScreening = isScreening;
	}
	@JsonProperty("BrandNo")
	public String getBrandNo() {
		return brandNo;
	}
	@JsonProperty("BrandNo")
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	@JsonProperty("ScreenSizeStandardValueId")
	public long getScreenSizeStandardValueId() {
		return screenSizeStandardValueId;
	}
	@JsonProperty("ScreenSizeStandardValueId")
	public void setScreenSizeStandardValueId(long screenSizeStandardValueId) {
		this.screenSizeStandardValueId = screenSizeStandardValueId;
	}
	@JsonProperty("SizeStandardDesc")
	public String getSizeStandardDesc() {
		return sizeStandardDesc;
	}
	@JsonProperty("SizeStandardDesc")
	public void setSizeStandardDesc(String sizeStandardDesc) {
		this.sizeStandardDesc = sizeStandardDesc;
	}
    
    
}
