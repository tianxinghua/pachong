package com.shangpin.ephub.product.business.pendingPage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by loyalty on 16/12/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpSkuDTO {

    private String supplierNo;
    private String supplierId;
    private String skuNo;
    private String supplierSkuNo;//供货商SKUNO

    @JsonProperty("SupplierNo")
    public String getSupplierNo() {
        return supplierNo;
    }
    @JsonProperty("SupplierNo")
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    @JsonProperty("SupplierId")
    public String getSupplierId() {
        return supplierId;
    }

    @JsonProperty("SupplierId")
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @JsonProperty("SkuNo")
    public String getSkuNo() {
        return skuNo;
    }

    @JsonProperty("SkuNo")
    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    @JsonProperty("SupplierSkuNo")
    public String getSupplierSkuNo() {
        return supplierSkuNo;
    }

    @JsonProperty("SupplierSkuNo")
    public void setSupplierSkuNo(String supplierSkuNo) {
        this.supplierSkuNo = supplierSkuNo;
    }
}
