package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/9/14.
 */
@Getter
@Setter
public class AddressInformation {
    @SerializedName("shipping_address")
    @Expose
    private Address shippingAddress;

    @SerializedName("billing_address")
    @Expose
    private Address billingAddress;
    @SerializedName("shipping_method_code")
    @Expose
    private String shippingMethodCode;
    @SerializedName("shipping_carrier_code")
    @Expose
    private String shippingCarrierCode;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;
    @SerializedName("custom_attributes")
    @Expose
    private CustomAttributes customAttributes;
}
