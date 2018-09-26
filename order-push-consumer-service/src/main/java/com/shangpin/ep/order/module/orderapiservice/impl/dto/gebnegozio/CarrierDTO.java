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
public class CarrierDTO {
    @SerializedName("carrier_code")
    @Expose
    private String carrierCode;
    @SerializedName("method_code")
    @Expose
    private String methodCode;
    @SerializedName("carrier_title")
    @Expose
    private String carrierTitle;
    @SerializedName("method_title")
    @Expose
    private String methodTitle;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("base_amount")
    @Expose
    private String baseAmount;
    @SerializedName("available")
    @Expose
    private String available;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("price_excl_tax")
    @Expose
    private String priceExclTax;
    @SerializedName("price_incl_tax")
    @Expose
    private String priceInclTax;
}
