package com.shangpin.iog.styleside.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SkuDTO {

    //{"product_sku":"3376856_40","qty":"0.0000"}
    /**
     * 供应商原始 skuNo
     */
    private String product_sku;
    /**
     * 库存
     */
    private String qty;
}
