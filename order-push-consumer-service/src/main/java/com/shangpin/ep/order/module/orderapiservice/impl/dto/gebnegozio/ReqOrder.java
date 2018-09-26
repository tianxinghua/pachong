package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri.BillingAddress;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/9/15.
 */
@Getter
@Setter
public class ReqOrder {
    @SerializedName("paymentMethod")
    @Expose
    private PaymentMethod paymentMethod;

    @SerializedName("billingAddress")
    @Expose
    private Address billingAddress;
}
