package com.shangpin.supplier.product.consumer.supplier.eraldo.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemInfo {
    @SerializedName("product_id")
    @Expose
    private String product_id;
    @SerializedName("product_sku")
    @Expose
    private String product_sku;
    @SerializedName("product_update")
    @Expose
    private String product_update;
    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("product_description")
    @Expose
    private String product_description;
    @SerializedName("product_gender")
    @Expose
    private String product_gender;
    @SerializedName("product_category")
    @Expose
    private String product_category;
    @SerializedName("product_category_id")
    @Expose
    private String product_category_id;
    @SerializedName("product_group")
    @Expose
    private String product_group;
    @SerializedName("product_group_id")
    @Expose
    private String product_group_id;
    @SerializedName("product_brand")
    @Expose
    private String product_brand;
    @SerializedName("product_brand_id")
    @Expose
    private String product_brand_id;
    @SerializedName("product_design_code")
    @Expose
    private String product_design_code;
    @SerializedName("product_design_article")
    @Expose
    private String product_design_article;
    @SerializedName("product_design_model")
    @Expose
    private String product_design_model;
    @SerializedName("product_design_color")
    @Expose
    private String product_design_color;
    @SerializedName("product_material")
    @Expose
    private String product_material;
    @SerializedName("product_dimension")
    @Expose
    private String product_dimension;
    @SerializedName("product_color")
    @Expose
    private String product_color;
    @SerializedName("product_made_in")
    @Expose
    private String product_made_in;
    @SerializedName("product_market_price")
    @Expose
    private String product_market_price;
    @SerializedName("product_purchase_price")
    @Expose
    private String product_purchase_price;
    @SerializedName("product_season")
    @Expose
    private String product_season;
    @SerializedName("product_picture")
    @Expose
    private List<String> product_picture;
    @SerializedName("items")
    @Expose
    private List<Items> items;


}
