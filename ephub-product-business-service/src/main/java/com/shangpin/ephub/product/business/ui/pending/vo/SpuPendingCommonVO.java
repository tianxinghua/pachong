package com.shangpin.ephub.product.business.ui.pending.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 * spu pending
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class SpuPendingCommonVO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4334243288429261993L;

	private Long spuPendingId;//spuPending表主键

    private String spuModel;//货号

    private String color;

    private String brandNo;

    private String categoryNo;

    private String supplierNo;

    private String supplierId;

    private String material;//材质

    private String origin;//产地

    private String updateTime ;

    private String memo;
    @JsonProperty("spuPendingId")
    public Long getSpuPendingId() {
        return spuPendingId;
    }
    @JsonProperty("spuPendingId")
    public void setSpuPendingId(Long spuPendingId) {
        this.spuPendingId = spuPendingId;
    }
    @JsonProperty("spuModel")
    public String getSpuModel() {
        return spuModel;
    }
    @JsonProperty("spuModel")
    public void setSpuModel(String spuModel) {
        this.spuModel = spuModel;
    }
    @JsonProperty("color")
    public String getColor() {
        return color;
    }
    @JsonProperty("color")
    public void setColor(String color) {
        this.color = color;
    }
    @JsonProperty("brandNo")
    public String getBrandNo() {
        return brandNo;
    }
    @JsonProperty("brandNo")
    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }
    @JsonProperty("categoryNo")
    public String getCategoryNo() {
        return categoryNo;
    }
    @JsonProperty("categoryNo")
    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo;
    }
    @JsonProperty("supplierNo")
    public String getSupplierNo() {
        return supplierNo;
    }
    @JsonProperty("supplierNo")
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }
    @JsonProperty("supplierId")
    public String getSupplierId() {
        return supplierId;
    }
    @JsonProperty("supplierId")
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    @JsonProperty("material")
    public String getMaterial() {
        return material;
    }
    @JsonProperty("material")
    public void setMaterial(String material) {
        this.material = material;
    }
    @JsonProperty("origin")
    public String getOrigin() {
        return origin;
    }
    @JsonProperty("origin")
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    @JsonProperty("updateTime")
    public String getUpdateTime() {
        return updateTime;
    }
    @JsonProperty("updateTime")
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    @JsonProperty("memo")
    public String getMemo() {
        return memo;
    }
    @JsonProperty("memo")
    public void setMemo(String memo) {
        this.memo = memo;
    }
}
