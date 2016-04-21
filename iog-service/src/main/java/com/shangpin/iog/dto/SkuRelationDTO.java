package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by loyalty on 15/10/2.
 * 供货商SKU和SOPSKU对账关系
 */
@Setter
@Getter
public class SkuRelationDTO {
    private String supplierId;//供货商门户ID
    private String sopNo;//供货商SOP编号
    private String sopSkuId;//尚品的SKU
    private String supplierSkuId;
    private Date createTime;

    @Override
    public String toString() {
        return "SkuRelationDTO{" +
                "supplierId='" + supplierId + '\'' +
                ", sopNo='" + sopNo + '\'' +
                ", sopSkuId='" + sopSkuId + '\'' +
                ", supplierSkuId='" + supplierSkuId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
