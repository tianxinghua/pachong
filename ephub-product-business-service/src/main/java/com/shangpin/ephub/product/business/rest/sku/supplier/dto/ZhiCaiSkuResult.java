package com.shangpin.ephub.product.business.rest.sku.supplier.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lizhongren on 2018/4/9.
 */
@Getter
@Setter
public class ZhiCaiSkuResult implements Serializable {

    private String supplierSkuNo;
    private String spSkuNo;
    private String size;

}
