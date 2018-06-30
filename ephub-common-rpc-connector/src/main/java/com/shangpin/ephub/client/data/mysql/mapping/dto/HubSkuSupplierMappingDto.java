package com.shangpin.ephub.client.data.mysql.mapping.dto;

import java.io.Serializable;
import java.util.Date;

public class HubSkuSupplierMappingDto implements Serializable {
    /**
     * 主键
     */
    private Long skuSupplierMappingId;

    /**
     * Sku编号
     */
    private String skuNo;

    /**
     * 供应商SkuNo
     */
    private String supplierSkuNo;

    /**
     * 条码
     */
    private String barcode;

    /**
     * 供应商Id
     */
    private String supplierId;

    /**
     * 新品类型
     */
    private Byte newSpuType;

    /**
     * 是否新供应商供货
     */
    private Byte isNewSupplier;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 供应商货号
     */
    private String supplierSpuModel;

    /**
     * 原始skuId
     */
    private Long supplierSkuId;

    /**
     * 备注
     */
    private String memo;

    /**
     * 数据状态：1未删除 0已删除
     */
    private Byte dataState;

    /**
     * 版本字段
     */
    private Long version;

    private String supplierNo;

    /**
     * =0 待选  =1 选品中  =2 已选 =3 scm审核中 =4 选品失败
     */
    private Byte supplierSelectState;

    private String spSkuNo;

    private String spuNo;

    private String spSpuNo;

    /**
     * 产地
     */
    private String origin;

    private Integer retryNum;

    private static final long serialVersionUID = 1L;

    public Long getSkuSupplierMappingId() {
        return skuSupplierMappingId;
    }

    public void setSkuSupplierMappingId(Long skuSupplierMappingId) {
        this.skuSupplierMappingId = skuSupplierMappingId;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo == null ? null : skuNo.trim();
    }

    public String getSupplierSkuNo() {
        return supplierSkuNo;
    }

    public void setSupplierSkuNo(String supplierSkuNo) {
        this.supplierSkuNo = supplierSkuNo == null ? null : supplierSkuNo.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public Byte getNewSpuType() {
        return newSpuType;
    }

    public void setNewSpuType(Byte newSpuType) {
        this.newSpuType = newSpuType;
    }

    public Byte getIsNewSupplier() {
        return isNewSupplier;
    }

    public void setIsNewSupplier(Byte isNewSupplier) {
        this.isNewSupplier = isNewSupplier;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public String getSupplierSpuModel() {
        return supplierSpuModel;
    }

    public void setSupplierSpuModel(String supplierSpuModel) {
        this.supplierSpuModel = supplierSpuModel == null ? null : supplierSpuModel.trim();
    }

    public Long getSupplierSkuId() {
        return supplierSkuId;
    }

    public void setSupplierSkuId(Long supplierSkuId) {
        this.supplierSkuId = supplierSkuId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Byte getDataState() {
        return dataState;
    }

    public void setDataState(Byte dataState) {
        this.dataState = dataState;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo == null ? null : supplierNo.trim();
    }

    public Byte getSupplierSelectState() {
        return supplierSelectState;
    }

    public void setSupplierSelectState(Byte supplierSelectState) {
        this.supplierSelectState = supplierSelectState;
    }

    public String getSpSkuNo() {
        return spSkuNo;
    }

    public void setSpSkuNo(String spSkuNo) {
        this.spSkuNo = spSkuNo == null ? null : spSkuNo.trim();
    }

    public String getSpuNo() {
        return spuNo;
    }

    public void setSpuNo(String spuNo) {
        this.spuNo = spuNo == null ? null : spuNo.trim();
    }

    public String getSpSpuNo() {
        return spSpuNo;
    }

    public void setSpSpuNo(String spSpuNo) {
        this.spSpuNo = spSpuNo == null ? null : spSpuNo.trim();
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
    }

    public Integer getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(Integer retryNum) {
        this.retryNum = retryNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", skuSupplierMappingId=").append(skuSupplierMappingId);
        sb.append(", skuNo=").append(skuNo);
        sb.append(", supplierSkuNo=").append(supplierSkuNo);
        sb.append(", barcode=").append(barcode);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", newSpuType=").append(newSpuType);
        sb.append(", isNewSupplier=").append(isNewSupplier);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", supplierSpuModel=").append(supplierSpuModel);
        sb.append(", supplierSkuId=").append(supplierSkuId);
        sb.append(", memo=").append(memo);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", supplierSelectState=").append(supplierSelectState);
        sb.append(", spSkuNo=").append(spSkuNo);
        sb.append(", spuNo=").append(spuNo);
        sb.append(", spSpuNo=").append(spSpuNo);
        sb.append(", origin=").append(origin);
        sb.append(", retryNum=").append(retryNum);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        HubSkuSupplierMappingDto other = (HubSkuSupplierMappingDto) that;
        return (this.getSkuSupplierMappingId() == null ? other.getSkuSupplierMappingId() == null : this.getSkuSupplierMappingId().equals(other.getSkuSupplierMappingId()))
            && (this.getSkuNo() == null ? other.getSkuNo() == null : this.getSkuNo().equals(other.getSkuNo()))
            && (this.getSupplierSkuNo() == null ? other.getSupplierSkuNo() == null : this.getSupplierSkuNo().equals(other.getSupplierSkuNo()))
            && (this.getBarcode() == null ? other.getBarcode() == null : this.getBarcode().equals(other.getBarcode()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getNewSpuType() == null ? other.getNewSpuType() == null : this.getNewSpuType().equals(other.getNewSpuType()))
            && (this.getIsNewSupplier() == null ? other.getIsNewSupplier() == null : this.getIsNewSupplier().equals(other.getIsNewSupplier()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getSupplierSpuModel() == null ? other.getSupplierSpuModel() == null : this.getSupplierSpuModel().equals(other.getSupplierSpuModel()))
            && (this.getSupplierSkuId() == null ? other.getSupplierSkuId() == null : this.getSupplierSkuId().equals(other.getSupplierSkuId()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getSupplierSelectState() == null ? other.getSupplierSelectState() == null : this.getSupplierSelectState().equals(other.getSupplierSelectState()))
            && (this.getSpSkuNo() == null ? other.getSpSkuNo() == null : this.getSpSkuNo().equals(other.getSpSkuNo()))
            && (this.getSpuNo() == null ? other.getSpuNo() == null : this.getSpuNo().equals(other.getSpuNo()))
            && (this.getSpSpuNo() == null ? other.getSpSpuNo() == null : this.getSpSpuNo().equals(other.getSpSpuNo()))
            && (this.getOrigin() == null ? other.getOrigin() == null : this.getOrigin().equals(other.getOrigin()))
            && (this.getRetryNum() == null ? other.getRetryNum() == null : this.getRetryNum().equals(other.getRetryNum()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSkuSupplierMappingId() == null) ? 0 : getSkuSupplierMappingId().hashCode());
        result = prime * result + ((getSkuNo() == null) ? 0 : getSkuNo().hashCode());
        result = prime * result + ((getSupplierSkuNo() == null) ? 0 : getSupplierSkuNo().hashCode());
        result = prime * result + ((getBarcode() == null) ? 0 : getBarcode().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getNewSpuType() == null) ? 0 : getNewSpuType().hashCode());
        result = prime * result + ((getIsNewSupplier() == null) ? 0 : getIsNewSupplier().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getSupplierSpuModel() == null) ? 0 : getSupplierSpuModel().hashCode());
        result = prime * result + ((getSupplierSkuId() == null) ? 0 : getSupplierSkuId().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getSupplierSelectState() == null) ? 0 : getSupplierSelectState().hashCode());
        result = prime * result + ((getSpSkuNo() == null) ? 0 : getSpSkuNo().hashCode());
        result = prime * result + ((getSpuNo() == null) ? 0 : getSpuNo().hashCode());
        result = prime * result + ((getSpSpuNo() == null) ? 0 : getSpSpuNo().hashCode());
        result = prime * result + ((getOrigin() == null) ? 0 : getOrigin().hashCode());
        result = prime * result + ((getRetryNum() == null) ? 0 : getRetryNum().hashCode());
        return result;
    }
}