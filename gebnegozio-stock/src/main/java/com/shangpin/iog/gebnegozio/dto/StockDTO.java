package com.shangpin.iog.gebnegozio.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/9/12.
 */
@Getter
@Setter
public class StockDTO {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("stock_id")
    @Expose
    private String stockId;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("stock_status")
    @Expose
    private String stockStatus;

}
