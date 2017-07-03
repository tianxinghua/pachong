package com.shangpin.ephub.product.business.service.studio.hubslot.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 17/6/30.
 */
@Getter
@Setter
public class SlotSpuSupplierDto {
    private String supplierId;
    private String supplierNo;
    /**
     * 状态  0:未寄出 1：已加入发货单  2：已发货  3:不处理
     */
    private Integer state ;

    private String createTime;
    private String updateTime;
    private String studioName;
}
