package com.shangpin.iog.levelgroup.dto;

/**
 * Created by Administrator on 2015/11/16.
 */
public class Sku {
    private String brand;
    private String c_categoryId;
    private String c_categoryName;
    private String c_color;
    private String c_madeIn;
    private String c_material;
    private Inventory inventory;
    private String c_season;
    private String c_size;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getC_categoryId() {
        return c_categoryId;
    }

    public void setC_categoryId(String c_categoryId) {
        this.c_categoryId = c_categoryId;
    }

    public String getC_categoryName() {
        return c_categoryName;
    }

    public void setC_categoryName(String c_categoryName) {
        this.c_categoryName = c_categoryName;
    }

    public String getC_color() {
        return c_color;
    }

    public void setC_color(String c_color) {
        this.c_color = c_color;
    }

    public String getC_madeIn() {
        return c_madeIn;
    }

    public void setC_madeIn(String c_madeIn) {
        this.c_madeIn = c_madeIn;
    }

    public String getC_season() {
        return c_season;
    }

    public void setC_season(String c_season) {
        this.c_season = c_season;
    }

    public String getC_size() {
        return c_size;
    }

    public void setC_size(String c_size) {
        this.c_size = c_size;
    }

    public String getC_material() {
        return c_material;
    }

    public void setC_material(String c_material) {
        this.c_material = c_material;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
