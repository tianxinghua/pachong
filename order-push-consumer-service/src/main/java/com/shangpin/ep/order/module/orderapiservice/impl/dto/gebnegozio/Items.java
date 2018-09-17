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
public class Items {
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("base_price")
    @Expose
    private String basePrice;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("row_total")
    @Expose
    private String rowTotal;
    @SerializedName("base_row_total")
    @Expose
    private String baseRowTotal;
    @SerializedName("row_total_with_discount")
    @Expose
    private String rowTotalWithDiscount;
    @SerializedName("tax_amount")
    @Expose
    private String taxAmount;
    @SerializedName("base_tax_amount")
    @Expose
    private String baseTaxAmount;
    @SerializedName("tax_percent")
    @Expose
    private String taxPercent;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("base_discount_amount")
    @Expose
    private String baseDiscountAmount;
    @SerializedName("discountPercent")
    @Expose
    private String discount_percent;
    @SerializedName("price_incl_tax")
    @Expose
    private String priceInclTax;
    @SerializedName("base_price_incl_tax")
    @Expose
    private String basePriceInclTax;
    @SerializedName("row_total_incl_tax")
    @Expose
    private String rowTotalInclTax;
    @SerializedName("base_row_total_incl_tax")
    @Expose
    private String baseRowTotalInclTax;
    @SerializedName("options")
    @Expose
    private String options;
    @SerializedName("weee_tax_applied_amount")
    @Expose
    private String weeeTaxAppliedAmount;
    @SerializedName("weee_tax_applied")
    @Expose
    private String weeeTaxApplied;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;
    @SerializedName("name")
    @Expose
    private String name;
}
