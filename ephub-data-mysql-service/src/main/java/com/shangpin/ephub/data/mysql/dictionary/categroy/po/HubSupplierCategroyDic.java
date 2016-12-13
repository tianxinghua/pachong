package com.shangpin.ephub.data.mysql.dictionary.categroy.po;

import java.io.Serializable;
import java.util.Date;

public class HubSupplierCategroyDic implements Serializable {
    /**
     * 主键
     */
    private Long supplierCategoryDicId;

    /**
     * 供应商
     */
    private String supplierId;

    /**
     * 供应商品类
     */
    private String supplierCategory;

    /**
     * 尚品品类编号
     */
    private String hubCategoryNo;

    /**
     * 尚品类目级别
     */
    private Byte categoryType;

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

    /**
     * 性别
     */
    private Long genderDicId;

    private static final long serialVersionUID = 1L;

    public Long getSupplierCategoryDicId() {
        return supplierCategoryDicId;
    }

    public void setSupplierCategoryDicId(Long supplierCategoryDicId) {
        this.supplierCategoryDicId = supplierCategoryDicId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSupplierCategory() {
        return supplierCategory;
    }

    public void setSupplierCategory(String supplierCategory) {
        this.supplierCategory = supplierCategory == null ? null : supplierCategory.trim();
    }

    public String getHubCategoryNo() {
        return hubCategoryNo;
    }

    public void setHubCategoryNo(String hubCategoryNo) {
        this.hubCategoryNo = hubCategoryNo == null ? null : hubCategoryNo.trim();
    }

    public Byte getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Byte categoryType) {
        this.categoryType = categoryType;
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

    public Long getGenderDicId() {
        return genderDicId;
    }

    public void setGenderDicId(Long genderDicId) {
        this.genderDicId = genderDicId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", supplierCategoryDicId=").append(supplierCategoryDicId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierCategory=").append(supplierCategory);
        sb.append(", hubCategoryNo=").append(hubCategoryNo);
        sb.append(", categoryType=").append(categoryType);
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
        sb.append(", genderDicId=").append(genderDicId);
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
        HubSupplierCategroyDic other = (HubSupplierCategroyDic) that;
        return (this.getSupplierCategoryDicId() == null ? other.getSupplierCategoryDicId() == null : this.getSupplierCategoryDicId().equals(other.getSupplierCategoryDicId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierCategory() == null ? other.getSupplierCategory() == null : this.getSupplierCategory().equals(other.getSupplierCategory()))
            && (this.getHubCategoryNo() == null ? other.getHubCategoryNo() == null : this.getHubCategoryNo().equals(other.getHubCategoryNo()))
            && (this.getCategoryType() == null ? other.getCategoryType() == null : this.getCategoryType().equals(other.getCategoryType()))
            && (this.getMappingState() == null ? other.getMappingState() == null : this.getMappingState().equals(other.getMappingState()))
            && (this.getPushState() == null ? other.getPushState() == null : this.getPushState().equals(other.getPushState()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getPushTime() == null ? other.getPushTime() == null : this.getPushTime().equals(other.getPushTime()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getGenderDicId() == null ? other.getGenderDicId() == null : this.getGenderDicId().equals(other.getGenderDicId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSupplierCategoryDicId() == null) ? 0 : getSupplierCategoryDicId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierCategory() == null) ? 0 : getSupplierCategory().hashCode());
        result = prime * result + ((getHubCategoryNo() == null) ? 0 : getHubCategoryNo().hashCode());
        result = prime * result + ((getCategoryType() == null) ? 0 : getCategoryType().hashCode());
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
        result = prime * result + ((getGenderDicId() == null) ? 0 : getGenderDicId().hashCode());
        return result;
    }
}