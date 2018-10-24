package com.shangpin.supplier.product.consumer.supplier.mclables.dto;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemInfo {
    @SerializedName("Images")
    @Expose
    private List<String> images;
    @SerializedName("Material")
    @Expose
    private List<String> material;
    @SerializedName("Sku")
    @Expose
    private String  sku;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("ListPrice")
    @Expose
    private String listPrice;
    @SerializedName("RetailPrice")
    @Expose
    private String retailPrice;
    @SerializedName("Season")
    @Expose
    private String season;
    @SerializedName("ParentCategory")
    @Expose
    private String parentCategory;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("Condition")
    @Expose
    private String condition;
    @SerializedName("Brand")
    @Expose
    private String brand;
    @SerializedName("Variants")
    @Expose
    private List<Variantinfo> Variants;
    @SerializedName("CreatedDatetimeUtc")
    @Expose
    private String createdDatetimeUtc;
    @SerializedName("LastUpdatedDatetimeUtc")
    @Expose
    private String lastUpdatedDatetimeUtc;


}
