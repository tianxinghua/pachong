package com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 17/1/1.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpSkuNoDto implements Serializable {

    private String supplierNo;
    private String supplierId;
    private String skuNo;
    private String supplierSkuNo;//供货商SKUNO

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
    @JsonProperty("skuNo")
    public String getSkuNo() {
        return skuNo;
    }
    @JsonProperty("skuNo")
    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }
    @JsonProperty("supplierSkuNo")
    public String getSupplierSkuNo() {
        return supplierSkuNo;
    }
    @JsonProperty("supplierSkuNo")
    public void setSupplierSkuNo(String supplierSkuNo) {
        this.supplierSkuNo = supplierSkuNo;
    }
}
