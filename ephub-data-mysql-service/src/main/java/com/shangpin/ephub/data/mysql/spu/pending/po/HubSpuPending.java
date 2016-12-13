package com.shangpin.ephub.data.mysql.spu.pending.po;

import java.io.Serializable;
import java.util.Date;

public class HubSpuPending implements Serializable {
    /**
     * 主键
     */
    private Long spuPendingId;

    /**
     * 供应商Id
     */
    private String supplierId;

    /**
     * 供应商Spu编号
     */
    private String supplierSpuNo;

    /**
     * 货号
     */
    private String spuModel;

    /**
     * 供应商原始商品名称
     */
    private String spuName;

    /**
     * 性别
     */
    private String hubGender;

    /**
     * 类目编号
     */
    private String hubCategoryNo;

    /**
     * 品牌编号
     */
    private String hubBrandNo;

    /**
     * 季节编号
     */
    private String hubSeason;

    /**
     * =0 信息待完善 =1 信息已完善
     */
    private Byte spuState;

    /**
     * =0:无图片 =1图片信息已完成 =2 图片信息未完成 
     */
    private Byte picState;

    /**
     * 1：代表当季    0:非当季
     */
    private Byte isCurrentSeason;

    /**
     * =0 否 =1 是
            是否是抓取的新数据不一定是应季新品
     */
    private Byte isNewData;

    /**
     * 商品材质
     */
    private String hubMaterial;

    /**
     * 商品产地
     */
    private String hubOrigin;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 商品描述
     */
    private String spuDesc;

    /**
     * hubspuno
     */
    private String hubSpuNo;

    /**
     * =0 验证不通过 =1 验证已通过
     */
    private Byte spuModelState;

    /**
     * =0 完全不匹配 =1 匹配到4级 =2 不能匹配到末级
     */
    private Byte catgoryState;

    /**
     * 原始信息主键
     */
    private Long supplierSpuId;

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
     * 品牌状态
     */
    private Byte spuBrandState;

    /**
     * 性别状态
     */
    private Byte spuGenderState;

    /**
     * 上市时间及季节状态
     */
    private Byte spuSeasonState;

    /**
     * 颜色
     */
    private String hubColor;

    /**
     * 颜色映射状态
     */
    private Byte spuColorState;

    private static final long serialVersionUID = 1L;

    public Long getSpuPendingId() {
        return spuPendingId;
    }

    public void setSpuPendingId(Long spuPendingId) {
        this.spuPendingId = spuPendingId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSupplierSpuNo() {
        return supplierSpuNo;
    }

    public void setSupplierSpuNo(String supplierSpuNo) {
        this.supplierSpuNo = supplierSpuNo == null ? null : supplierSpuNo.trim();
    }

    public String getSpuModel() {
        return spuModel;
    }

    public void setSpuModel(String spuModel) {
        this.spuModel = spuModel == null ? null : spuModel.trim();
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName == null ? null : spuName.trim();
    }

    public String getHubGender() {
        return hubGender;
    }

    public void setHubGender(String hubGender) {
        this.hubGender = hubGender == null ? null : hubGender.trim();
    }

    public String getHubCategoryNo() {
        return hubCategoryNo;
    }

    public void setHubCategoryNo(String hubCategoryNo) {
        this.hubCategoryNo = hubCategoryNo == null ? null : hubCategoryNo.trim();
    }

    public String getHubBrandNo() {
        return hubBrandNo;
    }

    public void setHubBrandNo(String hubBrandNo) {
        this.hubBrandNo = hubBrandNo == null ? null : hubBrandNo.trim();
    }

    public String getHubSeason() {
        return hubSeason;
    }

    public void setHubSeason(String hubSeason) {
        this.hubSeason = hubSeason == null ? null : hubSeason.trim();
    }

    public Byte getSpuState() {
        return spuState;
    }

    public void setSpuState(Byte spuState) {
        this.spuState = spuState;
    }

    public Byte getPicState() {
        return picState;
    }

    public void setPicState(Byte picState) {
        this.picState = picState;
    }

    public Byte getIsCurrentSeason() {
        return isCurrentSeason;
    }

    public void setIsCurrentSeason(Byte isCurrentSeason) {
        this.isCurrentSeason = isCurrentSeason;
    }

    public Byte getIsNewData() {
        return isNewData;
    }

    public void setIsNewData(Byte isNewData) {
        this.isNewData = isNewData;
    }

    public String getHubMaterial() {
        return hubMaterial;
    }

    public void setHubMaterial(String hubMaterial) {
        this.hubMaterial = hubMaterial == null ? null : hubMaterial.trim();
    }

    public String getHubOrigin() {
        return hubOrigin;
    }

    public void setHubOrigin(String hubOrigin) {
        this.hubOrigin = hubOrigin == null ? null : hubOrigin.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSpuDesc() {
        return spuDesc;
    }

    public void setSpuDesc(String spuDesc) {
        this.spuDesc = spuDesc == null ? null : spuDesc.trim();
    }

    public String getHubSpuNo() {
        return hubSpuNo;
    }

    public void setHubSpuNo(String hubSpuNo) {
        this.hubSpuNo = hubSpuNo == null ? null : hubSpuNo.trim();
    }

    public Byte getSpuModelState() {
        return spuModelState;
    }

    public void setSpuModelState(Byte spuModelState) {
        this.spuModelState = spuModelState;
    }

    public Byte getCatgoryState() {
        return catgoryState;
    }

    public void setCatgoryState(Byte catgoryState) {
        this.catgoryState = catgoryState;
    }

    public Long getSupplierSpuId() {
        return supplierSpuId;
    }

    public void setSupplierSpuId(Long supplierSpuId) {
        this.supplierSpuId = supplierSpuId;
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

    public Byte getSpuBrandState() {
        return spuBrandState;
    }

    public void setSpuBrandState(Byte spuBrandState) {
        this.spuBrandState = spuBrandState;
    }

    public Byte getSpuGenderState() {
        return spuGenderState;
    }

    public void setSpuGenderState(Byte spuGenderState) {
        this.spuGenderState = spuGenderState;
    }

    public Byte getSpuSeasonState() {
        return spuSeasonState;
    }

    public void setSpuSeasonState(Byte spuSeasonState) {
        this.spuSeasonState = spuSeasonState;
    }

    public String getHubColor() {
        return hubColor;
    }

    public void setHubColor(String hubColor) {
        this.hubColor = hubColor == null ? null : hubColor.trim();
    }

    public Byte getSpuColorState() {
        return spuColorState;
    }

    public void setSpuColorState(Byte spuColorState) {
        this.spuColorState = spuColorState;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", spuPendingId=").append(spuPendingId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierSpuNo=").append(supplierSpuNo);
        sb.append(", spuModel=").append(spuModel);
        sb.append(", spuName=").append(spuName);
        sb.append(", hubGender=").append(hubGender);
        sb.append(", hubCategoryNo=").append(hubCategoryNo);
        sb.append(", hubBrandNo=").append(hubBrandNo);
        sb.append(", hubSeason=").append(hubSeason);
        sb.append(", spuState=").append(spuState);
        sb.append(", picState=").append(picState);
        sb.append(", isCurrentSeason=").append(isCurrentSeason);
        sb.append(", isNewData=").append(isNewData);
        sb.append(", hubMaterial=").append(hubMaterial);
        sb.append(", hubOrigin=").append(hubOrigin);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", spuDesc=").append(spuDesc);
        sb.append(", hubSpuNo=").append(hubSpuNo);
        sb.append(", spuModelState=").append(spuModelState);
        sb.append(", catgoryState=").append(catgoryState);
        sb.append(", supplierSpuId=").append(supplierSpuId);
        sb.append(", memo=").append(memo);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
        sb.append(", spuBrandState=").append(spuBrandState);
        sb.append(", spuGenderState=").append(spuGenderState);
        sb.append(", spuSeasonState=").append(spuSeasonState);
        sb.append(", hubColor=").append(hubColor);
        sb.append(", spuColorState=").append(spuColorState);
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
        HubSpuPending other = (HubSpuPending) that;
        return (this.getSpuPendingId() == null ? other.getSpuPendingId() == null : this.getSpuPendingId().equals(other.getSpuPendingId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierSpuNo() == null ? other.getSupplierSpuNo() == null : this.getSupplierSpuNo().equals(other.getSupplierSpuNo()))
            && (this.getSpuModel() == null ? other.getSpuModel() == null : this.getSpuModel().equals(other.getSpuModel()))
            && (this.getSpuName() == null ? other.getSpuName() == null : this.getSpuName().equals(other.getSpuName()))
            && (this.getHubGender() == null ? other.getHubGender() == null : this.getHubGender().equals(other.getHubGender()))
            && (this.getHubCategoryNo() == null ? other.getHubCategoryNo() == null : this.getHubCategoryNo().equals(other.getHubCategoryNo()))
            && (this.getHubBrandNo() == null ? other.getHubBrandNo() == null : this.getHubBrandNo().equals(other.getHubBrandNo()))
            && (this.getHubSeason() == null ? other.getHubSeason() == null : this.getHubSeason().equals(other.getHubSeason()))
            && (this.getSpuState() == null ? other.getSpuState() == null : this.getSpuState().equals(other.getSpuState()))
            && (this.getPicState() == null ? other.getPicState() == null : this.getPicState().equals(other.getPicState()))
            && (this.getIsCurrentSeason() == null ? other.getIsCurrentSeason() == null : this.getIsCurrentSeason().equals(other.getIsCurrentSeason()))
            && (this.getIsNewData() == null ? other.getIsNewData() == null : this.getIsNewData().equals(other.getIsNewData()))
            && (this.getHubMaterial() == null ? other.getHubMaterial() == null : this.getHubMaterial().equals(other.getHubMaterial()))
            && (this.getHubOrigin() == null ? other.getHubOrigin() == null : this.getHubOrigin().equals(other.getHubOrigin()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSpuDesc() == null ? other.getSpuDesc() == null : this.getSpuDesc().equals(other.getSpuDesc()))
            && (this.getHubSpuNo() == null ? other.getHubSpuNo() == null : this.getHubSpuNo().equals(other.getHubSpuNo()))
            && (this.getSpuModelState() == null ? other.getSpuModelState() == null : this.getSpuModelState().equals(other.getSpuModelState()))
            && (this.getCatgoryState() == null ? other.getCatgoryState() == null : this.getCatgoryState().equals(other.getCatgoryState()))
            && (this.getSupplierSpuId() == null ? other.getSupplierSpuId() == null : this.getSupplierSpuId().equals(other.getSupplierSpuId()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getSpuBrandState() == null ? other.getSpuBrandState() == null : this.getSpuBrandState().equals(other.getSpuBrandState()))
            && (this.getSpuGenderState() == null ? other.getSpuGenderState() == null : this.getSpuGenderState().equals(other.getSpuGenderState()))
            && (this.getSpuSeasonState() == null ? other.getSpuSeasonState() == null : this.getSpuSeasonState().equals(other.getSpuSeasonState()))
            && (this.getHubColor() == null ? other.getHubColor() == null : this.getHubColor().equals(other.getHubColor()))
            && (this.getSpuColorState() == null ? other.getSpuColorState() == null : this.getSpuColorState().equals(other.getSpuColorState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSpuPendingId() == null) ? 0 : getSpuPendingId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierSpuNo() == null) ? 0 : getSupplierSpuNo().hashCode());
        result = prime * result + ((getSpuModel() == null) ? 0 : getSpuModel().hashCode());
        result = prime * result + ((getSpuName() == null) ? 0 : getSpuName().hashCode());
        result = prime * result + ((getHubGender() == null) ? 0 : getHubGender().hashCode());
        result = prime * result + ((getHubCategoryNo() == null) ? 0 : getHubCategoryNo().hashCode());
        result = prime * result + ((getHubBrandNo() == null) ? 0 : getHubBrandNo().hashCode());
        result = prime * result + ((getHubSeason() == null) ? 0 : getHubSeason().hashCode());
        result = prime * result + ((getSpuState() == null) ? 0 : getSpuState().hashCode());
        result = prime * result + ((getPicState() == null) ? 0 : getPicState().hashCode());
        result = prime * result + ((getIsCurrentSeason() == null) ? 0 : getIsCurrentSeason().hashCode());
        result = prime * result + ((getIsNewData() == null) ? 0 : getIsNewData().hashCode());
        result = prime * result + ((getHubMaterial() == null) ? 0 : getHubMaterial().hashCode());
        result = prime * result + ((getHubOrigin() == null) ? 0 : getHubOrigin().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSpuDesc() == null) ? 0 : getSpuDesc().hashCode());
        result = prime * result + ((getHubSpuNo() == null) ? 0 : getHubSpuNo().hashCode());
        result = prime * result + ((getSpuModelState() == null) ? 0 : getSpuModelState().hashCode());
        result = prime * result + ((getCatgoryState() == null) ? 0 : getCatgoryState().hashCode());
        result = prime * result + ((getSupplierSpuId() == null) ? 0 : getSupplierSpuId().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getSpuBrandState() == null) ? 0 : getSpuBrandState().hashCode());
        result = prime * result + ((getSpuGenderState() == null) ? 0 : getSpuGenderState().hashCode());
        result = prime * result + ((getSpuSeasonState() == null) ? 0 : getSpuSeasonState().hashCode());
        result = prime * result + ((getHubColor() == null) ? 0 : getHubColor().hashCode());
        result = prime * result + ((getSpuColorState() == null) ? 0 : getSpuColorState().hashCode());
        return result;
    }
}