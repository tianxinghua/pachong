package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by huxia on 2015/9/10.
 */
/*@Getter
@Setter*/
public class OrderDTO {
    private BigInteger id;
    private String supplierId;
    private String uuId;  //和供货商公用的订单唯一标识
    private String spOrderId;
    private String detail;//
    private String status;//订单状态
    private Date createTime;
    private Date updateTime;

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

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", supplierId='" + supplierId + '\'' +
                ", uuId='" + uuId + '\'' +
                ", spOrderId='" + spOrderId + '\'' +
                ", detail='" + detail + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +

                '}';
    }
}
