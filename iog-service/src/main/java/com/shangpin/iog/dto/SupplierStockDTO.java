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

}
