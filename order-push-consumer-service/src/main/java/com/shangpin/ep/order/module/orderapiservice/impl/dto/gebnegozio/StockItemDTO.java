package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/9/19.
 */
@Getter
@Setter
public class StockItemDTO {
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("stock_id")
    @Expose
    private String stockId;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("is_in_stock")
    @Expose
    private String isInStock;
}
