package com.shangpin.ephub.data.mysql.dictionary.brand.po;

import java.io.Serializable;
import java.util.Date;

public class HubSupplierBrandDic implements Serializable {
    /**
     * 主键
     */
    private Long supplierBrandDicId;

    /**
     * 供应商
     */
    private String supplierId;

    /**
     * 供应商品牌
     */
    private String supplierBrand;

    /**
     * 尚品品牌编号
     */
    private String hubBrandNo;

    /**
     * =0 未映射 =1 已映射 =2 部分映射
     */
    private Byte mappingState;

    /**
     * 发布状态
     */
    private Byte pushState;

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
     * 发布时间
     */
    private Date pushTime;

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

    private static final long serialVersionUID = 1L;

    public Long getSupplierBrandDicId() {
        return supplierBrandDicId;
    }

    public void setSupplierBrandDicId(Long supplierBrandDicId) {
        this.supplierBrandDicId = supplierBrandDicId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSupplierBrand() {
        return supplierBrand;
    }

    public void setSupplierBrand(String supplierBrand) {
        this.supplierBrand = supplierBrand == null ? null : supplierBrand.trim();
    }

    public String getHubBrandNo() {
        return hubBrandNo;
    }

    public void setHubBrandNo(String hubBrandNo) {
        this.hubBrandNo = hubBrandNo == null ? null : hubBrandNo.trim();
    }

    public Byte getMappingState() {
        return mappingState;
    }

    public void setMappingState(Byte mappingState) {
        this.mappingState = mappingState;
    }

    public Byte getPushState() {
        return pushState;
    }

    public void setPushState(Byte pushState) {
        this.pushState = pushState;
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

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", supplierBrandDicId=").append(supplierBrandDicId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierBrand=").append(supplierBrand);
        sb.append(", hubBrandNo=").append(hubBrandNo);
        sb.append(", mappingState=").append(mappingState);
        sb.append(", pushState=").append(pushState);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", pushTime=").append(pushTime);
        sb.append(", memo=").append(memo);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
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
        HubSupplierBrandDic other = (HubSupplierBrandDic) that;
        return (this.getSupplierBrandDicId() == null ? other.getSupplierBrandDicId() == null : this.getSupplierBrandDicId().equals(other.getSupplierBrandDicId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierBrand() == null ? other.getSupplierBrand() == null : this.getSupplierBrand().equals(other.getSupplierBrand()))
            && (this.getHubBrandNo() == null ? other.getHubBrandNo() == null : this.getHubBrandNo().equals(other.getHubBrandNo()))
            && (this.getMappingState() == null ? other.getMappingState() == null : this.getMappingState().equals(other.getMappingState()))
            && (this.getPushState() == null ? other.getPushState() == null : this.getPushState().equals(other.getPushState()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getPushTime() == null ? other.getPushTime() == null : this.getPushTime().equals(other.getPushTime()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSupplierBrandDicId() == null) ? 0 : getSupplierBrandDicId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierBrand() == null) ? 0 : getSupplierBrand().hashCode());
        result = prime * result + ((getHubBrandNo() == null) ? 0 : getHubBrandNo().hashCode());
        result = prime * result + ((getMappingState() == null) ? 0 : getMappingState().hashCode());
        result = prime * result + ((getPushState() == null) ? 0 : getPushState().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getPushTime() == null) ? 0 : getPushTime().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}