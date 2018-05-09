package com.shangpin.ephub.product.business.rest.sku.supplier.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lizhongren on 2018/4/9.
 */
@Getter
@Setter
public class StockResult implements Serializable {

    String supplierSkuNo;
    String size;
    Integer stock;

}
