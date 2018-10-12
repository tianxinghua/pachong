package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/9/19.
 */
@Getter
@Setter
public class StockItemDTO {
    private String item_id;
    private String product_id;
    private String stock_id;
    private String qty;
    private String is_in_stock;
}
