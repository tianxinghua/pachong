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
public class NegotiableQuoteItem {
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("original_price")
    @Expose
    private String originalPrice;
    @SerializedName("original_tax_amount")
    @Expose
    private String originalTaxAmount;
    @SerializedName("original_discount_amount")
    @Expose
    private String originalDiscountAmount;
    @SerializedName("original_discount_amount")
    @Expose
    private ExtensionAttributes extensionAttributes;
}
