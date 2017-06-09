package com.shangpin.ephub.client.data.mysql.studio.dic.dto;

import java.io.Serializable;
import java.util.Date;

public class HubDicCategoryMeasureTemplateDto implements Serializable {
    /**
     * 主键
     */
    private Long categoryMeasureTemplateId;

    /**
     * 品类编号
     */
    private String categoryNo;

    /**
     * 属性定义
     */
    private String attributeDefinition;

    /**
     * 属性定义类型  0:图片 1：正常属性
     */
    private Byte attributeDefinitionType;

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
     * 数据状态：1未删除 0已删除
     */
    private Byte dataState;

    /**
     * 版本字段
     */
    private Long version;

    private static final long serialVersionUID = 1L;

    public Long getCategoryMeasureTemplateId() {
        return categoryMeasureTemplateId;
    }

    public void setCategoryMeasureTemplateId(Long categoryMeasureTemplateId) {
        this.categoryMeasureTemplateId = categoryMeasureTemplateId;
    }

    public String getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo == null ? null : categoryNo.trim();
    }

    public String getAttributeDefinition() {
        return attributeDefinition;
    }

    public void setAttributeDefinition(String attributeDefinition) {
        this.attributeDefinition = attributeDefinition == null ? null : attributeDefinition.trim();
    }

    public Byte getAttributeDefinitionType() {
        return attributeDefinitionType;
    }

    public void setAttributeDefinitionType(Byte attributeDefinitionType) {
        this.attributeDefinitionType = attributeDefinitionType;
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
        sb.append(", categoryMeasureTemplateId=").append(categoryMeasureTemplateId);
        sb.append(", categoryNo=").append(categoryNo);
        sb.append(", attributeDefinition=").append(attributeDefinition);
        sb.append(", attributeDefinitionType=").append(attributeDefinitionType);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
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
        HubDicCategoryMeasureTemplateDto other = (HubDicCategoryMeasureTemplateDto) that;
        return (this.getCategoryMeasureTemplateId() == null ? other.getCategoryMeasureTemplateId() == null : this.getCategoryMeasureTemplateId().equals(other.getCategoryMeasureTemplateId()))
            && (this.getCategoryNo() == null ? other.getCategoryNo() == null : this.getCategoryNo().equals(other.getCategoryNo()))
            && (this.getAttributeDefinition() == null ? other.getAttributeDefinition() == null : this.getAttributeDefinition().equals(other.getAttributeDefinition()))
            && (this.getAttributeDefinitionType() == null ? other.getAttributeDefinitionType() == null : this.getAttributeDefinitionType().equals(other.getAttributeDefinitionType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCategoryMeasureTemplateId() == null) ? 0 : getCategoryMeasureTemplateId().hashCode());
        result = prime * result + ((getCategoryNo() == null) ? 0 : getCategoryNo().hashCode());
        result = prime * result + ((getAttributeDefinition() == null) ? 0 : getAttributeDefinition().hashCode());
        result = prime * result + ((getAttributeDefinitionType() == null) ? 0 : getAttributeDefinitionType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}