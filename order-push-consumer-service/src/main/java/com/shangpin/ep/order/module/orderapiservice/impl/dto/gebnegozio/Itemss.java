package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/9/16.
 */
@Getter
@Setter
public class Itemss {
    @SerializedName("order_item_id")
    @Expose
    private Integer orderItemId;
    @SerializedName("qty")
    @Expose
    private Integer qty;
}
