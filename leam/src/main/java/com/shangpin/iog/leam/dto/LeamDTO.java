package com.shangpin.iog.leam.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by sunny on 2015/8/17.
 */
public class LeamDTO {
    private String stock_id;//Product code of Leam
    private String supplier_sku;//Supplier Code
    private String brand;//Name Brand / Designer
    private String season;//Season
    private String department;//Department (eg: man, woman)
    private String category;//Category (eg: Clothing, Bags)
    private String subcategory;//Gender (eg shirt, jeans)
    private String color;//Color of the head
    private String qty;//Quantity available
    private String size;//Size
    private String price;//Price
    private String default_price;//市场价
    private String madein;//Made in
    private String composition;//Composition of the head
    private String nomenclature;//Data on nomenclature
    private String description;//Description
    private List<String> images;// Array - List List of available images

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

    public String getSupplier_sku() {
        return supplier_sku;
    }

    public void setSupplier_sku(String supplier_sku) {
        this.supplier_sku = supplier_sku;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMadein() {
        return madein;
    }

    public void setMadein(String madein) {
        this.madein = madein;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() { return images; }

    public void setImages(List<String> images) { this.images = images; }

    public String getDefault_price() { return default_price; }

    public void setDefault_price(String default_price) { this.default_price = default_price; }

}
