package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/10/15.
 */
@Getter
@Setter
public class AddressRequ {
    @SerializedName("address")
    @Expose
    private Address address;
}
