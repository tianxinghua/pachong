package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by huxia on 2015/9/10.
 */
@Getter
@Setter
public class OrderDTO {
    private BigInteger id;
    private String supplierId; //供货商门户编号
    private String supplierNo;//供货商编号
    private String uuId;  //和供货商公用的订单唯一标识
    private String spOrderId;    //订单编号
    private String spPurchaseNo; //采购单编号
    private String spPurchaseDetailNo;
    private String detail;//
    private String status;//订单状态
    private String memo;//备注
    private String deliveryNo;//发货单
    private Date createTime;
    private Date updateTime;
    private String excState;
    private String excDesc;
    private Date excTime;
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getSpOrderId() {
        return spOrderId;
    }

    public void setSpOrderId(String spOrderId) {
        this.spOrderId = spOrderId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getExcState() {
        return excState;
    }

    public void setExcState(String excState) {
        this.excState = excState;
    }

    public String getExcDesc() {
        return excDesc;
    }

    public void setExcDesc(String excDesc) {
        this.excDesc = excDesc;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", supplierId='" + supplierId + '\'' +
                ", uuId='" + uuId + '\'' +
                ", spOrderId='" + spOrderId + '\'' +
                ", spPurchaseDetailNo='" + spPurchaseDetailNo + '\'' +
                ", detail='" + detail + '\'' +
                ", status='" + status + '\'' +
                ", memo='" + memo + '\'' +
                ", deliveryNo='" + deliveryNo + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", excState='" + excState + '\'' +
                ", excDesc='" + excDesc + '\'' +
                ", excTime=" + excTime +
                '}';
    }
}
