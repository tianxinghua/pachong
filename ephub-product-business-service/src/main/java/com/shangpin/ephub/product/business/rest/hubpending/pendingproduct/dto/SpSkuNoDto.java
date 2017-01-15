package com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by loyalty on 17/1/1.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpSkuNoDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -725758204102710771L;
	private String supplierNo;
    private String supplierId;
    private String skuNo;
    private String supplierSkuNo;//供货商SKUNO
    private String errorReason;  //
    private int   sign;//生成是否成功标记  1:成功  0:失败

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

    @JsonProperty("errorReason")
    public String getErrorReason() {
        return errorReason;
    }
    @JsonProperty("errorReason")
    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    @JsonProperty("sign")
    public int getSign() {
        return sign;
    }
    @JsonProperty("sign")
    public void setSign(int sign) {
        this.sign = sign;
    }
}
