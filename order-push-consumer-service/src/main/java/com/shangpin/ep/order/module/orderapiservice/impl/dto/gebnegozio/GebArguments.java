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
public class GebArguments {
    @SerializedName("shipping_amount")
    @Expose
    private Integer shippingAmount;

    @SerializedName("adjustment_positive")
    @Expose
    private Integer adjustmentPositive;

    @SerializedName("adjustment_negative")
    @Expose
    private Integer adjustmentNegative;

    @SerializedName("extension_attributes")
    @Expose
    private ExtAttr extensionAttributes;
}
