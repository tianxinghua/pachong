package com.shangpin.pending.product.consumer.supplier.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lizhongren on 2016/12/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SizeStandardItem {

    private int sortIndex;
    private String sizeStandardValue;
    private long sizeStandardValueId;
    private long sizeStandardId;
    private String sizeStandardName;
    private byte isScreening;
    private String brandNo;
    private long screenSizeStandardValueId;
    private String sizeStandardDesc;

    @JsonProperty("BrandNo")
    public String getBrandNo() {
        return brandNo;
    }
    @JsonProperty("BrandNo")
    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }
    @JsonProperty("IsScreening")
    public byte getIsScreening() {
        return isScreening;
    }
    @JsonProperty("IsScreening")
    public void setIsScreening(byte isScreening) {
        this.isScreening = isScreening;
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
    @JsonProperty("SortIndex")
    public int getSortIndex() {
        return sortIndex;
    }
    @JsonProperty("SortIndex")
    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }
}
