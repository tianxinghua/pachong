package com.shangpin.ephub.product.business.ui.pendingCrud.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 * spu pending
 */

@Getter
@Setter
public class SpuPendingCommonVO implements Serializable{

    private Long spuPendingId;//spuPending表主键

    private String spuModel;//货号

    private String color;

    private String brandNo;

    private String categoryNo;

    private String supplierNo;

    private String supplierId;

    private String material;//材质

    private String origin;//产地

    private String updateTime ;
}
