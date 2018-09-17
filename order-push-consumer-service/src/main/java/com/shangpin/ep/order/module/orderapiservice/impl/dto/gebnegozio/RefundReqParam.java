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
public class RefundReqParam {
    @SerializedName("items")
    @Expose
    private Itemss items;
    @SerializedName("notify")
    @Expose
    private boolean notify;
    @SerializedName("arguments")
    @Expose
    private GebArguments arguments;
}
