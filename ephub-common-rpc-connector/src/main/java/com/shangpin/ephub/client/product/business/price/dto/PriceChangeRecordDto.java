package com.shangpin.ephub.client.product.business.price.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2017/4/7.
 */
@Getter
@Setter
public class PriceChangeRecordDto {

    private Long id;
    private String supplierId;
    private String skuNo;
    private String supplierSkuNo;
    private String memo;
}
