package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/14.
 */
@Getter
@Setter
public class PaymentDTO {
    @SerializedName("payment_methods")
    @Expose
    private List<PaymentMethods> paymentMethods = new ArrayList<PaymentMethods>();
    @SerializedName("totals")
    @Expose
    private TotalsDTO totals;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;
}
