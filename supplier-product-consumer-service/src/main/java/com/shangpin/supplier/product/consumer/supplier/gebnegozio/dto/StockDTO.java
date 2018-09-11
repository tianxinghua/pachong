package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/9/8.
 */
@Getter
@Setter
public class StockDTO {
    private String product_id;
    private String stock_id;
    private String qty;
    private String stock_status;
}
