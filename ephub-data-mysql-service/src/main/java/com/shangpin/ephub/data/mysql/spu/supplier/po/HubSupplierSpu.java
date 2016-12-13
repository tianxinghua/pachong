package com.shangpin.ephub.data.mysql.spu.supplier.po;

import java.io.Serializable;
import java.util.Date;

public class HubSupplierSpu implements Serializable {
    /**
     * 主键
     */
    private Long supplierSpuId;

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
    private String supplierSpuModel;

    /**
     * 供应商原始商品名称
     */
    private String supplierSpuName;

    /**
     * 商品颜色
     */
    private String supplierSpuColor;

    /**
     * 性别
     */
    private String supplierGender;

    /**
     * 供应商类目编号
     */
    private String supplierCategoryno;

    /**
     * 供应商类目名称
     */
    private String supplierCategoryname;

    /**
     * 供应商品牌编号
     */
    private String supplierBrandno;

    /**
     * 供应商品牌名称
     */
    private String supplierBrandname;

    /**
     * 供应商季节编号
     */
    private String supplierSeasonno;

    /**
     * 供应商季节名称
     */
    private String supplierSeasonname;

    /**
     * 0:不存在  1：存在
     */
    private Byte isexistpic;

    /**
     * 商品材质
     */
    private String supplierMaterial;

    /**
     * 商品产地
     */
    private String supplierOrigin;

    /**
     * 商品描述
     */
    private String supplierSpuDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * =0 信息不完整 =1 信息已完整
     */
    private Byte infoState;

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

    public Long getSupplierSpuId() {
        return supplierSpuId;
    }

    public void setSupplierSpuId(Long supplierSpuId) {
        this.supplierSpuId = supplierSpuId;
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

    public String getSupplierSpuModel() {
        return supplierSpuModel;
    }

    public void setSupplierSpuModel(String supplierSpuModel) {
        this.supplierSpuModel = supplierSpuModel == null ? null : supplierSpuModel.trim();
    }

    public String getSupplierSpuName() {
        return supplierSpuName;
    }

    public void setSupplierSpuName(String supplierSpuName) {
        this.supplierSpuName = supplierSpuName == null ? null : supplierSpuName.trim();
    }

    public String getSupplierSpuColor() {
        return supplierSpuColor;
    }

    public void setSupplierSpuColor(String supplierSpuColor) {
        this.supplierSpuColor = supplierSpuColor == null ? null : supplierSpuColor.trim();
    }

    public String getSupplierGender() {
        return supplierGender;
    }

    public void setSupplierGender(String supplierGender) {
        this.supplierGender = supplierGender == null ? null : supplierGender.trim();
    }

    public String getSupplierCategoryno() {
        return supplierCategoryno;
    }

    public void setSupplierCategoryno(String supplierCategoryno) {
        this.supplierCategoryno = supplierCategoryno == null ? null : supplierCategoryno.trim();
    }

    public String getSupplierCategoryname() {
        return supplierCategoryname;
    }

    public void setSupplierCategoryname(String supplierCategoryname) {
        this.supplierCategoryname = supplierCategoryname == null ? null : supplierCategoryname.trim();
    }

    public String getSupplierBrandno() {
        return supplierBrandno;
    }

    public void setSupplierBrandno(String supplierBrandno) {
        this.supplierBrandno = supplierBrandno == null ? null : supplierBrandno.trim();
    }

    public String getSupplierBrandname() {
        return supplierBrandname;
    }

    public void setSupplierBrandname(String supplierBrandname) {
        this.supplierBrandname = supplierBrandname == null ? null : supplierBrandname.trim();
    }

    public String getSupplierSeasonno() {
        return supplierSeasonno;
    }

    public void setSupplierSeasonno(String supplierSeasonno) {
        this.supplierSeasonno = supplierSeasonno == null ? null : supplierSeasonno.trim();
    }

    public String getSupplierSeasonname() {
        return supplierSeasonname;
    }

    public void setSupplierSeasonname(String supplierSeasonname) {
        this.supplierSeasonname = supplierSeasonname == null ? null : supplierSeasonname.trim();
    }

    public Byte getIsexistpic() {
        return isexistpic;
    }

    public void setIsexistpic(Byte isexistpic) {
        this.isexistpic = isexistpic;
    }

    public String getSupplierMaterial() {
        return supplierMaterial;
    }

    public void setSupplierMaterial(String supplierMaterial) {
        this.supplierMaterial = supplierMaterial == null ? null : supplierMaterial.trim();
    }

    public String getSupplierOrigin() {
        return supplierOrigin;
    }

    public void setSupplierOrigin(String supplierOrigin) {
        this.supplierOrigin = supplierOrigin == null ? null : supplierOrigin.trim();
    }

    public String getSupplierSpuDesc() {
        return supplierSpuDesc;
    }

    public void setSupplierSpuDesc(String supplierSpuDesc) {
        this.supplierSpuDesc = supplierSpuDesc == null ? null : supplierSpuDesc.trim();
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

    public Byte getInfoState() {
        return infoState;
    }

    public void setInfoState(Byte infoState) {
        this.infoState = infoState;
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
        sb.append(", supplierSpuId=").append(supplierSpuId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierSpuNo=").append(supplierSpuNo);
        sb.append(", supplierSpuModel=").append(supplierSpuModel);
        sb.append(", supplierSpuName=").append(supplierSpuName);
        sb.append(", supplierSpuColor=").append(supplierSpuColor);
        sb.append(", supplierGender=").append(supplierGender);
        sb.append(", supplierCategoryno=").append(supplierCategoryno);
        sb.append(", supplierCategoryname=").append(supplierCategoryname);
        sb.append(", supplierBrandno=").append(supplierBrandno);
        sb.append(", supplierBrandname=").append(supplierBrandname);
        sb.append(", supplierSeasonno=").append(supplierSeasonno);
        sb.append(", supplierSeasonname=").append(supplierSeasonname);
        sb.append(", isexistpic=").append(isexistpic);
        sb.append(", supplierMaterial=").append(supplierMaterial);
        sb.append(", supplierOrigin=").append(supplierOrigin);
        sb.append(", supplierSpuDesc=").append(supplierSpuDesc);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", infoState=").append(infoState);
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
        HubSupplierSpu other = (HubSupplierSpu) that;
        return (this.getSupplierSpuId() == null ? other.getSupplierSpuId() == null : this.getSupplierSpuId().equals(other.getSupplierSpuId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierSpuNo() == null ? other.getSupplierSpuNo() == null : this.getSupplierSpuNo().equals(other.getSupplierSpuNo()))
            && (this.getSupplierSpuModel() == null ? other.getSupplierSpuModel() == null : this.getSupplierSpuModel().equals(other.getSupplierSpuModel()))
            && (this.getSupplierSpuName() == null ? other.getSupplierSpuName() == null : this.getSupplierSpuName().equals(other.getSupplierSpuName()))
            && (this.getSupplierSpuColor() == null ? other.getSupplierSpuColor() == null : this.getSupplierSpuColor().equals(other.getSupplierSpuColor()))
            && (this.getSupplierGender() == null ? other.getSupplierGender() == null : this.getSupplierGender().equals(other.getSupplierGender()))
            && (this.getSupplierCategoryno() == null ? other.getSupplierCategoryno() == null : this.getSupplierCategoryno().equals(other.getSupplierCategoryno()))
            && (this.getSupplierCategoryname() == null ? other.getSupplierCategoryname() == null : this.getSupplierCategoryname().equals(other.getSupplierCategoryname()))
            && (this.getSupplierBrandno() == null ? other.getSupplierBrandno() == null : this.getSupplierBrandno().equals(other.getSupplierBrandno()))
            && (this.getSupplierBrandname() == null ? other.getSupplierBrandname() == null : this.getSupplierBrandname().equals(other.getSupplierBrandname()))
            && (this.getSupplierSeasonno() == null ? other.getSupplierSeasonno() == null : this.getSupplierSeasonno().equals(other.getSupplierSeasonno()))
            && (this.getSupplierSeasonname() == null ? other.getSupplierSeasonname() == null : this.getSupplierSeasonname().equals(other.getSupplierSeasonname()))
            && (this.getIsexistpic() == null ? other.getIsexistpic() == null : this.getIsexistpic().equals(other.getIsexistpic()))
            && (this.getSupplierMaterial() == null ? other.getSupplierMaterial() == null : this.getSupplierMaterial().equals(other.getSupplierMaterial()))
            && (this.getSupplierOrigin() == null ? other.getSupplierOrigin() == null : this.getSupplierOrigin().equals(other.getSupplierOrigin()))
            && (this.getSupplierSpuDesc() == null ? other.getSupplierSpuDesc() == null : this.getSupplierSpuDesc().equals(other.getSupplierSpuDesc()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getInfoState() == null ? other.getInfoState() == null : this.getInfoState().equals(other.getInfoState()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSupplierSpuId() == null) ? 0 : getSupplierSpuId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierSpuNo() == null) ? 0 : getSupplierSpuNo().hashCode());
        result = prime * result + ((getSupplierSpuModel() == null) ? 0 : getSupplierSpuModel().hashCode());
        result = prime * result + ((getSupplierSpuName() == null) ? 0 : getSupplierSpuName().hashCode());
        result = prime * result + ((getSupplierSpuColor() == null) ? 0 : getSupplierSpuColor().hashCode());
        result = prime * result + ((getSupplierGender() == null) ? 0 : getSupplierGender().hashCode());
        result = prime * result + ((getSupplierCategoryno() == null) ? 0 : getSupplierCategoryno().hashCode());
        result = prime * result + ((getSupplierCategoryname() == null) ? 0 : getSupplierCategoryname().hashCode());
        result = prime * result + ((getSupplierBrandno() == null) ? 0 : getSupplierBrandno().hashCode());
        result = prime * result + ((getSupplierBrandname() == null) ? 0 : getSupplierBrandname().hashCode());
        result = prime * result + ((getSupplierSeasonno() == null) ? 0 : getSupplierSeasonno().hashCode());
        result = prime * result + ((getSupplierSeasonname() == null) ? 0 : getSupplierSeasonname().hashCode());
        result = prime * result + ((getIsexistpic() == null) ? 0 : getIsexistpic().hashCode());
        result = prime * result + ((getSupplierMaterial() == null) ? 0 : getSupplierMaterial().hashCode());
        result = prime * result + ((getSupplierOrigin() == null) ? 0 : getSupplierOrigin().hashCode());
        result = prime * result + ((getSupplierSpuDesc() == null) ? 0 : getSupplierSpuDesc().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getInfoState() == null) ? 0 : getInfoState().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}