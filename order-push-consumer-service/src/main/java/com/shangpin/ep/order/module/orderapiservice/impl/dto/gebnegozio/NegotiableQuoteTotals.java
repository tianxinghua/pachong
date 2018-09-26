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
public class NegotiableQuoteTotals {
    @SerializedName("items_count")
    @Expose
    private String itemsCount;
    @SerializedName("quote_status")
    @Expose
    private String quoteStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("customer_group")
    @Expose
    private String customerGroup;
    @SerializedName("base_to_quote_rate")
    @Expose
    private String baseToQuoteRate;
    @SerializedName("cost_total")
    @Expose
    private String costTotal;
    @SerializedName("base_cost_total")
    @Expose
    private String baseCostTotal;
    @SerializedName("original_total")
    @Expose
    private String originalTotal;
    @SerializedName("base_original_total")
    @Expose
    private String baseOriginalTotal;
    @SerializedName("original_tax")
    @Expose
    private String originalTax;
    @SerializedName("base_original_tax")
    @Expose
    private String baseOriginalTax;
    @SerializedName("original_price_incl_tax")
    @Expose
    private String originalPriceInclTax;
    @SerializedName("base_original_price_incl_tax")
    @Expose
    private String baseOriginalPriceInclTax;
    @SerializedName("negotiated_price_type")
    @Expose
    private String negotiatedPriceType;
    @SerializedName("negotiated_price_value")
    @Expose
    private String negotiatedPriceValue;
}
