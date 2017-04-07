package com.shangpin.ephub.product.business.rest.price.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2017/4/7.
 */
@Getter
@Setter
public class PriceChangeRecordDto {

    private Long id; //传入的主键ID ,若没有 不填或者赋值0
    private String sign;//成功 ： 1  失败 ：0
    private String supplierId;
    private String skuNo;
    private String supplierSkuNo;
    private String memo;
}
