package com.shangpin.ephub.product.business.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>Title:SupplierDTO </p>
 * <p>Description: 通过请求sop api获取supplierid等信息返回的实体对象</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月22日 下午2:22:50
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierDTO {

    @JsonIgnore
    private String supplierNo;
    @JsonIgnore
    private Long sopUserNo;//门户编号 SopUserNo
    @JsonIgnore
    private String supplierId;
    @JsonIgnore
    private String supplierType;//对接类型
    @JsonIgnore
    private String supplierName;//供应商名称
    @JsonIgnore
    private long buttJointId;
    @JsonIgnore
    private String interfaceAddress; //接口地址
    @JsonIgnore
    private String interfaceDesc;  //接口描述
    @JsonIgnore
    private String buttJointNo;   // 接口编码
    @JsonIgnore
    private int interfaceType;// 接口类型    1库存对接   2订单锁库  3订单非锁库
    @JsonIgnore
    private int  interfaceStatus;//对接类型  0无对接   1有对接
    @JsonIgnore
    private String openApiKey;//调用API key
    @JsonIgnore
    private String openApiSecret; //调用API secret
    @JsonIgnore
    private Boolean  isPurchaseException;//是否需要设置采购异常

    @JsonProperty("ButtJointId")
    public void setButtJointId(long buttJointId) {
        buttJointId = buttJointId;
    }
    @JsonProperty("ButtJointNo")
    public void setButtJointNo(String buttJointNo) {
        buttJointNo = buttJointNo;
    }
    @JsonProperty("InterfaceAddress")
    public void setInterfaceAddress(String interfaceAddress) {
        interfaceAddress = interfaceAddress;
    }
    @JsonProperty("InterfaceDesc")
    public void setInterfaceDesc(String interfaceDesc) {
        interfaceDesc = interfaceDesc;
    }
    @JsonProperty("InterfaceStatus")
    public void setInterfaceStatus(int interfaceStatus) {
        interfaceStatus = interfaceStatus;
    }
    @JsonProperty("InterfaceType")
    public void setInterfaceType(int interfaceType) {
        interfaceType = interfaceType;
    }

    public void setIsPurchaseException(Boolean purchaseException) {
        isPurchaseException = purchaseException;
    }

    public void setOpenApiKey(String openApiKey) {
        this.openApiKey = openApiKey;
    }

    public void setOpenApiSecret(String openApiSecret) {
        this.openApiSecret = openApiSecret;
    }

    @JsonProperty("SupplierId")
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    @JsonProperty("SupplierNo")
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    @JsonProperty("SupplierType")
    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    @JsonProperty("ButtJointId")
    public long getButtJointId() {
        return buttJointId;
    }
    @JsonProperty("ButtJointNo")
    public String getButtJointNo() {
        return buttJointNo;
    }
    @JsonProperty("InterfaceAddress")
    public String getInterfaceAddress() {
        return interfaceAddress;
    }
    @JsonProperty("InterfaceDesc")
    public String getInterfaceDesc() {
        return interfaceDesc;
    }
    @JsonProperty("InterfaceStatus")
    public int getInterfaceStatus() {
        return interfaceStatus;
    }
    @JsonProperty("InterfaceType")
    public int getInterfaceType() {
        return interfaceType;
    }

    public Boolean getIsPurchaseException() {
        return isPurchaseException;
    }

    public String getOpenApiKey() {
        return openApiKey;
    }

    public String getOpenApiSecret() {
        return openApiSecret;
    }

    @JsonProperty("SupplierId")
    public String getSupplierId() {
        return supplierId;
    }
    @JsonProperty("SupplierNo")
    public String getSupplierNo() {
        return supplierNo;
    }
    @JsonProperty("SupplierType")
    public String getSupplierType() {
        return supplierType;
    }
    @JsonProperty("SopUserNo")
    public Long getSopUserNo() {
        return sopUserNo;
    }
    @JsonProperty("SopUserNo")
    public void setSopUserNo(Long sopUserNo) {
        this.sopUserNo = sopUserNo;
    }

    @JsonProperty("SupplierName")
    public String getSupplierName() {
        return supplierName;
    }
    @JsonProperty("SupplierName")
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
