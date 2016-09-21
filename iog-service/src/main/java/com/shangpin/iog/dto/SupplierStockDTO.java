package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by lizhongren on 2015/10/24.
 */
@Getter
@Setter
public class SupplierStockDTO {

    private String id;
    private String supplierId;
    private String supplierSkuId;
    private Integer quantity;
    private Date optTime;//操作时间
    private String status;//状态  1、待处理 2、已处理

}
