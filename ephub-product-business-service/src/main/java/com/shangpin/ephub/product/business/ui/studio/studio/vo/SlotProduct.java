package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/13.
 */

@Getter
@Setter
@ToString
public class SlotProduct implements Serializable {
    private static final long serialVersionUID = -3375361117729011883L;

    private Long id;
    private String supplierNo;
    private String supplierId;
    private Long spuPendingId;
    private Long supplierSpuId;
    private String slotSpuNo;
    private Long slotSpuSupplierId;
    private String supplierSpuName;
    private String supplierBrandName;
    private String supplierCategoryName;
    private String supplierSpuModel;
    private String supplierSeasonName;
    private Byte sendState;
    private Byte arriveState;
    private Long version;
}
