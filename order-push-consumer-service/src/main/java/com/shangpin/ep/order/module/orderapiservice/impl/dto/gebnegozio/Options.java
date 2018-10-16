package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/9/29.
 */
@Getter
@Setter
public class Options {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("value")
    @Expose
    private String label;
}
