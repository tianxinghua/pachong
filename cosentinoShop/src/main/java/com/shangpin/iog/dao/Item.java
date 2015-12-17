package com.shangpin.iog.dao;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Item
 * Created by loyalty on 15/6/5.
 */
@Setter
@Getter
public class Item {
	String sku;
    String title;
    String link;
    String description;
    String category;
    String images; //以;分隔
    String availability;//有效性
    String qty;//库存
    String price;    
    String material;
    String brand;
    String color;
    String item_group_id;
    String size;
    
//    String composition; //材质
//    @SerializedName("g:id")
//    String id;
//    @SerializedName("g:mpn")
//    String mpn;
//    @SerializedName("g:condition")
//    String condition;
//    @SerializedName("g:image_links")
//    ImageLinks imageLinks;
//    @SerializedName("g:brand")
//    String brand;
//    @SerializedName("g:product_type")
//    String productType;
//    @SerializedName("g:google_product_category")//-----------
//    String googleProductCategory;
//    @SerializedName("g:gender")
//    String gender;
//    @SerializedName("g:age_group")
//    String ageGroup;
//    @SerializedName("g:color")
//    String color;
//    @SerializedName("g:size")
//    String size;
//    @SerializedName("g:custom_label_0")
//    String customLabel0;
//    @SerializedName("g:custom_label_2")
//    String customLabel2;
//    @SerializedName("g:custom_label_3")
//    String customLabel3;
//    @SerializedName("g:price")
//    String price;
//    @SerializedName("g:availability")
//    String availability;
//    @SerializedName("g:item_group_id")
//    String itemGroupId;
//    @SerializedName("g:shipping")
//    Shipping shipping;
//    @SerializedName("g:tax")
//    Tax tax;

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getLink() {
//        return link;
//    }
//
//    public void setLink(String link) {
//        this.link = link;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getComposition() {
//        return composition;
//    }
//
//    public void setComposition(String composition) {
//        this.composition = composition;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getMpn() {
//        return mpn;
//    }
//
//    public void setMpn(String mpn) {
//        this.mpn = mpn;
//    }
//
//    public String getCondition() {
//        return condition;
//    }
//
//    public void setCondition(String condition) {
//        this.condition = condition;
//    }
//
//    public ImageLinks getImageLinks() {
//        return imageLinks;
//    }
//
//    public void setImageLinks(ImageLinks imageLinks) {
//        this.imageLinks = imageLinks;
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public String getProductType() {
//        return productType;
//    }
//
//    public void setProductType(String productType) {
//        this.productType = productType;
//    }
//
//    public String getGoogleProductCategory() {
//        return googleProductCategory;
//    }
//
//    public void setGoogleProductCategory(String googleProductCategory) {
//        this.googleProductCategory = googleProductCategory;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getAgeGroup() {
//        return ageGroup;
//    }
//
//    public void setAgeGroup(String ageGroup) {
//        this.ageGroup = ageGroup;
//    }
//
//    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//    public String getSize() {
//        return size;
//    }
//
//    public void setSize(String size) {
//        this.size = size;
//    }
//
//    public String getCustomLabel0() {
//        return customLabel0;
//    }
//
//    public void setCustomLabel0(String customLabel0) {
//        this.customLabel0 = customLabel0;
//    }
//
//    public String getCustomLabel2() {
//        return customLabel2;
//    }
//
//    public void setCustomLabel2(String customLabel2) {
//        this.customLabel2 = customLabel2;
//    }
//
//    public String getCustomLabel3() {
//        return customLabel3;
//    }
//
//    public void setCustomLabel3(String customLabel3) {
//        this.customLabel3 = customLabel3;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public String getAvailability() {
//        return availability;
//    }
//
//    public void setAvailability(String availability) {
//        this.availability = availability;
//    }
//
//    public String getItemGroupId() {
//        return itemGroupId;
//    }
//
//    public void setItemGroupId(String itemGroupId) {
//        this.itemGroupId = itemGroupId;
//    }
//
//    public Shipping getShipping() {
//        return shipping;
//    }
//
//    public void setShipping(Shipping shipping) {
//        this.shipping = shipping;
//    }
//
//    public Tax getTax() {
//        return tax;
//    }
//
//    public void setTax(Tax tax) {
//        this.tax = tax;
//    }

}
