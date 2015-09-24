package com.shangpin.iog.dante5.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Item
 * Created by loyalty on 15/6/5.
 */
public class Item {
    String title;
    String link;
    String description;
    String composition; //材质
    @SerializedName("g:id")
    String id;
    @SerializedName("g:mpn")
    String mpn;
    @SerializedName("g:condition")
    String condition;
    @SerializedName("g:image_links")
    ImageLinks imageLinks;
    @SerializedName("g:brand")
    String brand;
    @SerializedName("g:product_type")
    String productType;
    @SerializedName("g:google_product_category")
    String googleProductCategory;
    @SerializedName("g:gender")
    String gender;
    @SerializedName("g:age_group")
    String ageGroup;
    @SerializedName("g:color")
    String color;
    @SerializedName("g:size")
    String size;
    @SerializedName("g:custom_label_0")
    String customLabel0;
    @SerializedName("g:custom_label_2")
    String customLabel2;
    @SerializedName("g:custom_label_3")
    String customLabel3;
    @SerializedName("g:price")
    String price;
    @SerializedName("g:availability")
    String availability;
    @SerializedName("g:item_group_id")
    String itemGroupId;
    @SerializedName("g:shipping")
    Shipping shipping;
    @SerializedName("g:tax")
    Tax tax;

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", composition='" + composition + '\'' +
                ", id='" + id + '\'' +
                ", mpn='" + mpn + '\'' +
                ", condition='" + condition + '\'' +
                ", imageLinks=" + imageLinks +
                ", brand='" + brand + '\'' +
                ", productType='" + productType + '\'' +
                ", googleProductCategory='" + googleProductCategory + '\'' +
                ", gender='" + gender + '\'' +
                ", ageGroup='" + ageGroup + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", customLabel0='" + customLabel0 + '\'' +
                ", customLabel2='" + customLabel2 + '\'' +
                ", customLabel3='" + customLabel3 + '\'' +
                ", price='" + price + '\'' +
                ", availability='" + availability + '\'' +
                ", itemGroupId='" + itemGroupId + '\'' +
                ", shipping=" + shipping +
                ", tax=" + tax +
                '}';
    }
}
