package com.shangpin.iog.tony.stock.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2015/10/24.
 */
@Setter
@Getter
public class Additional_info {
    private int qty_diff;
    private int qty;
    private String from;
    private String sku;
    private float stock_price;
    private ShopDTO shop_from;
    private OrderDTO order_id;


}
