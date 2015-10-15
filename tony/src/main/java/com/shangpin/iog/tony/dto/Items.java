package com.shangpin.iog.tony.dto;

import java.util.List;

/**
 * Created by Administrator on 2015/9/21.
 */
public class Items {

    private Object _id;
    private String cur;
    private String color;
    private String sex;
    private String title;
    private Object cat_ids;
    private String season;
    private String sku;
    private String brand;
    private String barcode;
    private String age;
    private String desc;
    private String qty;
    private String stock_price;
    private String[] images;
    private Object cat_id;

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getCat_ids() {
        return cat_ids;
    }

    public void setCat_ids(Object cat_ids) {
        this.cat_ids = cat_ids;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStock_price() {
        return stock_price;
    }

    public void setStock_price(String stock_price) {
        this.stock_price = stock_price;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public Object getCat_id() {
        return cat_id;
    }

    public void setCat_id(Object cat_id) {
        this.cat_id = cat_id;
    }
}
