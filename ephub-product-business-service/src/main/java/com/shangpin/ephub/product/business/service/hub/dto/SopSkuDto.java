package com.shangpin.ephub.product.business.service.hub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by lizhongren on 2017/2/14.
 */

@ToString
public class SopSkuDto implements Serializable {
    @JsonIgnore
    private String  skuNo;
    @JsonIgnore
    private String  productNo;
    @JsonIgnore
    private Long   sopUserNo;
    @JsonIgnore
    private String  supplierSkuNo;
    @JsonIgnore
    private String  productModel;

    @JsonProperty("SkuNo")
    public String getSkuNo() {
        return skuNo;
    }
    @JsonProperty("SkuNo")
    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }
    @JsonProperty("ProductNo")
    public String getProductNo() {
        return productNo;
    }
    @JsonProperty("ProductNo")
    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }
    @JsonProperty("SopUserNo")
    public Long getSopUserNo() {
        return sopUserNo;
    }
    @JsonProperty("SopUserNo")
    public void setSopUserNo(Long sopUserNo) {
        this.sopUserNo = sopUserNo;
    }
    @JsonProperty("SupplierSkuNo")
    public String getSupplierSkuNo() {
        return supplierSkuNo;
    }
    @JsonProperty("SupplierSkuNo")
    public void setSupplierSkuNo(String supplierSkuNo) {
        this.supplierSkuNo = supplierSkuNo;
    }
    @JsonProperty("ProductModel")
    public String getProductModel() {
        return productModel;
    }
    @JsonProperty("ProductModel")
    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }
}
