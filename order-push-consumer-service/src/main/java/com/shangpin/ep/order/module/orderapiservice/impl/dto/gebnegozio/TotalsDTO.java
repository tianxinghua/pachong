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
public class TotalsDTO {
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("base_grand_total")
    @Expose
    private String baseGrandTotal;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("base_subtotal")
    @Expose
    private String baseSubtotal;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("base_discount_amount")
    @Expose
    private String baseDiscountAmount;
    @SerializedName("subtotal_with_discount")
    @Expose
    private String subtotalWithDiscount;
    @SerializedName("base_subtotal_with_discount")
    @Expose
    private String baseSubtotalWithDiscount;
    @SerializedName("shipping_amount")
    @Expose
    private String shippingAmount;
    @SerializedName("base_shipping_amount")
    @Expose
    private String baseShippingAmount;
    @SerializedName("shipping_discount_amount")
    @Expose
    private String shippingDiscountAmount;
    @SerializedName("base_shipping_discount_amount")
    @Expose
    private String baseShippingDiscountAmount;
    @SerializedName("tax_amount")
    @Expose
    private String taxAmount;
    @SerializedName("base_tax_amount")
    @Expose
    private String baseTaxAmount;
    @SerializedName("weee_tax_applied_amount")
    @Expose
    private String weeeTaxAppliedAmount;
    @SerializedName("shipping_tax_amount")
    @Expose
    private String shippingTaxAmount;
    @SerializedName("base_shipping_tax_amount")
    @Expose
    private String baseShippingTaxAmount;
    @SerializedName("subtotal_incl_tax")
    @Expose
    private String subtotalInclTax;
    @SerializedName("base_subtotal_incl_tax")
    @Expose
    private String baseSubtotalInclTax;
    @SerializedName("shipping_incl_tax")
    @Expose
    private String shippingInclTax;
    @SerializedName("base_shipping_incl_tax")
    @Expose
    private String baseShippingInclTax;
    @SerializedName("base_currency_code")
    @Expose
    private String baseCurrencyCode;
    @SerializedName("quote_currency_code")
    @Expose
    private String quoteCurrencyCode;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("items_qty")
    @Expose
    private String itemsQty;
    @SerializedName("items")
    @Expose
    private List<Items> items =  new ArrayList<Items>();
    @SerializedName("total_segments")
    @Expose
    private List<TotalSegments> totalSegments = new ArrayList<TotalSegments>();
}
