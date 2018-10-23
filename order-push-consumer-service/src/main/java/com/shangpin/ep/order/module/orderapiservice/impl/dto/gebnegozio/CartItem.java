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
public class CartItem {
    @SerializedName("item_id")
    @Expose
    private String itemId;

    @SerializedName("sku")
    @Expose
    private String sku;

    @SerializedName("qty")
    @Expose
    private Integer qty;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("product_type")
    @Expose
    private String productType;

    @SerializedName("quote_id")
    @Expose
    private String quoteId;

    @SerializedName("product_option")
    @Expose
    private ProductOption productOption;

    /*@SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;*/
}
