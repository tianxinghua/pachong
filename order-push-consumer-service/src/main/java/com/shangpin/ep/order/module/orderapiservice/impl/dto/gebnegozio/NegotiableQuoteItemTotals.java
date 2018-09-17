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
public class NegotiableQuoteItemTotals {
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("catalog_price")
    @Expose
    private String catalogPrice;
    @SerializedName("base_catalog_price")
    @Expose
    private String baseCatalogPrice;
    @SerializedName("catalog_price_incl_tax")
    @Expose
    private String catalogPriceInclTax;
    @SerializedName("base_catalog_price_incl_tax")
    @Expose
    private String baseCatalogPriceInclTax;
    @SerializedName("cart_price")
    @Expose
    private String cartPrice;
    @SerializedName("base_cart_price")
    @Expose
    private String baseCartPrice;
    @SerializedName("cart_tax")
    @Expose
    private String cartTax;
    @SerializedName("base_cart_tax")
    @Expose
    private String baseCartTax;
    @SerializedName("cart_price_incl_tax")
    @Expose
    private String cartPriceInclTax;
    @SerializedName("base_cart_price_incl_tax")
    @Expose
    private String baseCartPriceInclTax;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes ExtensionAttributes;
}
